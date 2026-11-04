package com.jetbrains.godot.gdscript.library

import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.GdKeywords
import gdscript.library.GdGlobalSingletonsDocWriter
import gdscript.library.GdSingletonInfo
import gdscript.polySymbols.sdk.xml.GdSdkXmlParser
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.file.Files
import kotlin.io.path.deleteIfExists
import kotlin.io.path.writeText

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/SDK"])
class GdGlobalSingletonsDocWriterTest {

    @Test
    fun testParseLineAcceptsSingletonLines() {
        val infoExpected = GdSingletonInfo(name = "Fusion", className = "FusionClass", apiType = 2)
        val infoParsed = GdSingletonInfo.parseLine("SINGLETON|Fusion|FusionClass|2")
        assertNotNull(infoParsed)
        assertEquals(infoExpected, infoParsed)
        assertTrue(infoParsed!!.isFromGdExtension)

        val core = GdSingletonInfo.parseLine("SINGLETON|IP|IPUnix|0")
        val coreExpected = GdSingletonInfo(name = "IP", className = "IPUnix", apiType = 0)
        assertNotNull(core)
        assertEquals(coreExpected, core)
        assertFalse(core!!.isFromGdExtension)
    }

    @Test
    fun testParseLineRejectsNoise() {
        assertNull(GdSingletonInfo.parseLine("Godot Engine v4.7.2.stable.mono - https://godotengine.org"))
        assertNull(GdSingletonInfo.parseLine("ERROR: Error loading extension: res://fusion.gdextension"))
        assertNull(GdSingletonInfo.parseLine("SINGLETON|Fusion"))
        assertNull(GdSingletonInfo.parseLine("SINGLETON||Fusion|2"))
        // An unparseable api type is tolerated: the singleton is still reported, just not as a GDExtension one.
        val unknownApi = GdSingletonInfo.parseLine("SINGLETON|Fusion|Fusion|whatever")
        assertNotNull(unknownApi)
        assertEquals(-1, unknownApi!!.apiType)
        assertFalse(unknownApi.isFromGdExtension)
    }

    @Test
    fun testGdExtensionScopeXmlContainsOnlyExtensionSingletons() {
        val xml = GdGlobalSingletonsDocWriter.buildGdExtensionScopeXml(
            listOf(
                GdSingletonInfo("Input", "Input", 0),
                GdSingletonInfo("EditorInterface", "EditorInterface", 1),
                GdSingletonInfo("Fusion", "Fusion", 2),
                GdSingletonInfo("EditorOnly", "EditorOnly", 3),
            )
        )

        assertTrue(xml.startsWith("<?xml "), "The generated document has no XML declaration:\n$xml")

        val path = Files.createTempFile("gd-gdextension-scope", ".xml")
        try {
            path.writeText(xml)
            val classData = GdSdkXmlParser.parseClass(path)
            assertNotNull(classData, "The generated document is not a parseable SDK class file:\n$xml")
            assertEquals(GdKeywords.GDEXTENSION_SCOPE, classData!!.name)

            val properties = classData.properties
            assertEquals(2, properties.size)
            assertEquals(listOf("EditorOnly", "Fusion"), properties.map { it.name })
            // Self-typed, exactly like @GlobalScope's members: this is what makes the type inferable.
            assertTrue(properties.all { it.name == it.type.name })
            assertTrue(classData.methods.isEmpty())
        } finally {
            path.deleteIfExists()
        }
    }

    @Test
    fun testGdExtensionScopeXmlWithoutExtensionSingletonsHasNoMembers() {
        val xml = GdGlobalSingletonsDocWriter.buildGdExtensionScopeXml(listOf(GdSingletonInfo("Input", "Input", 0)))

        val path = Files.createTempFile("gd-gdextension-scope", ".xml")
        try {
            path.writeText(xml)
            val classData = GdSdkXmlParser.parseClass(path)
            assertNotNull(classData)
            assertTrue(classData!!.properties.isEmpty())
        } finally {
            path.deleteIfExists()
        }
    }
}
