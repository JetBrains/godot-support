package com.jetbrains.godot.gdscript.refactoring

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdTestCase
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@TestApplication
class GdRefactoringTest : GdTestCase("refactoring") {

    // ─────────────────────────── Find Usages ──────────────────────────

     @Test
     @Disabled("restore it later, when polysymbols test in sdk is stabile")
     fun testFindUsagesOfMethod() = gd.doFindUsagesTest()

    // `new()` call sites resolve to `_init` only through PolySymbols own-references, never through
    // a classic name-text match ("new" != "_init") - doFindUsagesTest()'s shared test helper prefers
    // a symbol-based search that is purely name-text-driven and so can never see them, regardless of
    // GdConstructorReferencesSearcher below. Real Find Usages still shows `new()` sites because it
    // also runs classic-reference-based searchers, so assert against that lower-level API directly.
    @Test
    fun testFindUsagesOfConstructor() = gd.doConfiguredTest(dirName = "findUsagesOfConstructor") {
        val references = runReadActionBlocking { ReferencesSearch.search(elementAtCaret).findAll() }
        assertThat(references).hasSize(1)
        val reference = references.single()
        assertEquals("new", reference.element.text)
    }

    // ─────────────────────────── Rename ────────────────────────────────

    @Test
    fun testRenameProperty() = gd.doSymbolRenameTest("new_prop")
}
