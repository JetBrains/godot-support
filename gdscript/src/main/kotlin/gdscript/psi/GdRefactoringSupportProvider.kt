package gdscript.psi

import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.psi.PsiElement

class GdRefactoringSupportProvider : RefactoringSupportProvider() {
    // TODO
    override fun isMemberInplaceRenameAvailable(elementToRename: PsiElement, context: PsiElement?): Boolean {
        val asd = elementToRename is GdClassNameNm;

        return (elementToRename is GdClassNameNm);
    }

}
