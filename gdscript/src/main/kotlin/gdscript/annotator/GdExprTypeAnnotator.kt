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
            is GdArrEx -> arrIndexExpr(element, holder)
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
        var l = left
        var r = right
        if (l == r || r == GdKeywords.NULL) return
        if (l.isDynamicType() || r.isDynamicType()) return
        if (l == "PackedScene") return
        if (l == "EnumDictionary") l = "int"
        if (r == "EnumDictionary") r = "int"
        if (GdOperand.isAllowed(l, r, operator, element.project)) return
        if (operator.contains("=") && GdExprUtil.typeAccepts(r, l, element)) return
        if (operator.contains("=") && GdExprUtil.typeAccepts(l, r, element)) return

        holder
            .newAnnotationGd(element.project, HighlightSeverity.ERROR, "$message $l $operator $r")
            .range(element.textRange)
            .create()
    }

    private fun arrIndexExpr(element: GdArrEx, holder: AnnotationHolder) {

    }
}

