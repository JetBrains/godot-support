package com.jetbrains.godot.gdscript.xml

import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.polySymbols.sdk.xml.GdSdkXmlParser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.writeText

/**
 * Tests that the parser separates a source it cannot read from a document it cannot understand.
 *
 * A caller must retry after a read failure, and it must not retry after a malformed document.
 */
@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/SDK"])
class GdSdkXmlParseResultTest {

    @Test
    fun testReportsAReadFailureForAMissingFile() {
        val missing = Files.createTempFile("gd-parse-missing", ".xml")
        Files.delete(missing)

        assertSame(GdSdkXmlParser.ParseResult.ReadFailure, GdSdkXmlParser.parseClassResult(missing))
        assertNull(GdSdkXmlParser.parseClass(missing))
    }

    @Test
    fun testReportsAMalformedDocumentForBrokenXml() {
        val file = xmlFile("<class name=\"Node\"")

        assertSame(GdSdkXmlParser.ParseResult.Malformed, GdSdkXmlParser.parseClassResult(file))
        assertNull(GdSdkXmlParser.parseClass(file))
    }

    @Test
    fun testReportsTheParsedClassForAGoodDocument() {
        val file = xmlFile("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<class name=\"Node\" inherits=\"Object\"></class>")

        val result = GdSdkXmlParser.parseClassResult(file)

        val parsed = result as GdSdkXmlParser.ParseResult.Parsed
        assertEquals("Node", parsed.value.name)
        assertEquals("Node", GdSdkXmlParser.parseClass(file)?.name)
        assertEquals("Node", result.valueOrNull()?.name)
    }

    @Test
    fun testReportsAMalformedDocumentWhenTheOperationsSectionIsAbsent() {
        val file = xmlFile("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n<class name=\"Node\"></class>")

        assertSame(GdSdkXmlParser.ParseResult.Malformed, GdSdkXmlParser.parseOperationsResult(file))
    }

    @Test
    fun testReportsAReadFailureForAMissingAnnotationsFile() {
        val missing = Files.createTempFile("gd-parse-annotations", ".xml")
        Files.delete(missing)

        assertSame(GdSdkXmlParser.ParseResult.ReadFailure, GdSdkXmlParser.parseAnnotationsResult(missing))
    }

    private fun xmlFile(text: String): Path =
        Files.createTempFile("gd-parse", ".xml").also { it.writeText(text) }
}
