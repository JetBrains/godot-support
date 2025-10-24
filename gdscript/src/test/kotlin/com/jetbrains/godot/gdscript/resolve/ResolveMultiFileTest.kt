package com.jetbrains.godot.gdscript.resolve

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

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
    fun testEnums() {
        val files = loadFilesFromSubdirAsProjectRoot()
        val annotated = dumpResolvesForFiles(files)
        assertGold(annotated)
    }
}
