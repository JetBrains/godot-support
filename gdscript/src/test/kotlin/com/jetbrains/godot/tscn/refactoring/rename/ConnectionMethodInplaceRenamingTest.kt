package com.jetbrains.godot.tscn.refactoring.rename

import com.intellij.codeInsight.template.impl.TemplateManagerImpl
import com.intellij.ide.DataManager
import com.intellij.openapi.application.impl.TestOnlyThreading
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiDocumentManager
import com.intellij.refactoring.rename.Renamer
import com.intellij.refactoring.rename.RenamerFactory
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import kotlin.io.path.pathString

/**
 * The in-place rename of a GDScript method, the way the IDE runs it from the editor.
 *
 * `com.intellij.polySymbols.testFramework.renameSymbolAtCaret` calls the rename target directly, so
 * it skips the live template. The template deletes the text of every usage and puts it back before
 * it starts the real rename, so it needs a target pointer that survives the delete - see
 * `gdscript.polySymbols.psi.gdPsiSymbolPointer`.
 */
class ConnectionMethodInplaceRenamingTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/tscn/refactoring/rename/connectionMethodRenaming").pathString
    }

    fun testInplaceRename() {
        myFixture.configureByFiles("scene_a.gd", "scene_a.tscn")
        TemplateManagerImpl.setTemplateTesting(testRootDisposable)

        val dataContext = DataManager.getInstance().getDataContext(myFixture.editor.contentComponent)
        val renamers: MutableList<Renamer> = mutableListOf()
        RenamerFactory.EP_NAME.forEachExtensionSafe { renamers.addAll(it.createRenamers(dataContext)) }
        assertEquals(renamers.map { it.presentableText }.toString(), 1, renamers.size)
        renamers.single().performRename()

        finishTemplate("_on_goto_scene_b_pressed")
        awaitRename("_on_goto_scene_pressed")

        myFixture.checkResultByFile("scene_a.gd", "scene_a.gd.after", false)
        myFixture.checkResultByFile("scene_a.tscn", "scene_a.tscn.after", false)
    }

    /** Types [newName] into the template of the in-place rename and finishes it. */
    private fun finishTemplate(newName: String) {
        val state = TemplateManagerImpl.getTemplateState(myFixture.editor)
                    ?: throw AssertionError("The in-place rename started no template.")
        val range = state.currentVariableRange
                    ?: throw AssertionError("The template has no current variable.")
        WriteCommandAction.runWriteCommandAction(project) {
            myFixture.editor.document.replaceString(range.startOffset, range.endOffset, newName)
        }
        state.gotoEnd(false)
    }

    /**
     * Waits while the file of the editor still holds [oldName]. The rename runs in a coroutine, so
     * this pumps the event queue the way `com.intellij.refactoring.rename.impl.renameAndWait` does.
     */
    private fun awaitRename(oldName: String) {
        val deadline = System.currentTimeMillis() + 30_000
        while (System.currentTimeMillis() < deadline && myFixture.editor.document.text.contains(oldName)) {
            TestOnlyThreading.releaseTheAcquiredWriteIntentLockThenExecuteActionAndTakeWriteIntentLockBack {
                PlatformTestUtil.dispatchAllInvocationEventsInIdeEventQueue()
                Thread.sleep(10)
            }
        }
        PsiDocumentManager.getInstance(project).commitAllDocuments()
    }
}
