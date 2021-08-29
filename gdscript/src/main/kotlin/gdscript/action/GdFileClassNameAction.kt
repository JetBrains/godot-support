package gdscript.action

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import gdscript.psi.GdClassNameNm

class GdFileClassNameAction : BaseIntentionAction {

    val filename: String;
    val element: GdClassNameNm;

    constructor(
        filename: String,
        element: GdClassNameNm
    ) {
        this.filename = filename;
        this.element = element;
    }

    override fun getText(): String = "Rename class to match filename"

    override fun getFamilyName(): String = "Rename class"

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        return true;
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        element.name = filename;
    }
}