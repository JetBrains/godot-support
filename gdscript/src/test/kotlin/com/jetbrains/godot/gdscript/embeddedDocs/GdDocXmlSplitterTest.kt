package com.jetbrains.godot.gdscript.embeddedDocs

import gdscript.embeddedDocs.GdDocXmlSplitter
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.nio.file.Files
import java.util.concurrent.atomic.AtomicInteger

@RunWith(JUnit4::class)
class GdDocXmlSplitterTest {
    @Test
    fun testSplitsTwoClasses() {
        val node = document("Node", "A node.")
        val resource = document("Resource", "A resource.")
        val result = GdDocXmlSplitter.split(node + resource)

        assertEquals(listOf("Node", "Resource"), result.documents.map { it.className })
        assertEquals(listOf(0, node.size), result.documents.map { it.blobOffset })
        assertArrayEquals(node, result.documents[0].content)
        assertArrayEquals(resource, result.documents[1].content)
        assertEquals(0, result.conflicts.size)
        assertEquals(0, result.malformedDocuments.size)
    }

    @Test
    fun testAcceptsXmlDeclarationVariants() {
        val first = "<?xml version='1.0'?>\n<class name=\"Node\"></class>\n".toByteArray()
        val second = "<?xml  version = \"1.0\" encoding = 'UTF-8' ?>\n<class name=\"Resource\"></class>\n".toByteArray()

        val result = GdDocXmlSplitter.split(first + second)

        assertEquals(listOf("Node", "Resource"), result.documents.map { it.className })
        assertEquals(listOf(0, first.size), result.documents.map { it.blobOffset })
        assertEquals(0, result.malformedDocuments.size)
    }

    @Test
    fun doesNotSplitSingleItem() {
        val content =
            declaration + "<class name=\"Node\"><description>Before <?xml-note value?> after.</description></class>\n".toByteArray()

        val result = GdDocXmlSplitter.split(content)

        assertEquals(listOf("Node"), result.documents.map { it.className })
        assertEquals(0, result.malformedDocuments.size)
    }

    @Test
    fun testDeduplicatesExactClassAcrossSlices() {
        val node = document("Node", "A node.")
        val result = GdDocXmlSplitter.split(node + node)

        assertEquals(1, result.documents.size)
        assertEquals("Node", result.documents.single().className)
        assertEquals(0, result.conflicts.size)
        assertEquals(0, result.malformedDocuments.size)
    }

    @Test
    fun testRecordsSameNameContentConflict() {
        val first = document("Node", "First.")
        val second = document("Node", "Second.")
        val result = GdDocXmlSplitter.split(first + second)

        assertEquals(1, result.documents.size)
        assertArrayEquals(first, result.documents.single().content)
        assertEquals(1, result.conflicts.size)
        val conflict = result.conflicts.single()
        assertEquals("Node", conflict.className)
        assertEquals(result.documents.single().contentHash, conflict.firstContentHash)
        assertNotEquals(conflict.firstContentHash, conflict.conflictingContentHash)
        assertEquals(first.size, conflict.conflictingOffset)
        assertEquals(0, result.malformedDocuments.size)
    }

    @Test
    fun testReportsMalformedDocumentAndKeepsValidDocuments() {
        val first = document("Node", "First.")
        val malformed = declaration + "<class name=\"Broken\"><description></class>\n".toByteArray()
        val last = document("Resource", "Last.")
        val result = GdDocXmlSplitter.split(first + malformed + last)

        assertEquals(listOf("Node", "Resource"), result.documents.map { it.className })
        assertEquals(listOf(0, first.size + malformed.size), result.documents.map { it.blobOffset })
        assertEquals(1, result.malformedDocuments.size)
        assertEquals(first.size, result.malformedDocuments.single().blobOffset)
        assertEquals(0, result.conflicts.size)
    }

    @Test
    fun testAcceptsDoctype() {
        val content = declaration + "<!DOCTYPE class [<!ENTITY value 'text'>]>\n".toByteArray() +
            "<class name=\"Node\"><description>&value;</description></class>\n".toByteArray()

        val result = GdDocXmlSplitter.split(content)

        assertEquals(listOf("Node"), result.documents.map { it.className })
        assertEquals(0, result.malformedDocuments.size)
    }

    @Test
    fun testDoesNotReadSystemEntity() {
        val secret = Files.createTempFile("gd-doc-secret", ".txt")
        try {
            Files.writeString(secret, "local secret")
            val content = declaration + "<!DOCTYPE class [<!ENTITY value SYSTEM '${secret.toUri()}'>]>\n".toByteArray() +
                "<class name=\"Node\"><description>&value;</description></class>\n".toByteArray()

            val result = GdDocXmlSplitter.split(content)

            assertEquals(listOf("Node"), result.documents.map { it.className })
            assertEquals(0, result.malformedDocuments.size)
            assertFalse(result.documents.single().content.decodeToString().contains("local secret"))
        } finally {
            Files.deleteIfExists(secret)
        }
    }

    @Test
    fun testRethrowsCancellationBetweenDocuments() {
        val cancellation = IllegalStateException("cancel")
        val checks = AtomicInteger()
        try {
            GdDocXmlSplitter.split(document("Node", "First.") + document("Resource", "Second.")) {
                if (checks.incrementAndGet() == 2) throw cancellation
            }
            throw AssertionError("The splitter did not rethrow the cancellation.")
        } catch (e: IllegalStateException) {
            assertEquals(2, checks.get())
            assertEquals(cancellation, e)
        }
    }

    private fun document(name: String, description: String): ByteArray =
        declaration + "<class name=\"$name\"><description>$description</description></class>\n".toByteArray()

    private val declaration = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n".toByteArray()
}
