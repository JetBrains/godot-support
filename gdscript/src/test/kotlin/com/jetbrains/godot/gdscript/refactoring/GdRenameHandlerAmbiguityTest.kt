package com.jetbrains.godot.gdscript.refactoring

import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.openapi.editor.ex.util.EditorUtil
import com.intellij.refactoring.rename.RenamerFactory
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * RIDER-142152: Shift+F6 on a GDScript declaration must offer exactly one "Rename" choice, not a
 * chooser between the classic PSI rename and the PolySymbol-based rename.
 */
@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Rename"])
@TestApplication
class GdRenameHandlerAmbiguityTest {
    private val gd: GdFixture by gdLightFixture("gdscript/refactoring")

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
        gd.fixture.configureByText("test.gd", fileText)

        val dataContext = EditorUtil.getEditorDataContext(gd.fixture.editor)
        val renamers = timeoutRunBlocking {
            withContext(Dispatchers.EDT) {
                RenamerFactory.EP_NAME.extensionList.flatMap { it.createRenamers(dataContext) }
            }
        }

        runReadActionBlocking {
            assertEquals(
                1, renamers.size,
                "expected exactly one rename option, got: ${renamers.map { it.presentableText }}",
            )
        }
    }
}
