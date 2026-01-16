package gdscript.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.search.SearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import gdscript.inspection.fixes.GdAddUnderscoreFix
import gdscript.inspection.fixes.GdRemoveElementFix
import gdscript.inspection.util.ProblemsHolderExtension.registerUnused
import org.jetbrains.annotations.Nls

abstract class GdUnusedInspection : LocalInspectionTool() {

    protected abstract val description: String

    @Nls
    protected abstract fun removeText(@Nls symbol: String): String

    fun registerUnused(element: PsiElement, nmi: PsiNameIdentifierOwner, holder: ProblemsHolder) {
        holder.registerUnused(
            nmi,
            description,
            GdRemoveElementFix(element, removeText(nmi.name ?: "")),
        )
    }

    fun registerUnusedWithUnderscoreFix(element: PsiElement, nmi: PsiNameIdentifierOwner, holder: ProblemsHolder) {
        holder.registerUnused(
            nmi,
            description,
            GdAddUnderscoreFix(element),
        )
    }

    fun anyReference(nmi: PsiElement, searchScope: SearchScope): Boolean {
        return ReferencesSearch.search(nmi, searchScope).any()
    }

}
