package com.jetbrains.godot.gdscript.refactoring

import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.psi.PsiManager
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.psi.GdClassNaming
import gdscript.psi.GdFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Rename"])
@TestApplication
class GdRenamePsiFileProcessorSearchTest {

    private val gd: GdFixture by gdLightFixture("gdscript/refactoring")

    @Test
    fun testReferencesSearchFindsExtendsAndTypeHintUsagesOfClassName() {
        gd.fixture.addFileToProject(
            "base.gd", """
            class_name Base
        """.trimIndent()
        )
        gd.fixture.configureByText(
            "user.gd", """
            class Sub extends Base:
                var x: Base
        """.trimIndent()
        )
        val baseFile = gd.fixture.findFileInTempDir("base.gd")
        val basePsiFile = runReadActionBlocking { PsiManager.getInstance(gd.project).findFile(baseFile) as GdFile }
        val classNameNmi = timeoutRunBlocking {
            withContext(Dispatchers.EDT) { PsiTreeUtil.getStubChildOfType(basePsiFile, GdClassNaming::class.java)?.classNameNmi }
        }
        assertNotNull(classNameNmi, "expected a class_name declaration in base.gd")

        val references = runReadActionBlocking { ReferencesSearch.search(classNameNmi!!, GlobalSearchScope.allScope(gd.project)).findAll() }
        assertEquals(2, references.size, "expected references from both 'extends Base' and 'var x: Base': $references")
    }
}
