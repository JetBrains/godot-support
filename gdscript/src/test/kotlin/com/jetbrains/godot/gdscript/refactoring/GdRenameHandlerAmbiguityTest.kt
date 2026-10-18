package com.jetbrains.godot.gdscript.refactoring

import com.intellij.openapi.editor.ex.util.EditorUtil
import com.intellij.refactoring.rename.RenamerFactory
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * RIDER-142152: Shift+F6 on a GDScript declaration must offer exactly one "Rename" choice, not a
 * chooser between the classic PSI rename and the PolySymbol-based rename.
 */
@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Rename"])
@RunWith(JUnit4::class)
class GdRenameHandlerAmbiguityTest : BasePlatformTestCase() {

    @Test
    fun testRenamingClassPropertyOffersExactlyOneChoice() {
        assertSingleRenamer(
            """
            var _loaded_r<caret>d_path: String = ""
            """.trimIndent()
        )
    }

    @Test
    fun testRenamingMethodOffersExactlyOneChoice() {
        assertSingleRenamer(
            """
            func _load_r<caret>d(): pass
            """.trimIndent()
        )
    }

    @Test
    fun testRenamingClassNameOffersExactlyOneChoice() {
        assertSingleRenamer(
            """
            class_name Load<caret>edResource
            """.trimIndent()
        )
    }

    private fun assertSingleRenamer(fileText: String) {
        myFixture.configureByText("test.gd", fileText)

        val dataContext = EditorUtil.getEditorDataContext(myFixture.editor)
        val renamers = RenamerFactory.EP_NAME.extensionList.flatMap { it.createRenamers(dataContext) }

        assertEquals(
            "expected exactly one rename option, got: ${renamers.map { it.presentableText }}",
            1,
            renamers.size,
        )
    }
}
