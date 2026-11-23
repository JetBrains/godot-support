package gdscript.embeddedDocs

import com.github.luben.zstd.ZstdDecompressCtx
import com.github.luben.zstd.ZstdException
import com.intellij.openapi.diagnostic.logger
import gdscript.embeddedDocs.GdEmbeddedDocExtractor.InflateResult
import gdscript.embeddedDocs.GdEmbeddedDocExtractor.MARKER_WINDOW_SIZE
import gdscript.embeddedDocs.GdEmbeddedDocExtractor.PROBE_INPUT_CHUNK_SIZE
import gdscript.embeddedDocs.GdEmbeddedDocExtractor.containsAscii
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

internal object GdZstdEmbeddedDocs : GdBinaryDocMethod {
    override val name: String = "zstd"
    override val headerSize: Int = 4

    private val LOG = logger<GdZstdEmbeddedDocs>()
    private const val BUFFER_SIZE = 16 * 1024
    // Zstandard can require a complete 128 KiB block plus the frame header before it produces output.
    private const val BLOCK_PROBE_INPUT_LIMIT = 128 * 1024 + 64

    override fun isHeader(window: Int): Boolean = window == 0x28B52FFD

    override fun probe(channel: FileChannel, offset: Long, checkCanceled: () -> Unit): Boolean {
        val result = inflate(channel, offset, MARKER_WINDOW_SIZE, checkCanceled, stopAtLimit = true)
        val bytes = when (result) {
            is InflateResult.Complete -> result.bytes
            is InflateResult.Partial -> result.bytes
            InflateResult.CapExceeded, InflateResult.Rejected -> return false
        }
        val windowLength = minOf(bytes.size, MARKER_WINDOW_SIZE)
        return bytes.containsAscii("<?xml", windowLength) || bytes.containsAscii("<class ", windowLength)
    }

    override fun inflate(channel: FileChannel, offset: Long, limit: Int, checkCanceled: () -> Unit): InflateResult =
        inflate(channel, offset, limit, checkCanceled, stopAtLimit = false)

    private fun inflate(
        channel: FileChannel,
        offset: Long,
        limit: Int,
        checkCanceled: () -> Unit,
        stopAtLimit: Boolean,
    ): InflateResult {
        // Decode one frame. Buffer positions give the compressed length without including the following bytes.
        try {
            ZstdDecompressCtx().use { context ->
                val input = ByteBuffer.allocateDirect(BUFFER_SIZE)
                input.limit(0)
                val output = ByteBuffer.allocateDirect(BUFFER_SIZE)
                val bytes = ByteArray(BUFFER_SIZE)
                val result = ByteArrayOutputStream()
                var position = offset
                while (true) {
                    checkCanceled()
                    if (!input.hasRemaining()) {
                        input.clear()
                        if (stopAtLimit) {
                            val allowed = minOf(PROBE_INPUT_CHUNK_SIZE.toLong(), BLOCK_PROBE_INPUT_LIMIT - (position - offset)).toInt()
                            if (allowed <= 0) {
                                LOG.debug(
                                    "False candidate at $offset: no documentation output within the probe input limit " +
                                        "of $BLOCK_PROBE_INPUT_LIMIT bytes",
                                )
                                return InflateResult.Rejected
                            }
                            input.limit(allowed)
                        }
                        val count = channel.read(input, position)
                        if (count > 0) position += count
                        input.flip()
                    }
                    output.clear()
                    if (stopAtLimit) output.limit(minOf(BUFFER_SIZE, limit - result.size()))
                    val previousPosition = input.position()
                    val complete = context.decompressDirectByteBufferStream(output, input)
                    val count = output.position()
                    if (result.size() + count > limit) {
                        LOG.info("Zstandard documentation frame at $offset exceeds the output limit of $limit bytes")
                        return InflateResult.CapExceeded
                    }
                    output.flip()
                    output.get(bytes, 0, count)
                    result.write(bytes, 0, count)
                    if (complete) return InflateResult.Complete(result.toByteArray(), position - input.remaining() - offset)
                    if (stopAtLimit && result.size() >= limit) return InflateResult.Partial(result.toByteArray())
                    if (count == 0 && input.position() == previousPosition) {
                        LOG.info("Zstandard documentation frame at $offset is incomplete or made no progress")
                        return InflateResult.Rejected
                    }
                }
            }
        }
        catch (e: LinkageError) {
            LOG.warn("Cannot load the zstd documentation method at $offset", e)
            return InflateResult.Rejected
        }
        catch (e: ZstdException) {
            LOG.debug("Cannot decode the Zstandard documentation frame at $offset: ${e.message}")
            return InflateResult.Rejected
        }
        catch (e: IOException) {
            LOG.info("Cannot read the Zstandard documentation frame at $offset", e)
            throw e
        }
    }
}
