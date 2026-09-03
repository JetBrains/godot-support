package com.jetbrains.godot.tscn.refactoring.rename

import com.intellij.ide.DataManager
import com.intellij.polySymbols.testFramework.renameSymbolAtCaret
import com.intellij.refactoring.rename.Renamer
import com.intellij.refactoring.rename.RenamerFactory
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import kotlin.io.path.pathString

/**
 * The rename of a GDScript method that a `.tscn` connection calls. The `to` node of the connection
 * is the root, and its script declares the handler.
 *
 * Each test starts the rename from one side of the link. `ConnectionMethodInplaceRenamingTest` runs
 * the same case through the live template of the editor.
 */
class ConnectionMethodRenamingTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/tscn/refactoring/rename/connectionMethodRenaming").pathString
    }

    /** The caret sits on the declaration, and the scene value follows. */
    fun testRename() {
        myFixture.configureByFiles("scene_a.gd", "scene_a.tscn")
        myFixture.renameSymbolAtCaret(NEW_NAME)
        checkResult()
    }

    /**
     * The classic rename of the declaration reaches the scene too. It finds the value through
     * `gdscript.search.GdOwnReferencesSearcher` and writes the new name with
     * `tscn.psi.manipulator.TscnElementManipulator`, which keeps the quotes of the value.
     */
    fun testClassicRename() {
        myFixture.configureByFiles("scene_a.gd", "scene_a.tscn")
        myFixture.renameElementAtCaret(NEW_NAME)
        checkResult()
    }

    /**
     * The `method` value of a connection has one rename target, the GDScript declaration. The value
     * element was a `PsiNamedElement`, so it offered a second target of its own - see
     * `tscn.psi.TscnNamedElement`.
     */
    fun testSingleRenamerInScene() {
        myFixture.configureByFiles("scene_a.gd", "scene_a.tscn")
        caretInSceneMethodValue()
        val dataContext = DataManager.getInstance().getDataContext(myFixture.editor.contentComponent)
        val renamers: MutableList<Renamer> = mutableListOf()
        RenamerFactory.EP_NAME.forEachExtensionSafe { renamers.addAll(it.createRenamers(dataContext)) }
        assertEquals(renamers.map { it.presentableText }.toString(), 1, renamers.size)
    }

    /** The caret sits on the scene value, and the one target renames the GDScript declaration. */
    fun testRenameFromScene() {
        myFixture.configureByFiles("scene_a.gd", "scene_a.tscn")
        caretInSceneMethodValue()
        myFixture.renameSymbolAtCaret(NEW_NAME)
        checkResult()
    }

    /** Opens the scene in the editor and puts the caret in the `method` value of the connection. */
    private fun caretInSceneMethodValue() {
        myFixture.openFileInEditor(myFixture.findFileInTempDir("scene_a.tscn"))
        val offset = myFixture.editor.document.text.indexOf(OLD_NAME)
        assertTrue("The scene declares no `$OLD_NAME` connection.", offset >= 0)
        myFixture.editor.caretModel.moveToOffset(offset + 1)
    }

    private fun checkResult() {
        myFixture.checkResultByFile("scene_a.gd", "scene_a.gd.after", false)
        myFixture.checkResultByFile("scene_a.tscn", "scene_a.tscn.after", false)
    }

    private companion object {
        const val OLD_NAME = "_on_goto_scene_pressed"
        const val NEW_NAME = "_on_goto_scene_b_pressed"
    }
}
