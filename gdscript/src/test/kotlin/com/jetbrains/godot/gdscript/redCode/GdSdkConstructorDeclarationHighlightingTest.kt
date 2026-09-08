package com.jetbrains.godot.gdscript.redCode

import com.intellij.openapi.actionSystem.IdeActions
import com.intellij.openapi.application.invokeAndWaitIfNeeded
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.jetbrains.godot.gdscript.GdTestCaseWithSdk

/**
 * Reproduces a bug where navigating (Go To Declaration) from a bare SDK constructor call, e.g.
 * `Vector2(1, 2)`, into the generated synthetic SDK file lands on the `_init` declaration - but
 * that declaration's symbol had a name ("Vector2", the declaring class name) whose length didn't
 * match the declaring `_init` element's own text length, making the highlighting annotator crash
 * with "Range must be inside element being annotated" while computing the declaration's highlight
 * range. Runs highlighting directly (not via a markup-comparison helper) since the synthetic file
 * has other, unrelated pre-existing warnings (e.g. `float`/`Vector2i` "Unrecognized name") that
 * aren't in scope here - only the absence of a crash is being verified.
 */
class GdSdkConstructorDeclarationHighlightingTest : GdTestCaseWithSdk("highlighting") {

    fun testNavigatingToSdkConstructorDoesNotBreakIdentifierHighlighting() {
        doEditorTypingTest(
            fileContents = "func f():\n\tvar v = Vect<caret>or2(1, 2)",
            dir = false,
            checkResult = false,
        ) {
            performEditorAction(IdeActions.ACTION_GOTO_DECLARATION)

            val targetEditor = FileEditorManager.getInstance(myFixture.project).selectedTextEditor
            val targetVirtualFile = targetEditor?.let { FileDocumentManager.getInstance().getFile(it.document) }
            assertEquals("Vector2.generated.gd", targetVirtualFile?.name)
            invokeAndWaitIfNeeded {
                myFixture.openFileInEditor(targetVirtualFile!!)
                assertEquals("Vector2.generated.gd", myFixture.file.name)
                myFixture.doHighlighting()
            }
        }
    }
}

