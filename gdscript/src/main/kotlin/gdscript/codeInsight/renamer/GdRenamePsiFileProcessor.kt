package gdscript.codeInsight.renamer

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.search.SearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.rename.RenamePsiFileProcessor
import gdscript.psi.GdClassNaming
import gdscript.psi.GdFile
import gdscript.reference.GdClassNameReference

class GdRenamePsiFileProcessor : RenamePsiFileProcessor() {

    override fun canProcessElement(element: PsiElement): Boolean {
        return element is GdFile
    }

    override fun findReferences(
        element: PsiElement,
        searchScope: SearchScope,
        searchInCommentsAndStrings: Boolean
    ): MutableCollection<PsiReference> {
        PsiTreeUtil.getStubChildOfType(element, GdClassNaming::class.java)?.classNameNmi?.let {
            return mutableListOf(GdClassNameReference(it))
        }

        return mutableListOf()
    }

}
