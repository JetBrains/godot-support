package com.jetbrains.godot.gdscript.refactoring

import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdVarDeclSt
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdReferencesSearchDirectTest : BasePlatformTestCase() {

    @Test
    fun testDirectSearchOfMethodDeclaration() {
        myFixture.configureByText(
            "test.gd", """
            func used_method():
                pass

            func caller():
                used_method()
        """.trimIndent()
        )
        val methodDecl = PsiTreeUtil.findChildOfType(myFixture.file, GdMethodDeclTl::class.java)!!
        val nmi = methodDecl.methodIdNmi!!
        val references = ReferencesSearch.search(nmi, GlobalSearchScope.allScope(project)).findAll()
        assertEquals("direct search for method usages: $references", 1, references.size)
    }

    @Test
    fun testDirectSearchOfLocalVariable() {
        myFixture.configureByText(
            "test2.gd", """
            func caller():
                var used_var: int = 1
                print(used_var)
        """.trimIndent()
        )
        val varDecl = PsiTreeUtil.findChildOfType(myFixture.file, GdVarDeclSt::class.java)!!
        val nmi = varDecl.varNmi!!
        val references = ReferencesSearch.search(nmi, GlobalSearchScope.allScope(project)).findAll()
        assertEquals("direct search for local variable usages: $references", 1, references.size)
    }
}
