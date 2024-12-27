package gdscript.psi

import com.intellij.lang.Language
import com.intellij.lang.refactoring.InlineActionHandler
import com.intellij.openapi.application.WriteAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.childLeafs
import com.intellij.psi.util.elementType
import gdscript.GdLanguage

class GdInlineVariableActionHandler : InlineActionHandler() {
    override fun isEnabledForLanguage(l: Language?): Boolean {
        return l == GdLanguage
    }

    override fun canInlineElement(element: PsiElement?): Boolean {
        return element is GdVarNmi
    }

    override fun inlineElement(project: Project?, editor: Editor?, element: PsiElement?) {
        if (editor == null || project == null || element !is GdVarNmi) return

        // TODO error handling
        val varDecl = element.parent as? GdVarDeclSt ?: return
        val containingMethod = PsiTreeUtil.getParentOfType(varDecl, GdMethodDeclTl::class.java) ?: return
        val callExpressions = PsiTreeUtil.findChildrenOfType(containingMethod, GdCallEx::class.java) ?: return

        val usages = callExpressions.flatMap { it.childLeafs }
            .filter { it.elementType is GdTokenType }
            .filter { it.text == element.text }

        val replacementExpr = PsiTreeUtil.findChildOfType(varDecl, GdExpr::class.java) ?: return

        TODO("we need to replace the usages with the replacementExpr in a write action")
    }


}