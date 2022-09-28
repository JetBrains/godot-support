package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import gdscript.GdKeywords
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.*
import org.intellij.markdown.flavours.gfm.table.GitHubTableMarkerProvider.Companion.contains
import tscn.index.impl.TscnNodeIndex
import tscn.index.impl.TscnScriptIndex

object PsiGdExprUtil {

    fun getReturnType(expr: GdExpr): String {
        return when (expr) {
            is GdPlusMinusEx -> expr.expr.returnType;
            is GdCastEx -> fromTyped(expr.isTyped);
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
            is GdAttributeEx -> expr.exprList.lastOrNull()?.returnType ?: "";
            is GdIsEx -> fromTyped(expr.isTyped)
            is GdCallEx -> expr.expr.returnType;
            is GdArrEx -> fromTyped(expr.exprList.firstOrNull()?.returnType ?: "");
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
                    is GdDictDecl -> return "Dictionary";
                    is GdArrayDecl -> {
                        var type = "";
                        expr.arrayDecl?.exprList?.forEach {
                            val itType = it.returnType;
                            type = if (type == itType || type == "") {
                                itType
                            } else {
                                "Array";
                            }
                        }
                        type = if (type.isNotEmpty() && type != "Array") {
                            "Array[$type]";
                        } else {
                            "Array";
                        }

                        return type;
                    }
                    else -> expr.expr?.returnType ?: "";
                }
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
                } else if (elementType == GdTypes.REF_ID_NM) {
                    val named: GdNamedElement = expr.refIdNm ?: return "";
                    var parentFile = expr.containingFile;
                    if (text == GdKeywords.SELF) {
                        val clName = PsiGdClassUtil.getClassName(expr);
                        if (clName != null) {
                            return clName;
                        }
                        val inheritance = PsiTreeUtil.getChildOfType(parentFile, GdInheritance::class.java);
                        if (inheritance != null) {
                            return inheritance.inheritanceName ?: "";
                        }

                        return GdKeywords.SELF;
                    }

                    when (val parent = expr.parent) {
                        is GdAttributeEx -> {
                            if (expr.prevSibling != null) {
                                val first = parent.exprList.first();
                                val parReturn = first?.returnType ?: return "";
                                if (parReturn != GdKeywords.SELF) {
                                    parentFile = GdClassNamingIndex
                                        .get(parReturn, expr.project, GlobalSearchScope.allScope(expr.project))
                                        .firstOrNull()?.containingFile ?: return "";
                                }
                            }
                        }
                        is GdCallEx -> {
                            val prev = parent.parent;
                            if (prev.text != GdKeywords.SELF
                                && prev is GdAttributeEx
                            ) {
                                if (parent.prevSibling != null) {
                                    val first = prev.exprList.first();
                                    val parReturn = first?.returnType ?: return "";
                                    parentFile = GdClassNamingIndex
                                        .get(parReturn, expr.project, GlobalSearchScope.allScope(expr.project))
                                        .firstOrNull()?.containingFile ?: return "";
                                }
                            }
                        }
                    }

                    return when (val element = PsiGdNamedUtil.findInParent(named, includingSelf = true, containingFile = parentFile)) {
                        is GdMethodDeclTl -> element.returnType;
                        is GdClassVarDeclTl -> element.returnType;
                        is GdConstDeclTl -> element.returnType;
                        is GdVarDeclSt -> {
                            val sdf = element.returnType;
                            return sdf;
                        };
                        is GdConstDeclSt -> element.returnType;
                        is GdEnumDeclTl -> GdKeywords.INT;
                        is GdEnumValue -> GdKeywords.INT;
                        else -> {
                            GdClassNamingIndex.get(text, expr.project, GlobalSearchScope.allScope(expr.project))
                                .firstOrNull()?.classname ?: "";
                        }
                    }
                }

                return "";
            };
            else -> "";
        }
    }

    fun fromTyped(typed: GdTyped?): String {
        return typed?.text?.trim(':', ' ') ?: "";
    }

    fun getAttrOrCallParentClass(element: PsiElement): String? {
        if (element is GdRefIdNm
            && element.parent != null
            && element.parent is GdLiteralEx
        ) {
            val root = element.parent.parent ?: return null;
            if (root is GdAttributeEx && element.parent.prevSibling != null) {
                return GdPsiUtils.returnType(root.firstChild);
            }
            if (root is GdCallEx && root.prevSibling != null && root.parent is GdAttributeEx) {
                return GdPsiUtils.returnType(root.parent.firstChild);
            }
        }

        return null;
    }

    private fun fromTyped(typed: String): String {
        if (typed.startsWith("Array")) {
            return typed.substring(5).trim('[', ']');
        }

        return typed;
    }

    private fun fromTyped(typed: GdIsTyped?): String {
        if (typed == null) {
            return "";
        }

        val main = typed.typeHintNm.text;
        if (main != "Array") {
            return main;
        }

        return typed.typeHintArrayNm?.text ?: "";
    }

}