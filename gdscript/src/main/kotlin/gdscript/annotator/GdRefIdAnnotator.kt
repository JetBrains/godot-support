package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import gdscript.GdKeywords
import gdscript.highlighter.GdHighlighterColors
import gdscript.psi.GdMethodIdNmi
import gdscript.psi.GdRefIdNm
import gdscript.reference.GdClassMemberReference

/**
 * TODO ii
 * Colors references
 * Checks for existence
 */
class GdRefIdAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        return;

        if (element !is GdRefIdNm) return;
        if (element.text == GdKeywords.SELF) return;

        val attribute = when (GdClassMemberReference(element).resolve()) {
            is GdMethodIdNmi -> {
                GdHighlighterColors.METHOD_CALL;
            }
//            is GdConstIdNmi, is GdVarNmi, is GdClassVarIdNmi -> {
//                GdHighlighterColors.MEMBER;
//            }
            is PsiFile -> {
                GdHighlighterColors.CLASS_TYPE;
            }
            null -> {
                holder
                    .newAnnotation(HighlightSeverity.ERROR, "Member [${element.text}] not found")
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
