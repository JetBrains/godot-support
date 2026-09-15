package com.jetbrains.godot.gdscript.embeddedDocs

import com.intellij.openapi.application.PathManager
import gdscript.embeddedDocs.GdDocXmlMerger
import gdscript.embeddedDocs.GdDocXmlSplitter
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.nio.file.Files
import java.nio.file.Path

/**
 * Tests the fixture merge used when GdLibraryManager publishes GDExtension documentation.
 *
 * The golden hash cannot pin this behaviour. The merger does not run on the core write path,
 * and a hash over a self-merge stays equal even when the merger copies nothing.
 */
@RunWith(JUnit4::class)
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

        assertTrue(merged, merged.contains("Base class for all scene objects."))
        assertTrue(merged, merged.contains("Nodes are Godot's building blocks."))
        assertTrue(merged, merged.contains("Adds a child [param node]."))
        assertTrue(merged, merged.contains("The name of the node."))
        assertTrue(merged, merged.contains("Inherits the process mode from the parent node."))
        assertTrue(merged, merged.contains("Nodes and scenes"))
        assertTrue(merged, merged.contains("keywords=\"entity, actor\""))
    }

    @Test
    fun testKeepsTheTargetStructureAndAddsNoMember() {
        val merged = GdDocXmlMerger.merge(target, fixtureDocument("Node")) { }

        assertEquals("The target keeps one method.", 1, merged.split("<method ").size - 1)
        assertEquals("The target keeps one member.", 1, merged.split("<member ").size - 1)
        assertTrue("The source signal must stay out of the target.", !merged.contains("<signal"))
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
