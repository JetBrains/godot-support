package com.jetbrains.godot.gdscript.resolve

class ResolveMultiFileTest : ResolveTestBase() {
    fun testMultiFileReference() {
        val files = loadFilesByTestName()
        val annotated = dumpResolvesForFiles(files)
        assertGold(annotated)
    }

    fun testOuterStaticAccess() {
        val files = loadFilesByTestName()
        val annotated = dumpResolvesForFiles(files)
        assertGold(annotated)
    }
}
