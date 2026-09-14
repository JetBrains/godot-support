package gdscript.embeddedDocs

import com.intellij.openapi.progress.ProgressManager
import org.w3c.dom.Element
import org.xml.sax.ErrorHandler
import org.xml.sax.SAXParseException
import java.io.ByteArrayInputStream
import java.security.MessageDigest
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Splits a byte blob that contains consecutive class XML documents.
 * The result lists accepted documents, conflicting class definitions, and malformed documents.
 * Each accepted document carries its class name, exact bytes, content hash, and blob offset.
 * A caller can process the accepted documents and report the other two lists as partial-recovery diagnostics.
 */
object GdDocXmlSplitter {
    private val xmlDeclaration = Regex(
        """<\?xml[\t\r\n ]+version[\t\r\n ]*=[\t\r\n ]*(?:\"1\.[0-9]+\"|'1\.[0-9]+')""" +
            """(?:[\t\r\n ]+encoding[\t\r\n ]*=[\t\r\n ]*(?:\"[A-Za-z][A-Za-z0-9._-]*\"|'[A-Za-z][A-Za-z0-9._-]*'))?""" +
            """(?:[\t\r\n ]+standalone[\t\r\n ]*=[\t\r\n ]*(?:\"(?:yes|no)\"|'(?:yes|no)'))?[\t\r\n ]*\?>"""
    )
    private val closingClass = "</class>".toByteArray(Charsets.US_ASCII)
    private val xmlPrefix = "<?xml".toByteArray(Charsets.US_ASCII)

    data class ClassDocument(
        val className: String,
        val content: ByteArray,
        val contentHash: String,
        val blobOffset: Int,
    )

    data class Conflict(
        val className: String,
        val firstContentHash: String,
        val conflictingContentHash: String,
        val conflictingOffset: Int,
    )

    data class MalformedDocument(
        val blobOffset: Int,
        val message: String,
    )

    data class Result(
        val documents: List<ClassDocument>,
        val conflicts: List<Conflict>,
        val malformedDocuments: List<MalformedDocument>,
    )

    // The blob contains separate XML documents, so one XML parser cannot read the whole blob.
    // Byte boundaries isolate each document before DOM parsing. This permits per-class recovery and preserves exact bytes for hashes and duplicate detection.
    fun split(blob: ByteArray, checkCanceled: () -> Unit = { ProgressManager.checkCanceled() }): Result {
        val starts = findDocumentStarts(blob)
        if (starts.isEmpty()) {
            return Result(emptyList(), emptyList(), listOf(MalformedDocument(0, "The blob has no XML document boundary.")))
        }

        val documents = mutableListOf<ClassDocument>()
        val conflicts = mutableListOf<Conflict>()
        val malformed = mutableListOf<MalformedDocument>()
        val firstByName = mutableMapOf<String, ClassDocument>()
        val knownNameAndHash = mutableSetOf<Pair<String, String>>()
        val factory = newHardenedDocumentBuilderFactory()

        for (index in starts.indices) {
            checkCanceled()
            val start = starts[index]
            val end = if (index + 1 < starts.size) starts[index + 1] else blob.size
            val content = blob.copyOfRange(start, end)
            val root = parseRoot(factory, content, start, malformed) ?: continue
            if (root.tagName != "class") {
                malformed.add(MalformedDocument(start, "The XML root element is not class."))
                continue
            }
            val className = root.getAttribute("name")
            if (className.isEmpty()) {
                malformed.add(MalformedDocument(start, "The class element has no name attribute."))
                continue
            }

            val hash = content.sha256()
            val key = className to hash
            if (!knownNameAndHash.add(key)) continue

            val first = firstByName[className]
            if (first != null) {
                conflicts.add(Conflict(className, first.contentHash, hash, start))
                continue
            }

            val document = ClassDocument(className, content, hash, start)
            firstByName[className] = document
            documents.add(document)
        }
        return Result(documents, conflicts, malformed)
    }

    private fun findDocumentStarts(blob: ByteArray): List<Int> {
        val starts = mutableListOf<Int>()
        for (declarationStart in 0..blob.size - xmlPrefix.size) {
            if (blob.startsWith(xmlPrefix, declarationStart) &&
                isDocumentBoundary(blob, declarationStart) &&
                hasValidXmlDeclaration(blob, declarationStart)
            ) {
                starts.add(declarationStart)
            }
        }
        return starts
    }

    private fun isDocumentBoundary(blob: ByteArray, declarationStart: Int): Boolean {
        if (declarationStart == 0) return true
        if (blob[declarationStart - 1] != '\n'.code.toByte()) return false
        var closingEnd = declarationStart - 1
        if (closingEnd > 0 && blob[closingEnd - 1] == '\r'.code.toByte()) closingEnd--
        val closingStart = closingEnd - closingClass.size
        return blob.startsWith(closingClass, closingStart)
    }

    private fun hasValidXmlDeclaration(blob: ByteArray, declarationStart: Int): Boolean {
        val declarationEnd = blob.indexOf(xmlDeclarationEnd, declarationStart + xmlPrefix.size)
        if (declarationEnd < 0) return false
        val declaration = blob.copyOfRange(declarationStart, declarationEnd + xmlDeclarationEnd.size).toString(Charsets.US_ASCII)
        return xmlDeclaration.matches(declaration)
    }

    private fun parseRoot(
        factory: DocumentBuilderFactory,
        content: ByteArray,
        offset: Int,
        malformed: MutableList<MalformedDocument>,
    ): Element? {
        try {
            val builder = factory.newDocumentBuilder()
            builder.setErrorHandler(object : ErrorHandler {
                override fun warning(exception: SAXParseException) = Unit
                override fun error(exception: SAXParseException) = Unit
                override fun fatalError(exception: SAXParseException) = Unit
            })
            return ByteArrayInputStream(content).use { builder.parse(it).documentElement }
        } catch (e: Exception) {
            malformed.add(MalformedDocument(offset, e.message ?: "The XML document is malformed."))
            return null
        }
    }

    private fun ByteArray.sha256(): String =
        MessageDigest.getInstance("SHA-256").digest(this).joinToString("") { "%02x".format(it) }

    private fun ByteArray.startsWith(pattern: ByteArray, offset: Int): Boolean {
        if (offset < 0 || offset + pattern.size > size) return false
        return pattern.indices.all { this[offset + it] == pattern[it] }
    }

    private fun ByteArray.indexOf(pattern: ByteArray, start: Int): Int {
        for (offset in start..size - pattern.size) {
            if (startsWith(pattern, offset)) return offset
        }
        return -1
    }

    private val xmlDeclarationEnd = "?>".toByteArray(Charsets.US_ASCII)
}
