package gdscript.action.quickFix

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.refactoring.rename.RenameProcessor
import gdscript.psi.GdClassNameNmi

class GdFileClassNameAction : BaseIntentionAction {

    val filename: String
    val element: GdClassNameNmi

    constructor(
        filename: String,
        element: GdClassNameNmi,
    ) {
        this.filename = filename
        this.element = element
    }

    override fun getText(): String = GdScriptBundle.message("intention.name.rename.class.to.match.filename")

    override fun getFamilyName(): String = "Rename class"

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        return true
    }

    override fun startInWriteAction(): Boolean {
        return false
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        RenameProcessor( project, element, filename, false, false).run()
    }
}
