package gdscript.inspection.util

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemHighlightType.*
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import gdscript.annotator.isGodotSupportInstalled
import gdscript.utils.RiderGodotSupportPluginUtil

object ProblemsHolderExtension {

    fun ProblemsHolder.registerWeakWarning(element: PsiElement, description: String, quickFix: LocalQuickFix? = null) {
        this.registerProblem(element, description, WEAK_WARNING, quickFix)
    }

    fun ProblemsHolder.registerUnused(element: PsiElement, description: String, quickFix: LocalQuickFix? = null) {
        this.registerProblem(element, description, LIKE_UNUSED_SYMBOL, quickFix)
    }

    fun ProblemsHolder.registerError(element: PsiElement, description: String, quickFix: LocalQuickFix? = null) {
        this.registerProblem(element, description, ERROR, quickFix)
    }

    fun ProblemsHolder.registerGenericError(element: PsiElement, description: String, quickFix: LocalQuickFix? = null) {
        this.registerProblem(element, description, GENERIC_ERROR, quickFix)
    }

    private fun ProblemsHolder.registerProblem(element: PsiElement, description: String, highlightType: ProblemHighlightType, quickFix: LocalQuickFix?) {
        var type = highlightType
        if ((highlightType == GENERIC_ERROR || highlightType == ERROR) && isGodotSupportInstalled && RiderGodotSupportPluginUtil.isGodotSupportLSPRunning(project))
            type = POSSIBLE_PROBLEM

        if (quickFix == null) {
            this.registerProblem(element, description, type)
        } else {
            this.registerProblem(element, description, type, quickFix)
        }
    }
}