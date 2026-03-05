package gdscript.annotator

import com.intellij.codeInspection.util.InspectionMessage
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import gdscript.GdKeywords
import gdscript.GdScriptBundle
import gdscript.psi.GdArrEx
import gdscript.psi.GdAssignSt
import gdscript.psi.GdBitAndEx
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdComparisonEx
import gdscript.psi.GdFactorEx
import gdscript.psi.GdPlusEx
import gdscript.psi.GdShiftEx
import gdscript.psi.GdTypes
import gdscript.psi.GdVarDeclSt
import gdscript.psi.utils.GdExprUtil
import gdscript.utils.GdExprUtil.left
import gdscript.utils.GdExprUtil.right
import gdscript.utils.GdOperand
import gdscript.utils.StringUtil.isDynamicType
import org.jetbrains.annotations.Nls
import org.jetbrains.annotations.NonNls

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
        val invalidExpression = createExpressionIfInvalid(left, right, operator, element) ?: return
        holder.annotateExpressionGd(element, GdScriptBundle.message("annotator.cannot.assign", invalidExpression))
    }

    private fun classVarDecl(element: GdClassVarDeclTl, holder: AnnotationHolder) {
        val left = element.returnType
        val right = element.expr?.returnType ?: return
        val operator = element.assignTyped?.text ?: return
        val invalidExpression = createExpressionIfInvalid(left, right, operator, element) ?: return
        holder.annotateExpressionGd(element, GdScriptBundle.message("annotator.cannot.assign", invalidExpression))
    }

    private fun assignExpr(element: GdAssignSt, holder: AnnotationHolder) {
        val left = element.exprList.left()
        val right = element.exprList.right()
        val operator = element.assignSign.text
        val invalidExpression = createExpressionIfInvalid(left, right, operator, element) ?: return
        holder.annotateExpressionGd(element, GdScriptBundle.message("annotator.cannot.assign", invalidExpression))
    }

    private fun factorExpr(element: GdFactorEx, holder: AnnotationHolder) {
        val left = element.exprList.left()
        val right = element.exprList.right()
        val operator = element.factorSign.text
        val invalidExpression = createExpressionIfInvalid(left, right, operator, element) ?: return
        holder.annotateExpressionGd(element, GdScriptBundle.message("annotator.cannot.factor", invalidExpression))
    }

    private fun plusExpr(element: GdPlusEx, holder: AnnotationHolder) {
        val left = element.exprList.left()
        val right = element.exprList.right()
        val operator = element.sign.text
        val invalidExpression = createExpressionIfInvalid(left, right, operator, element) ?: return
        holder.annotateExpressionGd(element, GdScriptBundle.message("annotator.cannot.factor", invalidExpression))
    }

    private fun shiftExpr(element: GdShiftEx, holder: AnnotationHolder) {
        val left = element.exprList.left()
        val right = element.exprList.right()
        val operator = "<<"
        val invalidExpression = createExpressionIfInvalid(left, right, operator, element) ?: return
        holder.annotateExpressionGd(element, GdScriptBundle.message("annotator.cannot.factor", invalidExpression))
    }

    private fun comparisonExpr(element: GdComparisonEx, holder: AnnotationHolder) {
        val left = element.exprList.left()
        val right = element.exprList.right()
        val operator = element.operator.text
        val invalidExpression = createExpressionIfInvalid(left, right, operator, element) ?: return
        holder.annotateExpressionGd(element, GdScriptBundle.message("annotator.cannot.factor", invalidExpression))
    }

    private fun bitAndExpr(element: GdBitAndEx, holder: AnnotationHolder) {
        val left = element.exprList.left()
        val right = element.exprList.right()
        val operator = element.bitAndSign.text
        val invalidExpression = createExpressionIfInvalid(left, right, operator, element) ?: return
        holder.annotateExpressionGd(element, GdScriptBundle.message("annotator.incomparable", invalidExpression))
    }

    /** @return null if the expression is valid */
    @NonNls
    private fun createExpressionIfInvalid(
        @NonNls left: String,
        @NonNls right: String,
        @NonNls operator: String,
        element: PsiElement
    ): String? {
        @NonNls var l = left
        @NonNls var r = right
        if (l == r || r == GdKeywords.NULL) return null
        if (l.isDynamicType() || r.isDynamicType()) return null
        if (l == "PackedScene") return null
        if (l == "EnumDictionary") l = "int"
        if (r == "EnumDictionary") r = "int"
        if (GdOperand.isAllowed(l, r, operator, element.project)) return null
        if (operator.contains("=") && GdExprUtil.typeAccepts(r, l, element)) return null
        if (operator.contains("=") && GdExprUtil.typeAccepts(l, r, element)) return null

        return "$l $operator $r"
    }

    private fun AnnotationHolder.annotateExpressionGd(
        element: PsiElement,
        @InspectionMessage message: String
    ): Unit =
        this
            .newAnnotationGd(HighlightSeverity.ERROR, message)
            .range(element.textRange)
            .create()

    private fun arrIndexExpr(element: GdArrEx, holder: AnnotationHolder) {
        val exprs = element.exprList
        if (exprs.getOrNull(1) == null) {
            // Highlight both brackets and everything between them
            val lBracket = element.node.findChildByType(GdTypes.LSBR)?.psi
            val rBracket = element.node.findChildByType(GdTypes.RSBR)?.psi
            val range = when {
                lBracket != null && rBracket != null -> lBracket.textRange.union(rBracket.textRange)
                else -> element.textRange
            }
            holder
                .newAnnotationGd(HighlightSeverity.ERROR, GdScriptBundle.message("annotator.indexer.used.with.0.arguments"))
                .range(range)
                .create()
            return
        }

        val baseType = exprs.getOrNull(0)?.returnType ?: return
        val indexType = exprs.getOrNull(1)?.returnType ?: return

        // Skip if dynamic/unresolved
        if (baseType.isDynamicType() || indexType.isDynamicType()) return

        var expectedKey: String? = null
        if (baseType.startsWith("Array[")) {
            expectedKey = GdKeywords.INT
        } else if (baseType == "Array") {
            expectedKey = GdKeywords.INT
        } else if (baseType.startsWith("Dictionary[")) {
            val inside = baseType.substringAfter("[").substringBeforeLast("]")
            val parts = inside.split(",").map { it.trim() }
            if (parts.isNotEmpty()) expectedKey = parts[0]
        }

        val exp = expectedKey ?: return // if null (untyped dict) do not enforce
        // If the expected key type accepts the provided index type, it's fine
        if (GdExprUtil.typeAccepts(exp, indexType, element)) return

        holder
            .newAnnotationGd(
                HighlightSeverity.ERROR,
                GdScriptBundle.message("annotator.invalid.index.type", indexType, exp)
            )
            .range(exprs.getOrNull(1)?.textRange ?: element.textRange)
            .create()
    }
}

