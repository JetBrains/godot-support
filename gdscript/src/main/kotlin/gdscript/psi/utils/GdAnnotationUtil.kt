package gdscript.psi.utils

import com.intellij.psi.PsiElement
import gdscript.psi.GdAnnotationTl
import gdscript.utils.PsiElementUtil.prevNonWhiteCommentToken

object GdAnnotationUtil {

    fun collectPreceding(element: PsiElement): List<GdAnnotationTl> {
        val annotations = mutableListOf<GdAnnotationTl>()

        var previous: PsiElement? = element
        while (previous != null) {
            previous = previous.prevNonWhiteCommentToken()
            if (previous is GdAnnotationTl) {
                annotations.add(previous)
            } else {
                break
            }
        }

        return annotations
    }

}
