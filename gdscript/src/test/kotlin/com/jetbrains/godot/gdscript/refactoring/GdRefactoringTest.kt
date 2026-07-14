package com.jetbrains.godot.gdscript.refactoring

import com.jetbrains.godot.gdscript.GdTestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdRefactoringTest : GdTestCase("refactoring") {

    // ─────────────────────────── Find Usages ──────────────────────────

    @Test
    fun testFindUsagesOfMethod() = doFindUsagesTest()

    // ─────────────────────────── Rename ────────────────────────────────

    @Test
    fun testRenameProperty() = doSymbolRenameTest("new_prop")
}
