package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import gdscript.GdKeywords
import gdscript.highlighter.GdHighlighterColors
import gdscript.psi.*
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.utils.PsiElementUtil.getCallExpr

/**
 * Colors references
 * Checks for existence
 */
class GdRefIdAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is GdRefIdNm) return;
        val txt = element.text;

        if (txt == GdKeywords.SELF || txt == GdKeywords.SUPER) return;
        if (GdKeywords.BUILT_TYPES.contains(txt)
            || arrayOf(GdKeywords.PI, GdKeywords.TAU, GdKeywords.INF, GdKeywords.NAN).contains(txt)
        ) {
            holder
                .newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element.textRange)
                .textAttributes(GdHighlighterColors.KEYWORD)
                .create()
            return
        }

        var attribute = when (GdClassMemberUtil.findDeclaration(element)) {
            is GdMethodDeclTl -> GdHighlighterColors.METHOD_CALL
            is PsiFile, is GdClassDeclTl -> GdHighlighterColors.CLASS_TYPE
            null -> run {
                if (element.text == "new"
                    || GdClassMemberUtil.calledUpon(element)?.returnType == "Dictionary"
                ) {
                    return@run GdHighlighterColors.MEMBER
                }

                val calledUponType = GdClassMemberUtil.calledUpon(element)
                // For undefined types do not mark it as error
                if (calledUponType != null &&
                    (calledUponType.returnType == "" || calledUponType.returnType == GdKeywords.VARIANT)
                ) return@run GdHighlighterColors.MEMBER

                if (element.getCallExpr() != null && GdClassMemberUtil.hasMethodCheck(element))
                    return@run GdHighlighterColors.METHOD_CALL

                holder
                    .newAnnotation(HighlightSeverity.ERROR, "Reference [${element.text}] not found")
                    .range(element.textRange)
                    .create()
                return
            }

            else -> GdHighlighterColors.MEMBER
        }

        if (attribute == GdHighlighterColors.MEMBER && element.getCallExpr() != null) {
            attribute = GdHighlighterColors.METHOD_CALL
        }

        holder
            .newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element.textRange)
            .textAttributes(attribute)
            .create()
    }

}
