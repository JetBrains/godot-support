package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.TokenType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import gdscript.psi.*
import gdscript.utils.PsiElementUtil.prevNonWhiteCommentToken

object PsiGdClassVarUtil {

    fun getReturnType(element: GdClassVarDeclTl): String {
        if (element.typed !== null) {
            return PsiGdExprUtil.fromTyped(element.typed);
        }

        return element.expr?.returnType ?: "";
    }

    fun isAnnotated(element: GdClassVarDeclTl, annotator: String): Boolean {
        var previous: PsiElement? = element
        while (previous != null) {
            previous = previous.prevNonWhiteCommentToken()
            if (previous is GdAnnotationTl) {
                if (previous.annotationType.text == annotator) return true
            } else {
                break
            }
        }

        return false
    }

}
