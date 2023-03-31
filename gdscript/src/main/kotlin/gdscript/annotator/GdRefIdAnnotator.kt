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
                // For get_node(), ... to ignore unknown types
//                val ignoreTypes = arrayOf("get_node", "get_parent", "get_node_or_null")
//                if (ignoreTypes.contains(calledUponType?.text)) {
//                    val prev = PsiTreeUtil.nextVisibleLeaf(element)
//                    if (prev?.elementType == GdTypes.LRBR && prev?.parent is GdCallEx) {
//                        return@run GdHighlighterColors.METHOD_CALL
//                    }
//                    return@run GdHighlighterColors.MEMBER
//                }

                holder
                    .newAnnotation(HighlightSeverity.ERROR, "Reference [${element.text}] not found")
                    .range(element.textRange)
                    .create()
                return
            }

            else -> GdHighlighterColors.MEMBER
        }

        if (element.getCallExpr() != null) {
            attribute = GdHighlighterColors.METHOD_CALL
        }

        holder
            .newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element.textRange)
            .textAttributes(attribute)
            .create()
    }

}
