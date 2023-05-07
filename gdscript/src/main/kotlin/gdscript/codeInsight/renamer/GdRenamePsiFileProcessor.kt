package gdscript.codeInsight.renamer

import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.search.searches.ReferencesSearch
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.RefactoringSettings
import com.intellij.refactoring.rename.RenamePsiFileProcessor
import gdscript.psi.GdClassNaming
import gdscript.psi.GdFile
import gdscript.utils.StringUtil.snakeToPascalCase

/**
 * Renames class_name & type when renaming file
 */
class GdRenamePsiFileProcessor : RenamePsiFileProcessor() {

    override fun canProcessElement(element: PsiElement): Boolean {
        return element is GdFile
    }

    override fun prepareRenaming(element: PsiElement, newName: String, allRenames: MutableMap<PsiElement, String>) {
        if (RefactoringSettings.getInstance().RENAME_SEARCH_FOR_REFERENCES_FOR_FILE) {
            val newClassName = newName.substringBefore(".").snakeToPascalCase()
            PsiTreeUtil.getStubChildOfType(element, GdClassNaming::class.java)?.classNameNmi?.let { nmi ->
                allRenames[nmi] = newClassName
                ReferencesSearch.search(nmi, GlobalSearchScope.allScope(element.project)).forEach { typeRef ->
                    typeRef.resolve()?.let { type -> allRenames[type] = newClassName }
                }
            }
        }

        super.prepareRenaming(element, newName, allRenames)
    }

}
