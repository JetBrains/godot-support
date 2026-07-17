package com.jetbrains.godot.gdscript.refactoring

import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import gdscript.psi.GdClassNaming
import gdscript.psi.GdFile
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdRenamePsiFileProcessorSearchTest : BasePlatformTestCase() {

    @Test
    fun testReferencesSearchFindsExtendsAndTypeHintUsagesOfClassName() {
        myFixture.addFileToProject(
            "base.gd", """
            class_name Base
        """.trimIndent()
        )
        myFixture.configureByText(
            "user.gd", """
            class Sub extends Base:
                var x: Base
        """.trimIndent()
        )
        val baseFile = myFixture.findFileInTempDir("base.gd")
        val basePsiFile = com.intellij.psi.PsiManager.getInstance(project).findFile(baseFile) as GdFile
        val classNameNmi = PsiTreeUtil.getStubChildOfType(basePsiFile, GdClassNaming::class.java)?.classNameNmi
        assertNotNull("expected a class_name declaration in base.gd", classNameNmi)

        val references = ReferencesSearch.search(classNameNmi!!, GlobalSearchScope.allScope(project)).findAll()
        assertEquals("expected references from both 'extends Base' and 'var x: Base': $references", 2, references.size)
    }
}
