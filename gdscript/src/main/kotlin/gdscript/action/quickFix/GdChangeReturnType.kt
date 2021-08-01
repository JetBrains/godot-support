package gdscript.action.quickFix

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.refactoring.suggested.startOffset
import gdscript.psi.GdReturnHint

class GdChangeReturnType : BaseIntentionAction {

    private val element: GdReturnHint;
    private val desired: String;

    constructor(element: GdReturnHint, desired: String) {
        this.element = element;
        this.desired = desired;
    }

    override fun getText(): String {
        return "Change return type to $desired";
    }

    override fun getFamilyName(): String {
        return "Change return type to $desired";
    }

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        return true;
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        if (editor == null || file == null) {
            return;
        }
        val caret = editor.caretModel;

        val hint = element.typeHintNm;
        if (hint != null) {
            caret.moveToOffset(hint.startOffset);
            hint.delete();
        } else {
            val void = element.lastChild;
            caret.moveToOffset(void.startOffset);
            if (void.text == "void") {
                void.delete();
            }
        }

        EditorModificationUtil.insertStringAtCaret(editor, " $desired")
        PsiDocumentManager.getInstance(project).commitDocument(editor.document)
    }
}