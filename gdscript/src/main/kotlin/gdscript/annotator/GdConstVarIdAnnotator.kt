package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import gdscript.index.impl.GdClassNamingIndex
import gdscript.index.impl.GdClassVarDeclIndex
import gdscript.index.impl.GdConstDeclIndex
import gdscript.psi.*

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
                checkName = previous.varName;
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
        var parentName: String? =
            PsiTreeUtil.getChildOfType(element.containingFile, GdInheritance::class.java)?.inheritanceName;
        val thisName = element.name.orEmpty();

        while (parentName !== null) {
            val parent =
                GdClassNamingIndex.get(parentName, element.project, GlobalSearchScope.allScope(element.project))
                    .firstOrNull();
            if (parent === null) {
                return true;
            }

            if (!GdConstDeclIndex.get(thisName, element.project, GlobalSearchScope.fileScope(parent.containingFile))
                    .isEmpty()
                || !GdClassVarDeclIndex.get(thisName, element.project, GlobalSearchScope.fileScope(parent.containingFile))
                    .isEmpty()
            ) {
                holder
                    .newAnnotation(HighlightSeverity.ERROR,
                        "Field with name [${element.name}] defined in parent class")
                    .range(element.textRange)
                    .create();
                return false;
            }

            parentName = parent.parentName;
        }

        return true;
    }

}
