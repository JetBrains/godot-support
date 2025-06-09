package gdscript.refactoring.inline

import com.intellij.codeInsight.TargetElementUtil
import com.intellij.lang.Language
import com.intellij.lang.refactoring.InlineActionHandler
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.refactoring.util.CommonRefactoringUtil
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import gdscript.psi.*


class GdInlineVariableActionHandler : InlineActionHandler() {
    override fun isEnabledForLanguage(l: Language?): Boolean {
        return l == GdLanguage
    }

    override fun canInlineElement(element: PsiElement?): Boolean {
        return element is GdVarNmi
    }

    override fun inlineElement(project: Project?, editor: Editor?, element: PsiElement?) {
        if (editor == null || project == null || element !is GdVarNmi) return

        val replacementExpr: GdExpr? = when (val parent = element.parent) {
            is GdVarDeclSt -> parent.expr
            is GdClassVarDeclTl -> parent.expr
            is GdConstDeclSt -> parent.expr
            is GdConstDeclTl -> parent.expr
            else -> null
        }
        if (replacementExpr == null) {
            CommonRefactoringUtil.showErrorHint(
                project,
                editor,
                "Declaration lacks an expression",
                "Inline Variable",
                null
            )
            return
        }

        val occurrences = ReferencesSearch.search(element).count()
        if (occurrences <= 0) {
            CommonRefactoringUtil.showErrorHint(project, editor, "No usages", "Inline Variable", null)
            return
        }

        val reference = TargetElementUtil.findReference(editor, editor.caretModel.offset)
        val dialog = GdInlineVariableDialog(project, element, reference?.element)

        if (occurrences > 1) {
            dialog.show()
        } else {
            dialog.inlineAndRemove()
        }
    }

}
