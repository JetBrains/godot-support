package gdscript.embeddedDocs

import gdscript.embeddedDocs.GdEmbeddedDocExtractor.InflateResult
import gdscript.embeddedDocs.GdEmbeddedDocExtractor.MARKER_WINDOW_SIZE
import gdscript.embeddedDocs.GdEmbeddedDocExtractor.PROBE_INPUT_CHUNK_SIZE
import gdscript.embeddedDocs.GdEmbeddedDocExtractor.PROBE_LIMIT
import gdscript.embeddedDocs.GdEmbeddedDocExtractor.containsAscii
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.util.zip.DataFormatException
import java.util.zip.Inflater

internal object GdZlibEmbeddedDocs : GdBinaryDocMethod {
    override val name: String = "zlib"
    override val headerSize: Int = 2

    private const val INFLATE_BUFFER_SIZE = 16 * 1024

    override fun isHeader(window: Int): Boolean {
        val cmf = (window ushr 8) and 0xFF
        val flg = window and 0xFF
        val compressionMethod = cmf and 0x0F
        return cmf == 0x78 && compressionMethod == 8 && ((cmf shl 8) or flg) % 31 == 0
    }

    override fun probe(channel: FileChannel, offset: Long, checkCanceled: () -> Unit): Boolean {
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
            InflateResult.CapExceeded, InflateResult.Rejected -> return false
        }
        val windowLength = minOf(bytes.size, MARKER_WINDOW_SIZE)
        return bytes.containsAscii("<?xml", windowLength) || bytes.containsAscii("<class ", windowLength)
    }

    /**
     * Inflates one zlib-wrapped DEFLATE stream, which Godot writes at compression level 9.
     * The stream has no index, so this function cannot inflate a later range independently.
     * A probe stops after its small output limit. Full extraction reads from the stream head until completion or a safety limit.
     */
    override fun inflate(channel: FileChannel, offset: Long, limit: Int, checkCanceled: () -> Unit): InflateResult =
        inflate(channel, offset, limit, checkCanceled, stopAtLimit = false)

    private fun inflate(
        channel: FileChannel,
        offset: Long,
        limit: Int,
        checkCanceled: () -> Unit,
        stopAtLimit: Boolean,
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
}
