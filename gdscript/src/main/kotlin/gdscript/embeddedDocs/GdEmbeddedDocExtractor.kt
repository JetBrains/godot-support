package gdscript.embeddedDocs

import com.intellij.openapi.progress.ProgressManager
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Path
import java.util.zip.DataFormatException
import java.util.zip.Inflater

/**
 * Extracts documentation from a readable Godot executable. The executable can contain one or more zlib streams at arbitrary byte offsets.
 *
 * The extractor:
 * 1. Scans for valid zlib headers.
 * 2. Probes each candidate for an XML marker.
 * 3. Inflates each accepted stream from its start.
 *
 * Cancellation checks, size caps, and a candidate limit bound the work and memory use.
 * Invalid candidates do not stop the scan. Input errors stop the scan and return an explicit result.
 */
object GdEmbeddedDocExtractor {
    const val SCAN_CHUNK_SIZE: Int = 64 * 1024

    private const val PROBE_LIMIT = 8 * 1024
    private const val PROBE_INPUT_CHUNK_SIZE = 256
    private const val MARKER_WINDOW_SIZE = 64
    private const val STREAM_LIMIT = 64 * 1024 * 1024
    private const val TOTAL_LIMIT = 128 * 1024 * 1024
    private const val INFLATE_BUFFER_SIZE = 16 * 1024

    // The candidate limit is max(250,000, file size / 256). The floor permits many candidates in smaller files.
    // The divisor lets the limit grow for larger files. Together, they bound the scan and probe cost for pathological input.
    // A real binary produced 7,628 candidates. A 1.19 GB file has a limit of approximately 4.65 million candidates.
    private const val MIN_CANDIDATE_LIMIT = 250_000L
    private const val CANDIDATE_DENSITY_DIVISOR = 256L

    data class EmbeddedDocStream(
        val bytes: ByteArray,
        val fileOffset: Long,
        val compressedLength: Long,
        val uncompressedLength: Int,
    )

    enum class NotFoundReason {
        INPUT_UNAVAILABLE,
        NO_CANDIDATE_FOUND,
        NO_CANDIDATE_VALIDATED,
        CAP_EXCEEDED,
        SCAN_GUARD_EXCEEDED,
    }

    sealed interface Result {
        data class Found(
            val streams: List<EmbeddedDocStream>,
            val candidateCount: Int,
            val acceptedTotalExceeded: Boolean = false,
            val streamCapExceeded: Boolean = false,
        ) : Result

        data class NotFound(
            val reason: NotFoundReason,
            val candidateCount: Int,
        ) : Result
    }

    fun extract(path: Path, checkCanceled: () -> Unit = { ProgressManager.checkCanceled() }): Result {
        val accepted = mutableListOf<EmbeddedDocStream>()
        var candidateCount = 0
        var capExceeded = false
        var totalCapExceeded = false
        var scanGuardExceeded = false
        var acceptedTotal = 0L

        try {
            RandomAccessFile(path.toFile(), "r").use { file ->
                val channel = file.channel
                val candidateLimit = candidateLimit(channel.size())
                val buffer = ByteBuffer.allocate(SCAN_CHUNK_SIZE)
                var previous = -1
                var filePosition = 0L
                while (!totalCapExceeded && !scanGuardExceeded) {
                    checkCanceled()
                    buffer.clear()
                    val count = channel.read(buffer, filePosition)
                    if (count < 0) break
                    if (count == 0) continue

                    val bytes = buffer.array()
                    // The scan reads each byte once. The two-byte window finds a zlib header across a 64 KiB chunk boundary.
                    for (index in 0 until count) {
                        val current = bytes[index].toInt() and 0xFF
                        if (previous >= 0 && isZlibHeader(previous, current)) {
                            candidateCount++
                            if (candidateCount.toLong() > candidateLimit) {
                                scanGuardExceeded = true
                                break
                            }

                            val offset = filePosition + index - 1
                            if (probe(channel, offset, checkCanceled) == ProbeResult.Accepted) {
                                val totalRemaining = (TOTAL_LIMIT - acceptedTotal).toInt()
                                val inflateLimit = minOf(STREAM_LIMIT, totalRemaining)
                                when (val inflated = inflate(channel, offset, inflateLimit, checkCanceled)) {
                                    is InflateResult.Complete -> {
                                        accepted.add(
                                            EmbeddedDocStream(
                                                bytes = inflated.bytes,
                                                fileOffset = offset,
                                                compressedLength = inflated.compressedLength,
                                                uncompressedLength = inflated.bytes.size,
                                            )
                                        )
                                        acceptedTotal += inflated.bytes.size
                                    }

                                    InflateResult.CapExceeded -> {
                                        if (totalRemaining < STREAM_LIMIT) totalCapExceeded = true
                                        else capExceeded = true
                                    }

                                    is InflateResult.Partial, InflateResult.Rejected -> Unit
                                }
                            }
                        }
                        previous = current
                    }
                    filePosition += count
                }
            }
        } catch (_: IOException) {
            return Result.NotFound(NotFoundReason.INPUT_UNAVAILABLE, candidateCount)
        } catch (_: SecurityException) {
            return Result.NotFound(NotFoundReason.INPUT_UNAVAILABLE, candidateCount)
        }

        if (scanGuardExceeded) return Result.NotFound(NotFoundReason.SCAN_GUARD_EXCEEDED, candidateCount)
        if (accepted.isNotEmpty()) {
            return Result.Found(
                streams = accepted,
                candidateCount = candidateCount,
                acceptedTotalExceeded = totalCapExceeded,
                streamCapExceeded = capExceeded,
            )
        }
        val reason = when {
            capExceeded || totalCapExceeded -> NotFoundReason.CAP_EXCEEDED
            candidateCount == 0 -> NotFoundReason.NO_CANDIDATE_FOUND
            else -> NotFoundReason.NO_CANDIDATE_VALIDATED
        }
        return Result.NotFound(reason, candidateCount)
    }

