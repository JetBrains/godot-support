package gdscript.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.search.searches.ReferencesSearch
import gdscript.inspection.quickFix.GdRemoveElementFix
import gdscript.utils.ProjectUtil.globalSearchScope

abstract class GdUnusedInspection : LocalInspectionTool() {

    protected abstract val description: String
    protected abstract val text: String

    fun process(element: PsiElement?, nmi: PsiNameIdentifierOwner?, holder: ProblemsHolder) {
        if (element == null || nmi == null) return
        if (nmi.name?.startsWith("_") == true) return

        if (!ReferencesSearch.search(nmi, element.project.globalSearchScope()).any()) {
            holder.registerProblem(
                nmi,
                description,
                ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                GdRemoveElementFix(element, text.replace("{NAME}", nmi.name ?: "")),
            )
        }
    }

}
