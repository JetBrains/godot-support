package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import gdscript.psi.*
import gdscript.psi.utils.PsiGdNamedUtil

class GdConstVarIdAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element !is GdConstIdNmi && element !is GdClassVarIdNmi) {
            return;
        }

        if (!isLocallyUnique(element as GdNamedIdElement, holder)) {
            return;
        }

        isParentallyUnique(element, holder);
    }

    private fun isLocallyUnique(element: GdNamedIdElement, holder: AnnotationHolder): Boolean {
        val thisName = element.name;
        val constDecl = element.parent;
        var checkName: String?;

        var previous = constDecl.prevSibling;
        while (previous !== null) {
            if (previous is GdConstDeclTl) {
                checkName = previous.constName;
            } else if (previous is GdClassVarDeclTl) {
                checkName = previous.name;
            } else if ( // variables cant be above those
                previous is GdToolline
                || previous is GdClassNaming
                || previous is GdInheritance
            ) {
                return true;
            } else {
                previous = previous.prevSibling;
                continue;
            }

            if (checkName === thisName) {
                holder
                    .newAnnotation(HighlightSeverity.ERROR,
                        "Field with name [${element.name}] already defined above")
                    .range(element.textRange)
                    .create();
                return false;
            }

            previous = previous.prevSibling;
        }

        return true;
    }

    private fun isParentallyUnique(element: GdNamedIdElement, holder: AnnotationHolder): Boolean {
        val exists = PsiGdNamedUtil.isParentallyUnique(element);
        if (exists != null) {
            holder
                .newAnnotation(HighlightSeverity.ERROR,
                    "[${element.name}] is already defined in parent class")
                .range(element.textRange)
                .create();

            return false;
        }

        return true;
    }

}
