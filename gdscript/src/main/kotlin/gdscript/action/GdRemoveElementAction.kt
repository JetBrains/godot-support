package gdscript.action

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdScriptBundle
import gdscript.psi.GdAnnotationTl

/**
 * Removes current element
 */
class GdRemoveElementAction : BaseIntentionAction() {

    override fun getText(): String {
        return GdScriptBundle.message("intention.name.remove.element")
    }

    override fun getFamilyName(): String {
        return GdScriptBundle.message("intention.name.remove.element")
    }

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean {
        if (editor === null || file === null) return false

        return getRemovableElement(editor, file) != null
    }

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        if (editor === null || file === null) return
        getRemovableElement(editor, file)?.delete()
    }

    private fun getRemovableElement(editor: Editor, file: PsiFile): PsiElement? {
        return PsiTreeUtil.getParentOfType(file.findElementAt(editor.caretModel.offset), GdAnnotationTl::class.java)
    }

}
