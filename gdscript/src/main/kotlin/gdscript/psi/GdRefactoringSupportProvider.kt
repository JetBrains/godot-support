package gdscript.psi

import com.intellij.lang.refactoring.RefactoringSupportProvider
import com.intellij.psi.PsiElement
import com.intellij.refactoring.RefactoringActionHandler
import gdscript.refactoring.introduce.GdIntroduceVariableHandler

class GdRefactoringSupportProvider : RefactoringSupportProvider() {

//    RIDER-134345 GDScript - inplace rename refactor doesn't work
//    override fun isMemberInplaceRenameAvailable(element: PsiElement, context: PsiElement?): Boolean {
//        return element.elementType is GdElementType
//    }

    override fun isInplaceIntroduceAvailable(element: PsiElement, context: PsiElement?): Boolean {
        return element is GdExpr
    }

    override fun getIntroduceVariableHandler(): RefactoringActionHandler {
        return GdIntroduceVariableHandler()
    }

}
