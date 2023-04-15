package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.action.quickFix.GdAddReturnType
import gdscript.action.quickFix.GdRemoveElementsAction
import gdscript.psi.*
import gdscript.psi.utils.GdClassMemberUtil

/**
 * Checks for uniqueness of variables
 * Adds quickfix for return types
 */
class GdConstVarIdAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is GdConstIdNmi || element is GdClassVarIdNmi || element is GdVarNmi) {
            isUnique(element as GdNamedIdElement, holder);
        } else {
            hasReturnType(element, holder);
        }
    }

    private fun isUnique(element: GdNamedIdElement, holder: AnnotationHolder) {
        val inMethod = PsiTreeUtil.findFirstParent(element) {
            it is GdClassDeclTl || it is GdMethodDeclTl || it is GdFile
        } is GdMethodDeclTl

        val declaration = GdClassMemberUtil.findDeclaration(element, true, inMethod, true)
        if (declaration != null && declaration !is GdClassNaming) {
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
                .withFix(GdRemoveElementsAction(returnTypes.second!!))
                .create()
            return;
        }

        val returnType = returnTypes.second?.typedVal?.returnType;
        val expr = returnTypes.third;
        if (returnType != null || expr == null) return

        val realType = expr.returnType
        if (realType.isEmpty()) return
        if (realType == GdKeywords.VARIANT || realType == GdKeywords.NULL) return

        holder
            .newAnnotation(HighlightSeverity.WEAK_WARNING, "Field's return type can be specified as [$realType]")
            .range(expr.textRange)
            .withFix(GdAddReturnType(element, realType))
            .create()
    }

}
