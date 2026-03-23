package com.jetbrains.godot.gdscript.polySymbols.refactoring

import com.jetbrains.godot.gdscript.polySymbols.GdPolySymbolsTestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdPolySymbolRefactoringTest : GdPolySymbolsTestCase("refactoring") {

    // ─────────────────────────── Find Usages ──────────────────────────

    @Test
    fun testFindUsagesOfMethod() = doFindUsagesTest()

    // ─────────────────────────── Rename ────────────────────────────────

    @Test
    fun testRenameProperty() = doSymbolRenameTest("new_prop")
}
