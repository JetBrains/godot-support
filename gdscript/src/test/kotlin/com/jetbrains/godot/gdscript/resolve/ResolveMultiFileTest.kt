package com.jetbrains.godot.gdscript.resolve

import org.junit.jupiter.api.Test

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
}
