package com.jetbrains.godot.gdscript.embeddedDocs

import gdscript.embeddedDocs.newHardenedDocumentBuilderFactory
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import org.w3c.dom.Document
import org.xml.sax.ErrorHandler
import org.xml.sax.SAXParseException
import java.nio.file.Files
import java.nio.file.Path
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Tests the behaviour of the hardened XML parser factory against a default factory.
 *
 * The default factory is the positive control. Each test shows that the default factory accepts
 * the input, and that the hardened factory refuses it or ignores it. The control makes the test
 * fail if somebody removes the matching setting from GdXmlSafety.kt.
 */
class GdXmlSafetyTest {
    private val temporaryRoots = mutableListOf<Path>()

    @After
    fun removeTemporaryRoots() {
        for (root in temporaryRoots.asReversed()) {
            Files.walk(root).use { paths ->
                paths.sorted(Comparator.reverseOrder()).forEach { Files.deleteIfExists(it) }
            }
        }
    }

    @Test
    fun hardenedFactoryRefusesTooManyEntityExpansions() {
        // Entity references in an attribute value always expand. The expansion counter therefore runs.
        val document = buildString {
            append("<!DOCTYPE root [<!ENTITY a \"x\">]><root marker=\"")
            repeat(ENTITY_REFERENCE_COUNT) { append("&a;") }
            append("\"/>")
        }

        // The test relaxes the ambient JVM property to isolate the factory setting under test.
        // The default factory must accept the payload that the hardened factory refuses.
        withSystemProperty("jdk.xml.entityExpansionLimit", RELAXED_LIMIT) {
            val control = parse(DocumentBuilderFactory.newDefaultInstance(), document)
            assertEquals(ENTITY_REFERENCE_COUNT, control.documentElement.getAttribute("marker").length)

            assertRefusedByHardenedFactory(document)
        }
    }

    @Test
    fun hardenedFactoryDoesNotExpandEntityReferencesInContent() {
        // The hardened factory must still accept the document. It must not expand the payload.
        val document = buildString {
            append("<!DOCTYPE root [<!ENTITY a \"x\">]><root>")
            repeat(SAFE_ENTITY_REFERENCE_COUNT) { append("&a;") }
            append("</root>")
        }

        val control = parse(DocumentBuilderFactory.newDefaultInstance(), document)
        assertEquals(SAFE_ENTITY_REFERENCE_COUNT, control.documentElement.textContent.length)

        val hardened = parse(newHardenedDocumentBuilderFactory(), document)
        assertEquals(0, hardened.documentElement.textContent.length)
    }

    @Test
    fun hardenedFactoryRefusesDeeplyNestedElements() {
        val document = buildString {
            append("<root>")
            repeat(ELEMENT_NESTING_COUNT) { append("<n>") }
            repeat(ELEMENT_NESTING_COUNT) { append("</n>") }
            append("</root>")
        }

        // The test relaxes the ambient JVM property to isolate the factory setting under test.
        // The default factory must accept the payload that the hardened factory refuses.
        withSystemProperty("jdk.xml.maxElementDepth", RELAXED_LIMIT) {
            val control = parse(DocumentBuilderFactory.newDefaultInstance(), document)
            assertEquals("root", control.documentElement.nodeName)

            assertRefusedByHardenedFactory(document)
        }
    }

    @Test
    fun hardenedFactoryDoesNotResolveExternalParameterEntity() {
        // The external file supplies an attribute default. The parser must expand the parameter
        // entity to see that default, so the marker proves that the parser read the file.
        val declarations = writeMarkerFile("attribute-default.ent")
        val document = "<!DOCTYPE root [<!ENTITY % ext SYSTEM \"${declarations.toUri()}\"> %ext;]><root/>"

        val control = parse(DocumentBuilderFactory.newDefaultInstance(), document)
        assertEquals(MARKER, control.documentElement.getAttribute("marker"))

        // The hardened factory must accept the document, because Godot files declare a DOCTYPE.
        val hardened = parse(newHardenedDocumentBuilderFactory(), document)
        assertFalse(hardened.documentElement.hasAttribute("marker"))
    }

    @Test
    fun hardenedFactoryDoesNotResolveExternalDtdSubset() {
        val declarations = writeMarkerFile("external-subset.dtd")
        val document = "<!DOCTYPE root SYSTEM \"${declarations.toUri()}\"><root/>"

        val control = parse(DocumentBuilderFactory.newDefaultInstance(), document)
        assertEquals(MARKER, control.documentElement.getAttribute("marker"))

        val hardened = parse(newHardenedDocumentBuilderFactory(), document)
        assertFalse(hardened.documentElement.hasAttribute("marker"))
    }

    private fun <T> withSystemProperty(name: String, value: String, action: () -> T): T {
        val previous = System.getProperty(name)
        System.setProperty(name, value)
        try {
            return action()
        } finally {
            if (previous == null) System.clearProperty(name) else System.setProperty(name, previous)
        }
    }

    private fun assertRefusedByHardenedFactory(document: String) {
        try {
            parse(newHardenedDocumentBuilderFactory(), document)
        } catch (expected: SAXParseException) {
            return
        }
        throw AssertionError("The hardened factory accepted a document that it must refuse.")
    }

    private fun parse(factory: DocumentBuilderFactory, document: String): Document {
        val builder = factory.newDocumentBuilder()
        builder.setErrorHandler(FailFastErrorHandler)
        return document.byteInputStream().use { builder.parse(it) }
    }

    private fun writeMarkerFile(name: String): Path {
        val root = Files.createTempDirectory("gd-xml-safety")
        temporaryRoots.add(root)
        val file = root.resolve(name)
        Files.writeString(file, "<!ATTLIST root marker CDATA \"$MARKER\">\n")
        return file
    }

    private object FailFastErrorHandler : ErrorHandler {
        override fun warning(exception: SAXParseException) = Unit
        override fun error(exception: SAXParseException): Unit = throw exception
        override fun fatalError(exception: SAXParseException): Unit = throw exception
    }

    private companion object {
        /** This count is above the entity expansion limit in GdXmlSafety.kt. */
        const val ENTITY_REFERENCE_COUNT = 12_000

        /** This count is below the entity expansion limit in GdXmlSafety.kt. */
        const val SAFE_ENTITY_REFERENCE_COUNT = 100

        /** This nesting is above the element depth limit in GdXmlSafety.kt. */
        const val ELEMENT_NESTING_COUNT = 300

        /** A plain marker, not a secret. It only shows that the parser read a file. */
        const val MARKER = "GD-XML-SAFETY-MARKER"

        /** A JVM limit that is far above every test document. */
        const val RELAXED_LIMIT = "1000000"
    }
}
