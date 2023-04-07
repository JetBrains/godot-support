package gdscript.action.quickFix

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.elementType
import com.intellij.refactoring.suggested.endOffset
import gdscript.psi.*

class GdAddReturnType : BaseIntentionAction {

    private val element: PsiElement;
    private val desired: String;

    override fun getFamilyName(): String = "Add return type";
    override fun getText(): String = "Specify return type [$desired]";

    constructor(element: PsiElement, desired: String) {
        this.element = element;
        this.desired = desired;
    }

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean = true;

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        if (editor == null || file == null) {
            return;
        }
        val caret = editor.caretModel;

        val typed = when (element) {
            is GdMethodDeclTl -> methodDecl(element);
            is GdClassVarDeclTl -> classVarDecl(element);
            is GdConstDeclTl -> constDecl(element);
            is GdConstDeclSt -> constDeclSt(element);
            is GdVarDeclSt -> varDeclSt(element);
            else -> null;
        } ?: return;

        val baseOffset = caret.offset
        caret.moveToOffset(typed.first)
        EditorModificationUtil.insertStringAtCaret(editor, typed.second)
        PsiDocumentManager.getInstance(project).commitDocument(editor.document)
        caret.moveToOffset(baseOffset + typed.second.length)
    }

    // TODO tohle zobecnit také pro lambdu
    private fun methodDecl(element: GdMethodDeclTl): Pair<Int, String>? {
        var child = element.firstChild;
        while (child != null) {
            if (child.elementType == GdTypes.COLON) {
                break
            }
            child = child.nextSibling;
        }

        if (child == null) {
            return null;
        }

        return Pair(child.textOffset, " -> $desired");
    }

    private fun classVarDecl(element: GdClassVarDeclTl): Pair<Int, String>? {
        val offset = element.classVarIdNmi?.endOffset ?: return null;

        return Pair(offset, ": $desired");
    }

    private fun constDecl(element: GdConstDeclTl): Pair<Int, String>? {
        val offset = element.constIdNmi?.endOffset ?: return null;

        return Pair(offset, ": $desired");
    }

    private fun varDeclSt(element: GdVarDeclSt): Pair<Int, String> {
        val offset = element.varNmi.endOffset;

        return Pair(offset, ": $desired");
    }

    private fun constDeclSt(element: GdConstDeclSt): Pair<Int, String> {
        val offset = element.varNmi?.endOffset ?: 0;

        return Pair(offset, ": $desired");
    }

}
