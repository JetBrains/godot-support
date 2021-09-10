package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import gdscript.action.quickFix.GdAddReturnType
import gdscript.psi.*
import gdscript.psi.utils.PsiGdNamedUtil

class GdConstVarIdAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is GdConstIdNmi || element is GdClassVarIdNmi) {
            if (!isLocallyUnique(element as GdNamedIdElement, holder)) {
                return;
            }

            isParentallyUnique(element, holder);
        } else {
            hasReturnType(element, holder);
        }
    }

    private fun isLocallyUnique(element: GdNamedIdElement, holder: AnnotationHolder): Boolean {
        val thisName = element.name;
        val constDecl = element.parent;
        var checkName: String?;

        // TODO tady chybí enum check -> chtělo by to rozšířit an ten parentaly... :/
        var previous = constDecl.prevSibling;
        while (previous !== null) {
            if (previous is GdConstDeclTl) {
                checkName = previous.constName;
            } else if (previous is GdClassVarDeclTl) {
                checkName = previous.name;
            } else if ( // variables cant be above those
                //previous is GdToolline TODO
                previous is GdClassNaming
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
        val exists = PsiGdNamedUtil.findInParent(element);
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

    private fun hasReturnType(element: PsiElement, holder: AnnotationHolder) {
        if (element is GdClassVarDeclTl || element is GdConstDeclTl) {
            val returnTypes = when (element) {
                is GdConstDeclTl -> Pair(element.typed, element.expr)
                is GdClassVarDeclTl -> Pair(element.typed, element.expr)
                else -> Pair(null, null);
            }

            val returnType = returnTypes.first?.typeHintNm?.text;
            val expr = returnTypes.second;
            if (returnType != null || expr == null) {
                return;
            }
            val realType = expr.returnType;
            if (realType.isEmpty()) {
                return;
            }

            holder
                .newAnnotation(HighlightSeverity.WEAK_WARNING, "Field's return type can be specified as [$realType]")
                .range(expr.textRange)
                .withFix(GdAddReturnType(element, realType))
                .create()
        }
    }

}
