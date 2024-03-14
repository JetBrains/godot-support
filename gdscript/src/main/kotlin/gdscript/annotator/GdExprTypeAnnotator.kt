package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import gdscript.GdKeywords
import gdscript.psi.*
import gdscript.psi.utils.GdExprUtil
import gdscript.utils.GdExprUtil.left
import gdscript.utils.GdExprUtil.right
import gdscript.utils.GdOperand
import gdscript.utils.StringUtil.isDynamicType

class GdExprTypeAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is GdFactorEx -> factorExpr(element, holder)
            is GdPlusEx -> plusExpr(element, holder)
            is GdBitAndEx -> bitAndExpr(element, holder)
            is GdVarDeclSt -> varDeclExpr(element, holder)
            is GdAssignSt -> assignExpr(element, holder)
            is GdShiftEx -> shiftExpr(element, holder)
            is GdComparisonEx -> comparisonExpr(element, holder)
            is GdClassVarDeclTl -> classVarDecl(element, holder)
        }
    }

    private fun varDeclExpr(element: GdVarDeclSt, holder: AnnotationHolder) {
        val left = element.returnType
        val right = element.expr?.returnType ?: return
        val operator = element.assignTyped?.text ?: return
        validate(left, right, operator, "Cannot assign", element, holder)
    }

    private fun classVarDecl(element: GdClassVarDeclTl, holder: AnnotationHolder) {
        val left = element.returnType
        val right = element.expr?.returnType ?: return
        val operator = element.assignTyped?.text ?: return
        validate(left, right, operator, "Cannot assign", element, holder)
    }

    private fun assignExpr(element: GdAssignSt, holder: AnnotationHolder) {
        val left = element.exprList.left()
        val right = element.exprList.right()
        val operator = element.assignSign.text
        validate(left, right, operator, "Cannot assign", element, holder)
    }

    private fun factorExpr(element: GdFactorEx, holder: AnnotationHolder) {
        val left = element.exprList.left()
        val right = element.exprList.right()
        val operator = element.factorSign.text
        validate(left, right, operator, "Cannot factor", element, holder)
    }

    private fun plusExpr(element: GdPlusEx, holder: AnnotationHolder) {
        val left = element.exprList.left()
        val right = element.exprList.right()
        val operator = element.sign.text
        validate(left, right, operator, "Cannot factor", element, holder)
    }

    private fun shiftExpr(element: GdShiftEx, holder: AnnotationHolder) {
        val left = element.exprList.left()
        val right = element.exprList.right()
        validate(left, right, "<<", "Cannot factor", element, holder)
    }

    private fun comparisonExpr(element: GdComparisonEx, holder: AnnotationHolder) {
        val left = element.exprList.left()
        val right = element.exprList.right()
        validate(left, right, element.operator.text, "Cannot factor", element, holder)
    }

    private fun bitAndExpr(element: GdBitAndEx, holder: AnnotationHolder) {
        val left = element.exprList.left()
        val right = element.exprList.right()
        val operator = element.bitAndSign.text
        validate(left, right, operator, "Incomparable", element, holder)
    }

    private fun validate(
        left: String,
        right: String,
        operator: String,
        message: String,
        element: PsiElement,
        holder: AnnotationHolder,
    ) {
        if (left == right || right == GdKeywords.NULL) return
        if (left.isDynamicType() || right.isDynamicType()) return
        if (left == "PackedScene") return
        if (GdOperand.isAllowed(left, right, operator, element.project)) return
        if (operator == "=" && GdExprUtil.typeAccepts(right, left, element)) return

        holder
            .newAnnotation(HighlightSeverity.ERROR, "$message $left $operator $right")
            .range(element.textRange)
            .create()
    }


}

