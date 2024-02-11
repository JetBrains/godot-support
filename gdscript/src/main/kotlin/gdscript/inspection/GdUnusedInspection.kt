package gdscript.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType.LIKE_UNUSED_SYMBOL
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import gdscript.inspection.quickFix.GdRemoveElementFix

abstract class GdUnusedInspection : LocalInspectionTool() {

    protected abstract val description: String
    protected abstract val text: String

    fun registerUnused(element: PsiElement, nmi: PsiNameIdentifierOwner, holder: ProblemsHolder) {
        holder.registerProblem(
                nmi,
                description,
                LIKE_UNUSED_SYMBOL,
                GdRemoveElementFix(element, text.replace("{NAME}", nmi.name ?: "")))
    }

    fun anyReference(nmi: PsiNameIdentifierOwner, searchScope: SearchScope): Boolean {
        return ReferencesSearch.search(nmi, searchScope).any();
    }
}
