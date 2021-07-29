package gdscript.action

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.EditorModificationUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.endOffset

class GdCreateMethodAction : BaseIntentionAction {

    val name: String;
    val returnType: String?;
    val parameters: Array<String>;
    val bodyLines: Array<String>;

    constructor(
        name: String,
        returnType: String? = "void",
        parameters: Array<String> = emptyArray(),
        bodyLines: Array<String> = arrayOf("pass"),
    ) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = parameters;
        this.bodyLines = bodyLines;
    }

    override fun getText(): String {
        return "Create method";
    }

    override fun getFamilyName(): String {
        return "Create method";
    }

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        return true;
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        if (file === null || editor === null) {
            return;
        }

        val after = PsiTreeUtil.getDeepestVisibleLast(file) ?: return;
        editor.caretModel.moveToOffset(after.endOffset);

        val method = StringBuilder("\n\n\n");
        method.append("func $name(${parameters.joinToString()})");
        if (returnType != null) {
            method.append(" -> $returnType")
        }
        method.appendLine(":")
        val indent = "    "; // TODO read from settings;
        bodyLines.forEach {
            method.append("$indent$it");
        }

        EditorModificationUtil.insertStringAtCaret(editor, method.toString());
        PsiDocumentManager.getInstance(project).commitDocument(editor.document);
    }

}
