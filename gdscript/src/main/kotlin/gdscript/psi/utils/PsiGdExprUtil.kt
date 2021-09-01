package gdscript.psi.utils

import com.intellij.psi.util.elementType
import gdscript.GdKeywords
import gdscript.psi.*
import org.intellij.markdown.flavours.gfm.table.GitHubTableMarkerProvider.Companion.contains

object PsiGdExprUtil {

    fun getReturnType(expr: GdExpr): String {
        if (expr is GdPlusMinusEx) {
            return expr.expr.returnType;
        } else if (expr is GdCastEx) {
            return expr.typeHintNm.text;
        } else if (expr is GdTernaryEx) {
            val a = expr.exprList.getOrNull(0)?.returnType ?: "";
            val b = expr.exprList.getOrNull(2)?.returnType ?: "";

            return if (a == b) a else "";
        } else if (expr is GdLogicEx) {
            return GdKeywords.BOOL;
        } else if (expr is GdNegateEx) {
            return GdKeywords.BOOL;
        } else if (expr is GdInEx) {
            return GdKeywords.BOOL;
        } else if (expr is GdComparisonEx) {
            return GdKeywords.BOOL;
        } else if (expr is GdBitAndEx) {
            return GdKeywords.INT;
        } else if (expr is GdShiftEx) {
            return GdKeywords.INT;
        } else if (expr is GdPlusEx) {
            val a = expr.exprList.getOrNull(0)?.returnType ?: GdKeywords.FLOAT;
            val b = expr.exprList.getOrNull(1)?.returnType ?: GdKeywords.FLOAT;

            return if (a == b) a else GdKeywords.FLOAT;
        } else if (expr is GdFactorEx) {
            val a = expr.exprList.getOrNull(0)?.returnType ?: GdKeywords.FLOAT;
            val b = expr.exprList.getOrNull(1)?.returnType ?: GdKeywords.FLOAT;

            return if (a == b) a else GdKeywords.FLOAT;
        } else if (expr is GdSignEx) {
            return expr.expr?.returnType ?: "";
        } else if (expr is GdBitNotEx) {
            return GdKeywords.INT;
        } else if (expr is GdPlusMinusPreEx) {
            return expr.expr?.returnType ?: GdKeywords.INT;
        } else if (expr is GdAttributeEx) {
            //TODO dopsat gramatiku
        } else if (expr is GdIsEx) {
            return expr.typeHintNm.text;
        } else if (expr is GdCallEx) {
            // TODO
        } else if (expr is GdArrEx) {
            // TODO
        } else if (expr is GdPrimaryEx) {
            if (expr is GdNodePath) {
                // TODO node path to class !! potřeba .tscn
                return "";
            }
            // arraydelc, dictDecl, ( expr )
        } else if (expr is GdLiteralEx) {
            val text = expr.text;
            when (text) {
                GdKeywords.TRUE -> return GdKeywords.BOOL;
                GdKeywords.FALSE -> return GdKeywords.BOOL;
                GdKeywords.NULL -> return GdKeywords.NULL;
                GdKeywords.PI -> return GdKeywords.FLOAT;
                GdKeywords.TAU -> return GdKeywords.FLOAT;
                GdKeywords.NAN -> return GdKeywords.NAN;
                GdKeywords.INF -> return GdKeywords.FLOAT;
                // TODO
                "ref" -> return "bool";
            }

            val elementType = expr.firstChild?.elementType;
            if (elementType == GdTypes.NUMBER) {
                if (text.startsWith("0b")) {
                    return GdKeywords.INT;
                } else if (text.startsWith("0x")) {
                    return GdKeywords.INT;
                } else if (text.contains('e') || text.contains('.')) {
                    return GdKeywords.FLOAT;
                }

                return GdKeywords.INT;
            } else if (elementType == GdTypes.STRING) {
                return GdKeywords.STR;
            }
        }

        return "";
    }

}