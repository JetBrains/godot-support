package com.jetbrains.godot.gdscript.embeddedDocs

import com.intellij.openapi.application.PathManager
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.embeddedDocs.GdDocXmlMerger
import gdscript.embeddedDocs.GdDocXmlSplitter
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path

/**
 * Tests the fixture merge used when GdLibraryManager publishes GDExtension documentation.
 *
 * The golden hash cannot pin this behaviour. The merger does not run on the core write path,
 * and a hash over a self-merge stays equal even when the merger copies nothing.
 */
@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Documentation"])
class GdDocXmlMergerFixtureTest {

    private val target = """
        <?xml version="1.0" encoding="UTF-8" ?>
        <class name="Node" inherits="Object">
        <brief_description></brief_description>
        <description></description>
        <tutorials></tutorials>
        <methods><method name="add_child"><return type="void" /><param index="0" name="node" type="Node" /><param index="1" name="force_readable_name" type="bool" /><description></description></method></methods>
        <members><member name="name" type="StringName" setter="set_name" getter="get_name"></member></members>
        <constants><constant name="PROCESS_MODE_INHERIT" value="0" enum="ProcessMode"></constant></constants>
        </class>
    """.trimIndent()

    @Test
    fun testCopiesTheProseOfTheExtractedFixtureOntoTheTargetStructure() {
        val merged = GdDocXmlMerger.merge(target, fixtureDocument("Node")) { }

        assertTrue(merged.contains("Base class for all scene objects."), merged)
        assertTrue(merged.contains("Nodes are Godot's building blocks."), merged)
        assertTrue(merged.contains("Adds a child [param node]."), merged)
        assertTrue(merged.contains("The name of the node."), merged)
        assertTrue(merged.contains("Inherits the process mode from the parent node."), merged)
        assertTrue(merged.contains("Nodes and scenes"), merged)
        assertTrue(merged.contains("keywords=\"entity, actor\""), merged)
    }

    @Test
    fun testKeepsTheTargetStructureAndAddsNoMember() {
        val merged = GdDocXmlMerger.merge(target, fixtureDocument("Node")) { }

        assertEquals(1, merged.split("<method ").size - 1, "The target keeps one method.")
        assertEquals(1, merged.split("<member ").size - 1, "The target keeps one member.")
        assertTrue(!merged.contains("<signal"), "The source signal must stay out of the target.")
    }

    private fun fixtureDocument(className: String): String {
        val blob = Files.readAllBytes(testDataPath("gdscript/embeddedDocs/coreDocsBlob.xml"))
        val document = GdDocXmlSplitter.split(blob) { }.documents.single { it.className == className }
        return document.content.decodeToString()
    }

    private fun testDataPath(relativePath: String): Path {
        val home = PathManager.getHomeDirFor(javaClass)
        if (home != null) return home.resolve("dotnet/Plugins/godot-support/gdscript/src/test/testData").resolve(relativePath)
        return PathManager.getPluginsDir().parent.parent.parent.parent.parent.resolve("src/test/testData").resolve(relativePath)
    }
}
