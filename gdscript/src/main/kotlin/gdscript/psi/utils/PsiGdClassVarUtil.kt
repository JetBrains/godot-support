package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import gdscript.psi.GdAnnotationTl
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdTypes
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

    fun isStatic(element: GdClassVarDeclTl): Boolean {
        val stub = element.stub
        if (stub != null) return stub.isStatic()

        return element.firstChild.elementType == GdTypes.STATIC
    }

    fun isAnnotated(element: GdClassVarDeclTl): Boolean {
        var previous: PsiElement? = element
        while (previous != null) {
            previous = previous.prevNonWhiteCommentToken()
            return previous is GdAnnotationTl
        }

        return false
    }

    fun isAnnotatedContains(element: GdClassVarDeclTl, contains: String): Boolean {
        var previous: PsiElement? = element
        while (previous != null) {
            previous = previous.prevNonWhiteCommentToken()
            if (previous is GdAnnotationTl) {
                if (previous.annotationType.text.contains(contains)) return true
            } else {
                break
            }
        }

        return false
    }

}
