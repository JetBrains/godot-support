package gdscript.codeInsight

import com.intellij.find.findUsages.FindUsagesHandler
import com.intellij.find.findUsages.FindUsagesHandlerFactory
import com.intellij.lang.findUsages.LanguageFindUsages
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileSystemItem

class GdFindUsageHandlerFactory : FindUsagesHandlerFactory() {

    override fun canFindUsages(element: PsiElement): Boolean {
        return if (!element.isValid) {
            false
        } else if (element is PsiFileSystemItem) {
            element.virtualFile != null
        } else {
            LanguageFindUsages.canFindUsagesFor(element)
        }
    }

    override fun createFindUsagesHandler(element: PsiElement, forHighlightUsages: Boolean): FindUsagesHandler {
        return GdFindUsageHandler(element)
    }
}