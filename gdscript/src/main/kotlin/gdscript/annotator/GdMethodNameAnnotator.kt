package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import gdscript.highlighter.GdHighlighterColors
import gdscript.psi.*
import gdscript.psi.utils.PsiGdClassUtil

class GdMethodNameAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is GdMethodIdNmi) return

        holder
            .newSilentAnnotation(HighlightSeverity.INFORMATION)
            .range(element.textRange)
            .textAttributes(GdHighlighterColors.METHOD_DECLARATION)
            .create();

        if (!isLocallyUnique(element, holder)) {
            return;
        }
    }

    private fun isLocallyUnique(element: GdMethodIdNmi, holder: AnnotationHolder): Boolean {
        val thisName = element.name;
        if (PsiGdClassUtil.getClassName(element) == thisName) {
            return true; // Constructors
        }

        val constDecl = element.parent;
        var checkName: String?;

        var previous = constDecl.prevSibling;
        while (previous !== null) {
            if (previous is GdMethodDeclTl) {
                checkName = previous.name;
            } else {
                previous = previous.prevSibling;
                continue;
            }

            if (checkName == thisName) {
                holder
                    .newAnnotation(HighlightSeverity.ERROR,
                        "Method with name [${element.name}] already defined")
                    .range(element.textRange)
                    .create();
                return false;
            }

            previous = previous.prevSibling;
        }

        return true;
    }

}
