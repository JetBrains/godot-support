package gdscript.embeddedDocs

import gdscript.embeddedDocs.GdEmbeddedDocExtractor.InflateResult
import java.nio.channels.FileChannel

internal interface GdBinaryDocMethod {
    val name: String
    /** The length of the format header in bytes. */
    val headerSize: Int

    /** Matches the format header in the last scanned bytes. */
    fun isHeader(window: Int): Boolean
    fun probe(channel: FileChannel, offset: Long, checkCanceled: () -> Unit): Boolean
    fun inflate(channel: FileChannel, offset: Long, limit: Int, checkCanceled: () -> Unit): InflateResult
}
