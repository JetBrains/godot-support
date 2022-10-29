package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import gdscript.GdKeywords
import gdscript.highlighter.GdHighlighterColors
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdRefIdNm
import gdscript.psi.utils.GdClassMemberUtil

/**
 * Colors references
 * Checks for existence
 */
class GdRefIdAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is GdRefIdNm) return;
        if (element.text == GdKeywords.SELF || element.text == GdKeywords.SUPER) return;

        val attribute = when (GdClassMemberUtil.findDeclaration(element)) {
            is GdMethodDeclTl -> GdHighlighterColors.METHOD_CALL;
            is PsiFile, is GdClassDeclTl -> GdHighlighterColors.CLASS_TYPE;
            null -> {
                holder
                    .newAnnotation(HighlightSeverity.ERROR, "Reference [${element.text}] not found")
                    .range(element.textRange)
                    .create();
                return;
            }
            else -> GdHighlighterColors.MEMBER;
        }

        holder
            .newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element.textRange)
            .textAttributes(attribute)
            .create()
    }

}
