package gdscript.psi

import com.intellij.lang.Language
import com.intellij.lang.refactoring.InlineActionHandler
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.application.runWriteActionAndWait
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootModificationUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.childLeafs
import com.intellij.psi.util.elementType
import gdscript.GdLanguage
import gdscript.utils.PsiReferenceUtil

class GdInlineVariableActionHandler : InlineActionHandler() {
    override fun isEnabledForLanguage(l: Language?): Boolean {
        return l == GdLanguage
    }

    override fun canInlineElement(element: PsiElement?): Boolean {
        return element is GdVarNmi
    }

    override fun inlineElement(project: Project?, editor: Editor?, element: PsiElement?) {
        if (editor == null || project == null || element !is GdVarNmi) return

        val replacementExpr: GdExpr = when (val parent = element.parent) {
            is GdVarDeclSt -> parent.expr
            is GdClassVarDeclTl -> parent.expr
            is GdConstDeclSt -> parent.expr
            is GdConstDeclTl -> parent.expr
            else -> null
        } ?: return

        val usages = ReferencesSearch.search(element).findAll()

//        val containingMethod = PsiTreeUtil.getParentOfType(expr, GdMethodDeclTl::class.java) ?: return
//        val callExpressions = PsiTreeUtil.findChildrenOfType(containingMethod, GdCallEx::class.java) ?: return
//
//        val usages = callExpressions.flatMap { it.childLeafs }
//            .filter { it.elementType is GdTokenType }
//            .filter { it.text == element.text }

//        val replacementExpr = PsiTreeUtil.findChildOfType(varDecl, GdExpr::class.java) ?: return

        // TODO some UI for selection wich references should be replaces?
        WriteCommandAction.writeCommandAction(project).run<Exception> {
            usages.forEach {
                it.element.replace(replacementExpr)
            }
        }
    }

}
