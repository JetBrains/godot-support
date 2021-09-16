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
            is GdMethodDeclTl -> methodDecl();
            is GdClassVarDeclTl -> classVarDecl();
            is GdConstDeclTl -> constDecl();
            is GdConstDeclSt -> constDeclSt();
            is GdVarDeclSt -> varDeclSt();
            else -> null;
        } ?: return;

        val currentOffset = caret.offset;
        caret.moveToOffset(typed.first);
        EditorModificationUtil.insertStringAtCaret(editor, typed.second)
        PsiDocumentManager.getInstance(project).commitDocument(editor.document)
        caret.moveToOffset(currentOffset + typed.second.length);
    }

    private fun methodDecl(): Pair<Int, String>? {
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

    private fun classVarDecl(): Pair<Int, String>? {
        val el = element as GdClassVarDeclTl;
        val offset = el.classVarIdNmi?.endOffset ?: return null;

        return Pair(offset, ": $desired");
    }

    private fun constDecl(): Pair<Int, String>? {
        val el = element as GdConstDeclTl;
        val offset = el.constIdNmi?.endOffset ?: return null;

        return Pair(offset, ": $desired");
    }

    private fun varDeclSt(): Pair<Int, String> {
        val el = element as GdVarDeclSt;
        val offset = el.varNmi.endOffset;

        return Pair(offset, ": $desired");
    }

    private fun constDeclSt(): Pair<Int, String> {
        val el = element as GdConstDeclSt;
        val offset = el.varNmi.endOffset;

        return Pair(offset, ": $desired");
    }

}