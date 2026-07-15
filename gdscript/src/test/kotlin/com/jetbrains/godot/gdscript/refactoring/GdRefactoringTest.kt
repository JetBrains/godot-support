package com.jetbrains.godot.gdscript.refactoring

import com.intellij.psi.search.searches.ReferencesSearch
import com.jetbrains.godot.gdscript.GdTestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdRefactoringTest : GdTestCase("refactoring") {

    // ─────────────────────────── Find Usages ──────────────────────────

    @Test
    fun testFindUsagesOfMethod() = doFindUsagesTest()

    // `new()` call sites resolve to `_init` only through PolySymbols own-references, never through
    // a classic name-text match ("new" != "_init") - doFindUsagesTest()'s shared test helper prefers
    // a symbol-based search that is purely name-text-driven and so can never see them, regardless of
    // GdConstructorReferencesSearcher below. Real Find Usages still shows `new()` sites because it
    // also runs classic-reference-based searchers, so assert against that lower-level API directly.
    @Test
    fun testFindUsagesOfConstructor() = doConfiguredTest(dirName = "findUsagesOfConstructor") {
        val references = ReferencesSearch.search(elementAtCaret).findAll()
        val reference = assertOneElement(references.toList())
        assertEquals("new", reference.element.text)
    }

    // ─────────────────────────── Rename ────────────────────────────────

    @Test
    fun testRenameProperty() = doSymbolRenameTest("new_prop")
}
