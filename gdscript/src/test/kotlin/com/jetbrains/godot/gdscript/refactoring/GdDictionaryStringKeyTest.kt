package com.jetbrains.godot.gdscript.refactoring

import com.intellij.openapi.application.EDT
import com.intellij.polySymbols.testFramework.renameSymbolAtCaret
import com.intellij.polySymbols.testFramework.symbolAtCaret
import com.intellij.polySymbols.testFramework.usagesAtCaret
import com.intellij.polySymbols.testFramework.usagesAtOffsetBySignature
import com.intellij.psi.PsiDocumentManager
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdResolveFixture
import com.jetbrains.godot.gdscript.gdResolveFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.polySymbols.psi.GdPsiDictKeySymbol
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@TestApplication
class GdDictionaryStringKeyTest {
    private val gd: GdResolveFixture by gdResolveFixture("gdscript/resolve")

    @ChecklistItems(["GDScript/Find usages"])
    @Test
    fun testFindUsagesOfStringKey() {
        val code = """
            |func f():
            |	var dict = {"key1": 1}
            |	var other = {"key1": 3}
            |	print(dict.key1)
            |	dict.key1 = 2
            |	print(other.key1)
        """.trimMargin()

        gd.fixture.configureByText("dict_find_usages.gd", code)

        // Only dict.key1 (read + write), other.key1 resolves to a different dictionary's key
        val usagesDict = timeoutRunBlocking {
            withContext(Dispatchers.EDT) { gd.fixture.usagesAtOffsetBySignature("var dict = {\"<caret>key1\": 1}") }
        }
        assertEquals(2, usagesDict.size, "unexpected usages: $usagesDict")

        val usagesOther = timeoutRunBlocking {
            withContext(Dispatchers.EDT) { gd.fixture.usagesAtOffsetBySignature("var other = {\"<caret>key1\": 3}") }
        }
        assertEquals(1, usagesOther.size, "unexpected usages: $usagesOther")
    }

    @ChecklistItems(["GDScript/Find usages"])
    @Test
    fun testFindUsagesOfStringKeyFromDeclaration() {
        val code = """
            |func f():
            |	var dict = {"k<caret>ey1": 1}
            |	print(dict.key1)
            |	dict.key1 = 2
        """.trimMargin()

        gd.fixture.configureByText("dict_find_usages_decl.gd", code)

        val symbol = timeoutRunBlocking {
            withContext(Dispatchers.EDT) { gd.fixture.symbolAtCaret(includePsiSymbols = true) }
        }
        assertInstanceOf(GdPsiDictKeySymbol::class.java, symbol)

        val usages = timeoutRunBlocking {
            withContext(Dispatchers.EDT) { gd.fixture.usagesAtCaret() }
        }
        assertEquals(2, usages.size, "unexpected usages: $usages")
    }

    @ChecklistItems(["GDScript/Rename"])
    @Test
    fun testRenameStringKeyFromUsage() {
        val code = """
            |func f():
            |	var dict = {"key1": 1}
            |	print(dict.k<caret>ey1)
            |	dict.key1 = 2
        """.trimMargin()

        gd.fixture.configureByText("dict_rename.gd", code)
        timeoutRunBlocking {
            withContext(Dispatchers.EDT) { gd.fixture.renameSymbolAtCaret("renamed") }
        }

        assertRenamed()
    }

    @ChecklistItems(["GDScript/Rename"])
    @Test
    fun testRenameStringKeyFromDeclaration() {
        val code = """
            |func f():
            |	var dict = {"k<caret>ey1": 1}
            |	print(dict.key1)
            |	dict.key1 = 2
        """.trimMargin()

        gd.fixture.configureByText("dict_rename_decl.gd", code)
        timeoutRunBlocking {
            withContext(Dispatchers.EDT) { gd.fixture.renameSymbolAtCaret("renamed") }
        }

        assertRenamed()
    }

    private fun assertRenamed() {
        timeoutRunBlocking {
            withContext(Dispatchers.EDT) { PsiDocumentManager.getInstance(gd.project).commitAllDocuments() }
        }
        gd.fixture.doHighlighting()

        val updated = gd.fixture.file.text
        assertTrue(updated.contains("{\"renamed\": 1}"), "declaration literal not renamed:\n$updated")
        assertTrue(updated.contains("print(dict.renamed)"), "read usage not renamed:\n$updated")
        assertTrue(updated.contains("dict.renamed = 2"), "write usage not renamed:\n$updated")
    }
}
