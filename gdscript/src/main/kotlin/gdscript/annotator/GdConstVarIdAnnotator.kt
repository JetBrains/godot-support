package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import gdscript.action.quickFix.GdAddReturnType
import gdscript.action.quickFix.GdRemoveElementAction
import gdscript.psi.*
import gdscript.psi.utils.GdClassMemberUtil

/**
 * Checks for uniqueness of variables
 * Adds quickfix for return types
 */
class GdConstVarIdAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is GdConstIdNmi || element is GdClassVarIdNmi) {
            isUnique(element as GdNamedIdElement, holder);
        } else {
            hasReturnType(element, holder);
        }
    }

    private fun isUnique(element: GdNamedIdElement, holder: AnnotationHolder) {
        if (GdClassMemberUtil.findDeclaration(element) != null) {
            holder
                .newAnnotation(HighlightSeverity.ERROR,
                    "[${element.name}] is already defined")
                .range(element.textRange)
                .create();
        }
    }

    private fun hasReturnType(element: PsiElement, holder: AnnotationHolder) {
        val returnTypes = when (element) {
            is GdConstDeclTl -> Triple(element.assignTyped, element.typed, element.expr)
            is GdClassVarDeclTl -> Triple(element.assignTyped, element.typed, element.expr)
            is GdVarDeclSt -> Triple(element.assignTyped, element.typed, element.expr)
            is GdConstDeclSt -> Triple(element.assignTyped, element.typed, element.expr)
            else -> return;
        }

        val assigment = returnTypes.first;
        // := assigment cannot specify the type
        if (assigment !== null && assigment.text.equals(":=")) {
            if (returnTypes.second === null) {
                return;
            }

            holder
                .newAnnotation(HighlightSeverity.ERROR, ":= assigment cannot have return type")
                .range(TextRange.create(returnTypes.second!!.textRange.startOffset, assigment.textRange.endOffset))
                .withFix(GdRemoveElementAction(returnTypes.second!!))
                .create()
            return;
        }

        val returnType = returnTypes.second?.typeHintNmList?.first()?.text;
        val expr = returnTypes.third;
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
