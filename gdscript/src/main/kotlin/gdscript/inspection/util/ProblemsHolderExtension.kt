package gdscript.inspection.util

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemHighlightType.ERROR
import com.intellij.codeInspection.ProblemHighlightType.GENERIC_ERROR
import com.intellij.codeInspection.ProblemHighlightType.LIKE_UNUSED_SYMBOL
import com.intellij.codeInspection.ProblemHighlightType.POSSIBLE_PROBLEM
import com.intellij.codeInspection.ProblemHighlightType.WEAK_WARNING
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.codeInspection.util.InspectionMessage
import com.intellij.psi.PsiElement
import gdscript.annotator.isGodotSupportInstalled
import gdscript.utils.RiderGodotSupportPluginUtil
import org.jetbrains.annotations.Nls

object ProblemsHolderExtension {

    fun ProblemsHolder.registerWeakWarning(element: PsiElement, @Nls description: String, quickFix: LocalQuickFix? = null) {
        this.registerProblem(element, description, WEAK_WARNING, quickFix)
    }

    fun ProblemsHolder.registerUnused(element: PsiElement, @Nls description: String, quickFix: LocalQuickFix? = null) {
        this.registerProblem(element, description, LIKE_UNUSED_SYMBOL, quickFix)
    }

    fun ProblemsHolder.registerError(element: PsiElement, @Nls description: String, quickFix: LocalQuickFix? = null) {
        this.registerProblem(element, description, ERROR, quickFix)
    }

    fun ProblemsHolder.registerGenericError(element: PsiElement, @Nls description: String, quickFix: LocalQuickFix? = null) {
        this.registerProblem(element, description, GENERIC_ERROR, quickFix)
    }

    private fun ProblemsHolder.registerProblem(element: PsiElement, @InspectionMessage description: String, highlightType: ProblemHighlightType, quickFix: LocalQuickFix?) {
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