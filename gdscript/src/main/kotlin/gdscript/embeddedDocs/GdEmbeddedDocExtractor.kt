package gdscript.embeddedDocs

import com.intellij.openapi.progress.ProgressManager
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.file.Path

/**
 * Extracts documentation from zlib streams and Zstandard frames at arbitrary offsets in a readable Godot executable.
 * The scan probes each candidate for an XML marker before it inflates it.
 *
 * Cancellation checks, size caps, and a candidate limit bound the work and memory use.
 * Invalid candidates do not stop the scan. Input errors stop the scan and return an explicit result.
 */
object GdEmbeddedDocExtractor {
    const val SCAN_CHUNK_SIZE: Int = 64 * 1024
    internal const val MARKER_WINDOW_SIZE = 64
    internal const val PROBE_LIMIT = 8 * 1024
    internal const val PROBE_INPUT_CHUNK_SIZE = 256
    private const val STREAM_LIMIT = 64 * 1024 * 1024
    private const val TOTAL_LIMIT = 128 * 1024 * 1024

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

    internal fun extract(
        path: Path,
        method: GdBinaryDocMethod,
        checkCanceled: () -> Unit = { ProgressManager.checkCanceled() },
    ): Result {
        val accepted = mutableListOf<EmbeddedDocStream>()
        var candidateCount = 0
        var capExceeded = false
        var totalCapExceeded = false
        var scanGuardExceeded = false
        var acceptedTotal = 0L

        try {
            @Suppress("IO_FILE_USAGE")
            RandomAccessFile(path.toFile(), "r").use { file ->
                val channel = file.channel
                val candidateLimit = candidateLimit(channel.size())
                val buffer = ByteBuffer.allocate(SCAN_CHUNK_SIZE)
                // Keep the last four scanned bytes across chunk boundaries.
                var headerWindow = 0
                var filePosition = 0L
                while (!totalCapExceeded && !scanGuardExceeded) {
                    checkCanceled()
                    buffer.clear()
                    val count = channel.read(buffer, filePosition)
                    if (count < 0) break
                    if (count == 0) continue

                    val bytes = buffer.array()
                    // Keep the method header across scan boundaries.
                    for (index in 0 until count) {
                        val current = bytes[index].toInt() and 0xFF
                        headerWindow = (headerWindow shl 8) or current
                        val headerSize = method.headerSize
                        // Require enough file bytes to keep the candidate offset non-negative.
                        if (method.isHeader(headerWindow) && filePosition + index >= headerSize - 1) {
                            candidateCount++
                            if (candidateCount.toLong() > candidateLimit) {
                                scanGuardExceeded = true
                                break
                            }

                            val offset = filePosition + index - headerSize + 1
                            val probed = method.probe(channel, offset, checkCanceled)
                            if (probed) {
                                val totalRemaining = (TOTAL_LIMIT - acceptedTotal).toInt()
                                val inflateLimit = minOf(STREAM_LIMIT, totalRemaining)
                                val inflated = method.inflate(channel, offset, inflateLimit, checkCanceled)
                                when (inflated) {
                                    is InflateResult.Complete -> {
                                        val window = minOf(inflated.bytes.size, MARKER_WINDOW_SIZE)
                                        if (!inflated.bytes.containsAscii("<?xml", window) &&
                                            !inflated.bytes.containsAscii("<class ", window)) continue
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

    internal fun ByteArray.containsAscii(value: String, endExclusive: Int): Boolean {
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

    internal sealed interface InflateResult {
        data class Complete(val bytes: ByteArray, val compressedLength: Long) : InflateResult
        data class Partial(val bytes: ByteArray) : InflateResult
        data object CapExceeded : InflateResult
        data object Rejected : InflateResult
    }
}