    private fun candidateLimit(fileSize: Long): Long =
        maxOf(MIN_CANDIDATE_LIMIT, fileSize / CANDIDATE_DENSITY_DIVISOR)

    private fun isZlibHeader(cmf: Int, flg: Int): Boolean {
        val compressionMethod = cmf and 0x0F
        return cmf == 0x78 && compressionMethod == 8 && ((cmf shl 8) or flg) % 31 == 0
    }

    private fun probe(channel: FileChannel, offset: Long, checkCanceled: () -> Unit): ProbeResult {
        val result = inflate(
            channel = channel,
            offset = offset,
            limit = MARKER_WINDOW_SIZE,
            checkCanceled = checkCanceled,
            stopAtLimit = true,
            inputLimit = PROBE_LIMIT,
            inputChunkSize = PROBE_INPUT_CHUNK_SIZE,
        )
        val bytes = when (result) {
            is InflateResult.Complete -> result.bytes
            is InflateResult.Partial -> result.bytes
            else -> return ProbeResult.Rejected
        }
        val windowLength = minOf(bytes.size, MARKER_WINDOW_SIZE)
        return if (bytes.containsAscii("<?xml", windowLength) || bytes.containsAscii("<class ", windowLength)) {
            ProbeResult.Accepted
        } else {
            ProbeResult.Rejected
        }
    }

    /**
     * Inflates one zlib-wrapped DEFLATE stream, which Godot writes at compression level 9.
     * The stream has no index, so this function cannot inflate a later range independently.
     * A probe stops after its small output limit. Full extraction reads from the stream head until completion or a safety limit.
     */
    private fun inflate(
        channel: FileChannel,
        offset: Long,
        limit: Int,
        checkCanceled: () -> Unit,
        stopAtLimit: Boolean = false,
        inputLimit: Int? = null,
        inputChunkSize: Int = INFLATE_BUFFER_SIZE,
    ): InflateResult {
        val inflater = Inflater()
        var inputPosition = offset
        var inputTotal = 0
        try {
            val inputBuffer = ByteArray(inputChunkSize)
            val outputBuffer = ByteArray(INFLATE_BUFFER_SIZE)
            val output = ByteArrayOutputStream(minOf(limit, PROBE_LIMIT))
            while (true) {
                checkCanceled()
                if (inflater.needsInput()) {
                    val allowed = inputLimit?.let { minOf(inputBuffer.size, it - inputTotal) } ?: inputBuffer.size
                    if (allowed <= 0) return InflateResult.Rejected
                    val count = channel.read(ByteBuffer.wrap(inputBuffer, 0, allowed), inputPosition)
                    if (count <= 0) return InflateResult.Rejected
                    inputTotal += count
                    inputPosition += count
                    inflater.setInput(inputBuffer, 0, count)
                }

                val count = try {
                    inflater.inflate(outputBuffer)
                } catch (_: DataFormatException) {
                    return InflateResult.Rejected
                }
                if (count > 0) {
                    if (output.size() + count > limit) {
                        if (stopAtLimit) {
                            val remaining = limit - output.size()
                            if (remaining > 0) output.write(outputBuffer, 0, remaining)
                            return InflateResult.Partial(output.toByteArray())
                        }
                        return InflateResult.CapExceeded
                    }
                    output.write(outputBuffer, 0, count)
                }
                if (inflater.finished()) {
                    return InflateResult.Complete(output.toByteArray(), inflater.bytesRead)
                }
                if (inflater.needsDictionary() || count == 0 && !inflater.needsInput()) return InflateResult.Rejected
            }
        }
        finally {
            inflater.end()
        }
    }

    private fun ByteArray.containsAscii(value: String, endExclusive: Int): Boolean {
        val pattern = value.toByteArray(Charsets.US_ASCII)
        for (start in 0..endExclusive - pattern.size) {
            var matches = true
            for (index in pattern.indices) {
                if (this[start + index] != pattern[index]) {
                    matches = false
                    break
                }
            }
            if (matches) return true
        }
        return false
    }

    private enum class ProbeResult {
        Accepted,
        Rejected,
    }

    private sealed interface InflateResult {
        data class Complete(val bytes: ByteArray, val compressedLength: Long) : InflateResult
        data class Partial(val bytes: ByteArray) : InflateResult
        data object CapExceeded : InflateResult
        data object Rejected : InflateResult
    }
}
