package com.jetbrains.godot.gdscript.refactoring

import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.godot.gdscript.resolve.ResolveTestBase
import gdscript.psi.GdKeyNmi
import gdscript.psi.GdKeyValue

class GdDictionaryStringKeyTest : ResolveTestBase() {

    fun testFindUsagesOfStringKey() {
        val code = """
            |func f():
            |	var dict = {"key1": 1}
            |	var other = {"key1": 3}
            |	print(dict.key1)
            |	dict.key1 = 2
            |	print(other.key1)
        """.trimMargin()

        myFixture.configureByText("dict_find_usages.gd", code)

        val targetDict = PsiTreeUtil.findChildrenOfType(myFixture.file, GdKeyValue::class.java).firstOrNull()?.keyNmi
        assertNotNull(targetDict)

        val targetOther = PsiTreeUtil.findChildrenOfType(myFixture.file, GdKeyValue::class.java).lastOrNull()?.keyNmi
        assertNotNull(targetOther)

        // Only dict.key1 (read + write), other.key1 resolves to a different dictionary's key
        val usagesDict = myFixture.findUsages(targetDict!!)
        assertEquals("unexpected usages: ${usagesDict.map { it.element?.text }}", 2, usagesDict.size)

        val usagesOther = myFixture.findUsages(targetOther!!)
        assertEquals("unexpected usages: ${usagesOther.map { it.element?.text }}", 1, usagesOther.size)
    }

    fun testFindUsagesOfStringKeyFromDeclaration() {
        val code = """
            |func f():
            |	var dict = {"k<caret>ey1": 1}
            |	print(dict.key1)
            |	dict.key1 = 2
        """.trimMargin()

        myFixture.configureByText("dict_find_usages_decl.gd", code)

        val target = myFixture.elementAtCaret
        assertInstanceOf(target, GdKeyNmi::class.java)

        val usages = myFixture.findUsages(target)
        assertEquals("unexpected usages: ${usages.map { it.element?.text }}", 2, usages.size)
    }

    fun testRenameStringKeyFromUsage() {
        val code = """
            |func f():
            |	var dict = {"key1": 1}
            |	print(dict.k<caret>ey1)
            |	dict.key1 = 2
        """.trimMargin()

        myFixture.configureByText("dict_rename.gd", code)
        myFixture.renameElementAtCaret("renamed")

        assertRenamed()
    }

    fun testRenameStringKeyFromDeclaration() {
        val code = """
            |func f():
            |	var dict = {"k<caret>ey1": 1}
            |	print(dict.key1)
            |	dict.key1 = 2
        """.trimMargin()

        myFixture.configureByText("dict_rename_decl.gd", code)
        myFixture.renameElementAtCaret("renamed")

        assertRenamed()
    }

    private fun assertRenamed() {
        PsiDocumentManager.getInstance(project).commitAllDocuments()
        myFixture.doHighlighting()

        val updated = myFixture.file.text
        assertTrue("declaration literal not renamed:\n$updated", updated.contains("{\"renamed\": 1}"))
        assertTrue("read usage not renamed:\n$updated", updated.contains("print(dict.renamed)"))
        assertTrue("write usage not renamed:\n$updated", updated.contains("dict.renamed = 2"))
    }
}