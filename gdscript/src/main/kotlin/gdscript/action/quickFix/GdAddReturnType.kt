package gdscript.action.quickFix

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType
import com.intellij.refactoring.suggested.endOffset
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdTypes

class GdAddReturnType : BaseIntentionAction {

    private val element: GdMethodDeclTl;
    private val desired: String;

    override fun getFamilyName(): String = "Add return type";
    override fun getText(): String = "Specify return type [${desired}]";

    constructor(element: GdMethodDeclTl, desired: String) {
        this.element = element;
        this.desired = desired;
    }

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean = true;

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        if (editor == null || file == null) {
            return;
        }
        val caret = editor.caretModel;
        var child = element.firstChild;
        while (child != null) {
            if (child.elementType == GdTypes.COLON) {
                break
            }
            child = child.nextSibling;
        }

        if (child == null) {
            return;
        }

        caret.moveToOffset(child.textOffset);
        EditorModificationUtil.insertStringAtCaret(editor, " -> $desired")
        PsiDocumentManager.getInstance(project).commitDocument(editor.document)
    }
}