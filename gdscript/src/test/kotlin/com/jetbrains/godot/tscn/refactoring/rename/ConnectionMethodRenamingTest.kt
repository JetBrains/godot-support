package com.jetbrains.godot.tscn.refactoring.rename

import com.intellij.ide.DataManager
import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.polySymbols.testFramework.renameSymbolAtCaret
import com.intellij.refactoring.rename.Renamer
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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * The rename of a GDScript method that a `.tscn` connection calls. The `to` node of the connection
 * is the root, and its script declares the handler.
 *
 * Each test starts the rename from one side of the link. `ConnectionMethodInplaceRenamingTest` runs
 * the same case through the live template of the editor.
 */
@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["Scenes and resources/Rename"])
@TestApplication
class ConnectionMethodRenamingTest {
    private val gd: GdFixture by gdLightFixture("tscn/refactoring/rename/connectionMethodRenaming")

    /** The caret sits on the declaration, and the scene value follows. */
    @Test
    fun testRename() = timeoutRunBlocking {
        gd.fixture.configureByFiles("scene_a.gd", "scene_a.tscn")
        withContext(Dispatchers.EDT) { gd.fixture.renameSymbolAtCaret(NEW_NAME) }
        checkResult()
    }

    /**
     * The classic rename of the declaration reaches the scene too. It finds the value through
     * `gdscript.search.GdOwnReferencesSearcher` and writes the new name with
     * `tscn.psi.manipulator.TscnElementManipulator`, which keeps the quotes of the value.
     */
    @Test
    fun testClassicRename() = timeoutRunBlocking {
        gd.fixture.configureByFiles("scene_a.gd", "scene_a.tscn")
        withContext(Dispatchers.EDT) { gd.fixture.renameElementAtCaret(NEW_NAME) }
        checkResult()
    }

    /**
     * The `method` value of a connection has one rename target, the GDScript declaration. The value
     * element was a `PsiNamedElement`, so it offered a second target of its own - see
     * `tscn.psi.TscnNamedElement`.
     */
    @Test
    fun testSingleRenamerInScene() = timeoutRunBlocking {
        gd.fixture.configureByFiles("scene_a.gd", "scene_a.tscn")
        val renamers: MutableList<Renamer> = mutableListOf()
        withContext(Dispatchers.EDT) {
            caretInSceneMethodValue()
            val dataContext = DataManager.getInstance().getDataContext(gd.fixture.editor.contentComponent)
            RenamerFactory.EP_NAME.forEachExtensionSafe { renamers.addAll(it.createRenamers(dataContext)) }
        }
        runReadActionBlocking {
            assertEquals(1, renamers.size, renamers.map { it.presentableText }.toString())
        }
    }

    /** The caret sits on the scene value, and the one target renames the GDScript declaration. */
    @Test
    fun testRenameFromScene() = timeoutRunBlocking {
        gd.fixture.configureByFiles("scene_a.gd", "scene_a.tscn")
        withContext(Dispatchers.EDT) {
            caretInSceneMethodValue()
            gd.fixture.renameSymbolAtCaret(NEW_NAME)
        }
        checkResult()
    }

    /** Opens the scene in the editor and puts the caret in the `method` value of the connection. */
    private fun caretInSceneMethodValue() {
        gd.fixture.openFileInEditor(gd.fixture.findFileInTempDir("scene_a.tscn"))
        val offset = gd.fixture.editor.document.text.indexOf(OLD_NAME)
        assertTrue(offset >= 0, "The scene declares no `$OLD_NAME` connection.")
        gd.fixture.editor.caretModel.moveToOffset(offset + 1)
    }

    private fun checkResult() {
        gd.fixture.checkResultByFile("scene_a.gd", "scene_a.gd.after", false)
        gd.fixture.checkResultByFile("scene_a.tscn", "scene_a.tscn.after", false)
    }

    private companion object {
        const val OLD_NAME = "_on_goto_scene_pressed"
        const val NEW_NAME = "_on_goto_scene_b_pressed"
    }
}
