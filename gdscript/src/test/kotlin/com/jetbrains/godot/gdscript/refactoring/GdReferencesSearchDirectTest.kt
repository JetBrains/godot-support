package com.jetbrains.godot.gdscript.refactoring

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdVarDeclSt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Find usages"])
@TestApplication
class GdReferencesSearchDirectTest {
    private val gd: GdFixture by gdLightFixture("gdscript/refactoring")

    @Test
    fun testDirectSearchOfMethodDeclaration() {
        gd.fixture.configureByText(
            "test.gd", """
            func used_method():
                pass

            func caller():
                used_method()
        """.trimIndent()
        )
        val references = runReadActionBlocking {
            val methodDecl = PsiTreeUtil.findChildOfType(gd.fixture.file, GdMethodDeclTl::class.java)!!
            val nmi = methodDecl.methodIdNmi!!
            ReferencesSearch.search(nmi, GlobalSearchScope.allScope(gd.project)).findAll()
        }
        assertEquals(1, references.size, "direct search for method usages: $references")
    }

    @Test
    fun testDirectSearchOfLocalVariable() {
        gd.fixture.configureByText(
            "test2.gd", """
            func caller():
                var used_var: int = 1
                print(used_var)
        """.trimIndent()
        )
        val references = runReadActionBlocking {
            val varDecl = PsiTreeUtil.findChildOfType(gd.fixture.file, GdVarDeclSt::class.java)!!
            val nmi = varDecl.varNmi!!
            ReferencesSearch.search(nmi, GlobalSearchScope.allScope(gd.project)).findAll()
        }
        assertEquals(1, references.size, "direct search for local variable usages: $references")
    }
}
