package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import gdscript.GdKeywords
import gdscript.highlighter.GdHighlighterColors
import gdscript.psi.GdTypeHint
import gdscript.psi.GdTypeHintNm
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.reference.GdTypeHintNmReference
import gdscript.utils.PsiElementUtil.psi
import gdscript.utils.PsiFileUtil.isInSdk

/**
 * Checks that given return type is valid (built-in or Class)
 */
class GdTypeHintAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is GdTypeHint -> existingType(element, holder)
            is GdTypeHintNm -> colorType(element, holder)
        }
    }

    private fun existingType(element: GdTypeHint, holder: AnnotationHolder) {
        if (GdTypeHintNmReference(element.typeHintNmList.last()).resolve() == null) {
            holder
                .newAnnotation(HighlightSeverity.ERROR, "Invalid type")
                .range(element.textRange)
                .create()
        }
    }

    private fun colorType(element: GdTypeHintNm, holder: AnnotationHolder) {
        val name = element.text

        var color = GdHighlighterColors.CLASS_TYPE
        if (GdKeywords.BUILT_TYPES.contains(name) || name == GdKeywords.VOID) {
            color = GdHighlighterColors.KEYWORD
        }

        if (GdClassMemberUtil.listDeclarations(element, element.text).firstOrNull()
                ?.psi()?.containingFile?.isInSdk() == true
        )
            color = GdHighlighterColors.BASE_TYPE

        holder
            .newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element.textRange)
            .textAttributes(color)
            .create()
    }

}
