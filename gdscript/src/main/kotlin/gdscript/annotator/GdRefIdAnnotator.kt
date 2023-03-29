package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import gdscript.GdKeywords
import gdscript.highlighter.GdHighlighterColors
import gdscript.psi.*
import gdscript.psi.utils.GdClassMemberUtil

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

        val attribute = when (GdClassMemberUtil.findDeclaration(element)) {
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
                if (calledUponType != null && calledUponType.returnType == "") return@run GdHighlighterColors.MEMBER
                // For get_node() to ignore unknown types
                if (calledUponType is GdCallEx && calledUponType.expr.text == "get_node") {
                    val prev = PsiTreeUtil.nextVisibleLeaf(element)
                    if (prev?.elementType == GdTypes.LRBR && prev?.parent is GdCallEx) {
                        return@run GdHighlighterColors.METHOD_CALL
                    }
                    return@run GdHighlighterColors.MEMBER
                }

                holder
                    .newAnnotation(HighlightSeverity.ERROR, "Reference [${element.text}] not found")
                    .range(element.textRange)
                    .create()
                return
            }

            else -> GdHighlighterColors.MEMBER
        }

        holder
            .newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element.textRange)
            .textAttributes(attribute)
            .create()
    }

}
