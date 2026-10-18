package com.jetbrains.godot.gdscript.resolve

import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Resolve and navigation"])
@RunWith(JUnit4::class)
class ResolveMultiFileTest : ResolveTestBase() {
    @Test
    fun testMultiFileReference() {
        val files = loadFilesByTestName()
        val annotated = dumpResolvesForFiles(files)
        assertGold(annotated)
    }
    @Test
    fun testOuterStaticAccess() {
        val files = loadFilesByTestName()
        val annotated = dumpResolvesForFiles(files)
        assertGold(annotated)
    }

    @Test
    fun testGlobals() {
        val files = loadFilesFromSubdirAsProjectRoot()
        val annotated = dumpResolvesForFiles(files)
        assertGold(annotated)
    }

    @Test
    fun testTscnGlobals() {
        val files = loadFilesFromSubdirAsProjectRoot()
        val useFile = files.find { it.name == "use.gd" }!!
        val annotated = dumpResolvesWithInlineMarkers(useFile)
        assertGold(annotated)
    }

    @Test
    fun testEnums() {
        val files = loadFilesFromSubdirAsProjectRoot()
        val annotated = dumpResolvesForFiles(files)
        assertGold(annotated)
    }

    @Test
    fun testGdAutoloadUid() {
        val files = loadFilesFromSubdirAsProjectRoot()
        val useFile = files.find { it.name == "use.gd" }!!
        val annotated = dumpResolvesWithInlineMarkers(useFile)
        assertGold(annotated)
    }

    @Test
    fun testTscnAutoloadUid() {
        val files = loadFilesFromSubdirAsProjectRoot()
        val useFile = files.find { it.name == "new_gd_autoload.gd" }!!
        val annotated = dumpResolvesWithInlineMarkers(useFile)
        assertGold(annotated)
    }
}
