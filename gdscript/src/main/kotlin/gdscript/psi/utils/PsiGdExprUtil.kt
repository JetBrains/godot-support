package gdscript.psi.utils

import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.elementType
import gdscript.GdKeywords
import gdscript.psi.*
import org.intellij.markdown.flavours.gfm.table.GitHubTableMarkerProvider.Companion.contains
import tscn.index.impl.TscnNodeIndex
import tscn.index.impl.TscnScriptIndex

object PsiGdExprUtil {

    fun getReturnType(expr: GdExpr): String {
        return when (expr) {
            is GdPlusMinusEx -> expr.expr.returnType;
            is GdCastEx -> expr.typeHintNm.text;
            is GdTernaryEx -> {
                val a = expr.exprList.getOrNull(0)?.returnType ?: "";
                val b = expr.exprList.getOrNull(2)?.returnType ?: "";

                return if (a == b) a else "";
            };
            is GdLogicEx -> GdKeywords.BOOL;
            is GdNegateEx -> GdKeywords.BOOL;
            is GdInEx -> GdKeywords.BOOL;
            is GdComparisonEx -> GdKeywords.BOOL;
            is GdBitAndEx -> GdKeywords.INT;
            is GdShiftEx -> GdKeywords.INT;
            is GdPlusEx -> {
                val a = expr.exprList.getOrNull(0)?.returnType ?: GdKeywords.FLOAT;
                val b = expr.exprList.getOrNull(1)?.returnType ?: GdKeywords.FLOAT;

                return if (a == b) a else GdKeywords.FLOAT;
            };
            is GdFactorEx -> {
                val a = expr.exprList.getOrNull(0)?.returnType ?: GdKeywords.FLOAT;
                val b = expr.exprList.getOrNull(1)?.returnType ?: GdKeywords.FLOAT;

                return if (a == b) a else GdKeywords.FLOAT;
            };
            is GdSignEx -> expr.expr?.returnType ?: "";
            is GdBitNotEx -> GdKeywords.INT;
            is GdPlusMinusPreEx -> expr.expr?.returnType ?: GdKeywords.INT;
            is GdAttributeEx -> ""; // TODO gramatika
            is GdIsEx -> expr.typeHintNm.text;
            is GdCallEx -> ""; // TODO
            is GdArrEx -> ""; // TODO
            is GdPrimaryEx -> {
                when (expr.firstChild) {
                    is GdNodePath -> {
                        val project = expr.project;
                        val filename = PsiGdFileUtil.filepath(expr);
                        val script = TscnScriptIndex.get(filename, project, GlobalSearchScope.allScope(project))
                            .firstOrNull()
                            ?: return "";

                        val node = TscnNodeIndex.get(
                            expr.text.removePrefix("$"),
                            project,
                            GlobalSearchScope.fileScope(script.containingFile)
                        ).firstOrNull();

                        return node?.type ?: "";
                    }
                }
                // TODO arraydelc, dictDecl, ( expr )
                return "";
            };
            is GdLiteralEx -> {
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
                    //"ref" -> return "bool";
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

                return "";
            };
            else -> "";
        }
    }

}