package com.jetbrains.godot.gdscript.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProcess
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.psi.util.PsiUtilCore
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import gdscript.completion.GdCompletionDeduplicatingContributor
import gdscript.completion.GdCompletionSource
import gdscript.lsp.GodotLspCompletionSupport
import org.eclipse.lsp4j.CompletionItem
import org.eclipse.lsp4j.CompletionItemKind
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdCompletionDeduplicationTest : BasePlatformTestCase() {

    private val customizer = GodotLspCompletionSupport()

    @Test
    fun testNormalizeKey_removesEmptyParens() {
        assertEquals("test", GdCompletionDeduplicatingContributor.normalizeKey("test()"))
    }

    @Test
    fun testNormalizeKey_removesEllipsisParens() {
        assertEquals("new", GdCompletionDeduplicatingContributor.normalizeKey("new(\u2026)"))
    }

    @Test
    fun testNormalizeKey_removesSingleOpenParen() {
        assertEquals("new", GdCompletionDeduplicatingContributor.normalizeKey("new("))
    }

    @Test
    fun testNormalizeKey_preservesPlainStrings() {
        assertEquals("variable", GdCompletionDeduplicatingContributor.normalizeKey("variable"))
        assertEquals("ENUM_VALUE", GdCompletionDeduplicatingContributor.normalizeKey("ENUM_VALUE"))
    }

    @Test
    fun testLspMethodWithEmptyParens() {
        myFixture.configureByText("Test.gd", "")
        val params = makeParams(0)

        val item = CompletionItem("test()").apply {
            kind = CompletionItemKind.Function
            insertText = "test()"
        }

        val lookup = customizer.createLookupElement(params, item)
        assertNotNull("LSP method lookup should be created", lookup)
        // The lookup string is normalized by GodotLspCompletionSupport
        assertEquals("test", lookup?.lookupString)
    }

    @Test
    fun testLspConstructorWithEllipsis() {
        myFixture.configureByText("Test.gd", "")
        val params = makeParams(0)

        // Godot LSP sends label = "new(…)", insertText = "new("
        val item = CompletionItem("new(\u2026)").apply {
            kind = CompletionItemKind.Method
            insertText = "new("
        }

        val lookup = customizer.createLookupElement(params, item)
        assertNotNull("LSP constructor lookup should be created", lookup)

        // The lookup string is normalized by GodotLspCompletionSupport
        assertEquals("new", lookup?.lookupString)
    }


    @Test
    fun testShouldKeep_dropsLspDuplicateOfAnAlreadySeenKey() {
        val seenKeys = mutableSetOf<String>()
        assertTrue(GdCompletionDeduplicatingContributor.shouldKeep(seenKeys, null, "test"))
        assertFalse(GdCompletionDeduplicatingContributor.shouldKeep(seenKeys, GdCompletionSource.LSP, "test()"))
    }

    @Test
    fun testShouldKeep_keepsLspItemWithAnUnseenKey() {
        val seenKeys = mutableSetOf<String>()
        assertTrue(GdCompletionDeduplicatingContributor.shouldKeep(seenKeys, null, "move_toward"))
        assertTrue(GdCompletionDeduplicatingContributor.shouldKeep(seenKeys, GdCompletionSource.LSP, "new(\u2026)"))
    }

    @Test
    fun testShouldKeep_neverDropsANonLspItemEvenOnAKeyCollision() {
        val seenKeys = mutableSetOf<String>()
        assertTrue(GdCompletionDeduplicatingContributor.shouldKeep(seenKeys, null, "test"))
        // Not expected in practice, but the contributor must never drop a non-LSP item. Better to see something is up, than swallow error.
        assertTrue(GdCompletionDeduplicatingContributor.shouldKeep(seenKeys, null, "test()"))
    }

    @Test
    fun testShouldKeep_treatsNullSourceAsNonLsp() {
        val seenKeys = mutableSetOf<String>()
        assertTrue(GdCompletionDeduplicatingContributor.shouldKeep(seenKeys, null, "test"))
        assertTrue(seenKeys.contains("test"))
    }

    private fun makeParams(caretOffset: Int): CompletionParameters {
        val position = PsiUtilCore.getElementAtOffset(myFixture.file, caretOffset)
        return CompletionParameters(
            position,
            myFixture.file,
            CompletionType.BASIC,
            caretOffset,
            1,
            myFixture.editor,
            object : CompletionProcess {
                override fun isAutopopupCompletion(): Boolean = false
            },
        )
    }
}
