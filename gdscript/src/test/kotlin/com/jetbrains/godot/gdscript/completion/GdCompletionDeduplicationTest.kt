package com.jetbrains.godot.gdscript.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProcess
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.psi.util.PsiUtilCore
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.completion.GdCompletionDeduplicatingContributor
import gdscript.completion.GdCompletionSource
import gdscript.lsp.GodotLspCompletionSupport
import org.eclipse.lsp4j.CompletionItem
import org.eclipse.lsp4j.CompletionItemKind
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["LSP functionality/Completion deduplication"])
@TestApplication
class GdCompletionDeduplicationTest {

    private val gd: GdFixture by gdLightFixture("gdscript/completion")

    private val customizer = GodotLspCompletionSupport()

    @Test
    fun testNormalizeKey_removesEmptyParens() {
        assertEquals("test", GdCompletionDeduplicatingContributor.normalizeKey("test()"))
    }

    @Test
    fun testNormalizeKey_removesEllipsisParens() {
        assertEquals("new", GdCompletionDeduplicatingContributor.normalizeKey("new(…)"))
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
        gd.fixture.configureByText("Test.gd", "")
        val params = makeParams(0)

        val item = CompletionItem("test()").apply {
            kind = CompletionItemKind.Function
            insertText = "test()"
        }

        val lookup = customizer.createLookupElement(params, item)
        assertNotNull(lookup, "LSP method lookup should be created")
        // The lookup string is normalized by GodotLspCompletionSupport
        assertEquals("test", lookup?.lookupString)
    }

    @Test
    fun testLspConstructorWithEllipsis() {
        gd.fixture.configureByText("Test.gd", "")
        val params = makeParams(0)

        // Godot LSP sends label = "new(…)", insertText = "new("
        val item = CompletionItem("new(…)").apply {
            kind = CompletionItemKind.Method
            insertText = "new("
        }

        val lookup = customizer.createLookupElement(params, item)
        assertNotNull(lookup, "LSP constructor lookup should be created")

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
        assertTrue(GdCompletionDeduplicatingContributor.shouldKeep(seenKeys, GdCompletionSource.LSP, "new(…)"))
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
        val position = PsiUtilCore.getElementAtOffset(gd.fixture.file, caretOffset)
        return CompletionParameters(
            position,
            gd.fixture.file,
            CompletionType.BASIC,
            caretOffset,
            1,
            gd.fixture.editor,
            object : CompletionProcess {
                override fun isAutopopupCompletion(): Boolean = false
            },
        )
    }
}
