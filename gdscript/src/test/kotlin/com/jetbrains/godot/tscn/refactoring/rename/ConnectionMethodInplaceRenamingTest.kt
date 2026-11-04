package com.jetbrains.godot.tscn.refactoring.rename

import com.intellij.codeInsight.template.impl.TemplateManagerImpl
import com.intellij.ide.DataManager
import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.impl.TestOnlyThreading
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiDocumentManager
import com.intellij.refactoring.rename.Renamer
import com.intellij.refactoring.rename.RenamerFactory
import com.intellij.testFramework.PlatformTestUtil
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
 * The in-place rename of a GDScript method, the way the IDE runs it from the editor.
 *
 * `com.intellij.polySymbols.testFramework.renameSymbolAtCaret` calls the rename target directly, so
 * it skips the live template. The template deletes the text of every usage and puts it back before
 * it starts the real rename, so it needs a target pointer that survives the delete - see
 * `gdscript.polySymbols.psi.gdPsiSymbolPointer`.
 *
 * The other tests of this package pass even when the template path fails. So add an in-place test
 * here for a new rename case.
 */
@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["Scenes and resources/Rename"])
@TestApplication
class ConnectionMethodInplaceRenamingTest {

    private val gd: GdFixture by gdLightFixture("tscn/refactoring/rename/connectionMethodRenaming")

    @Test
    fun testInplaceRename() = timeoutRunBlocking {
        gd.fixture.configureByFiles("scene_a.gd", "scene_a.tscn")
        TemplateManagerImpl.setTemplateTesting(gd.testRootDisposable)

        // The whole in-place rename interaction - starting the template and pumping the event queue
        // until the rename coroutine finishes - runs on the EDT under the write-intent lock.
        withContext(Dispatchers.EDT) {
            val dataContext = DataManager.getInstance().getDataContext(gd.fixture.editor.contentComponent)
            val renamers: MutableList<Renamer> = mutableListOf()
            RenamerFactory.EP_NAME.forEachExtensionSafe { renamers.addAll(it.createRenamers(dataContext)) }
            assertEquals(1, renamers.size, renamers.map { it.presentableText }.toString())
            renamers.single().performRename()

            finishTemplate("_on_goto_scene_b_pressed")
            awaitRename("_on_goto_scene_pressed")
        }

        gd.fixture.checkResultByFile("scene_a.gd", "scene_a.gd.after", false)
        gd.fixture.checkResultByFile("scene_a.tscn", "scene_a.tscn.after", false)
    }

    /** Types [newName] into the template of the in-place rename and finishes it. */
    private fun finishTemplate(newName: String) {
        val state = TemplateManagerImpl.getTemplateState(gd.fixture.editor)
                    ?: throw AssertionError("The in-place rename started no template.")
        val range = state.currentVariableRange
                    ?: throw AssertionError("The template has no current variable.")
        WriteCommandAction.runWriteCommandAction(gd.project) {
            gd.fixture.editor.document.replaceString(range.startOffset, range.endOffset, newName)
        }
        state.gotoEnd(false)
    }

    /**
     * Waits while the file of the editor still holds [oldName]. The rename runs in a coroutine, so
     * this pumps the event queue the way `com.intellij.refactoring.rename.impl.renameAndWait` does.
     */
    private fun awaitRename(oldName: String) {
        val deadline = System.currentTimeMillis() + 30_000
        while (System.currentTimeMillis() < deadline && gd.fixture.editor.document.text.contains(oldName)) {
            TestOnlyThreading.releaseTheAcquiredWriteIntentLockThenExecuteActionAndTakeWriteIntentLockBack {
                PlatformTestUtil.dispatchAllInvocationEventsInIdeEventQueue()
                Thread.sleep(10)
            }
        }
        PsiDocumentManager.getInstance(gd.project).commitAllDocuments()
    }
}
