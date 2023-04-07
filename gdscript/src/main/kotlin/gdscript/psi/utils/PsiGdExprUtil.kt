package gdscript.psi.utils

import com.intellij.openapi.project.DumbService
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.elementType
import gdscript.GdKeywords
import gdscript.utils.GdOperand
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.*
import gdscript.utils.GdExprUtil.left
import gdscript.utils.GdExprUtil.right
import project.psi.model.GdAutoload

object PsiGdExprUtil {

    fun getReturnType(expr: GdExpr): String {
        return when (expr) {
            is GdFuncDeclEx -> GdKeywords.CALLABLE;
            is GdPlusMinusEx -> expr.expr.returnType;
            is GdCastEx -> fromTyped(expr.typedVal);
            is GdTernaryEx -> {
                val a = expr.exprList.getOrNull(0)?.returnType ?: "";
                val b = expr.exprList.getOrNull(2)?.returnType ?: "";

                return if (a == b) a else "";
            };
            is GdLogicEx -> GdKeywords.BOOL;
            is GdNegateEx -> GdKeywords.BOOL;
            is GdInEx -> GdKeywords.BOOL;
            is GdShiftEx -> GdKeywords.INT;
            is GdBitAndEx -> GdKeywords.INT;
            is GdComparisonEx -> GdOperand.getReturnType(
                expr.exprList.left(), expr.exprList.right(), expr.operator.text,
            )
            is GdPlusEx -> GdOperand.getReturnType(
                expr.exprList.left(), expr.exprList.right(), expr.sign.text,
            )
            is GdFactorEx -> GdOperand.getReturnType(
                expr.exprList.left(), expr.exprList.right(), expr.factorSign.text,
            )
            is GdSignEx -> expr.expr?.returnType ?: ""
            is GdBitNotEx -> GdKeywords.INT;
            is GdPlusMinusPreEx -> expr.expr?.returnType ?: GdKeywords.INT;
            is GdAttributeEx -> expr.exprList.lastOrNull()?.returnType ?: "";
            is GdIsEx -> fromTyped(expr.typedVal)
            is GdCallEx -> {
                if (expr.text == "new()") { // TODO může to mít params?
                    if (expr.parent is GdAttributeEx) {
                        GdPsiUtils.returnType(expr.parent.firstChild);
                    } else {
                        ""
                    }
                } else {
                    val method = expr.expr.text
                    if (method == "get_node" || method == "get_node_or_null") {
                        //TODO zkusit vyparsovat Node z .tscn
                        return GdKeywords.VARIANT
                    } else if (method == "instantiate") {
                        return GdKeywords.VARIANT
                    } else if (method == "get_child") {
                        return GdKeywords.VARIANT
                    } else if (method == "get_parent") {
                        //TODO zkusit vyparsovat Node z .tscn
                        return GdKeywords.VARIANT
                    }
                    expr.expr.returnType
                }
            }
            // TODO [] array accesor tu je také -> např. Basis je také přístupný -> potřeba překopat Array access
            is GdArrEx -> fromTyped(expr.exprList.firstOrNull()?.returnType ?: "");
            is GdPrimaryEx -> {
                when (expr.firstChild) {
                    is GdNodePath -> {
                        val node = GdNodeUtil.findNode(expr.firstChild as GdNodePath);

                        return node?.element?.type ?: "";
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
                    GdKeywords.NAN -> return "inf";
                    GdKeywords.INF -> return "nan";
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
                } else if (elementType == GdTypes.STRING_VAL) {
                    return GdKeywords.STR;
                } else if (elementType == GdTypes.STRING) {
                    return GdKeywords.STR;
                } else if (elementType == GdTypes.STRING_NAME) {
                    return GdKeywords.STR_NAME;
                } else if (elementType == GdTypes.NODE_PATH) {
                    return GdKeywords.STR;
                } else if (elementType == GdTypes.NODE_PATH_LIT) {
                    return GdKeywords.NODE_PATH;
                } else if (elementType == GdTypes.REF_ID_NM) {
                    if (text == GdKeywords.SELF) {
                        return GdClassUtil.getOwningClassName(expr)
                    } else if (text == GdKeywords.SUPER) {
                        // TODO tohle může vrátit zanoření... :/ Losos.InnerClass -> nějak se to musí vyparsovat
                        return GdInheritanceUtil.getExtendedClassId(expr);
                    }

                    if (DumbService.isDumb(expr.project)) {
                        return "";
                    }

                    val named: GdNamedElement = expr.refIdNm ?: return "";
                    return when (val element =
                        GdClassMemberUtil.findDeclaration(named)) {
                        is GdClassVarDeclTl -> element.returnType
                        is GdVarDeclSt -> element.returnType
                        is GdConstDeclTl -> element.returnType
                        is GdConstDeclSt -> element.returnType
                        is GdMethodDeclTl -> element.returnType
                        is GdSignalDeclTl -> "Signal"
                        is GdEnumDeclTl -> "EnumDictionary"
                        is GdEnumValue -> GdKeywords.INT
                        is GdClassNaming -> text
                        is GdForSt -> {
                            var type = element.expr?.returnType?.removePrefix("Array[")?.removeSuffix("]") ?: ""
                            if (type == "Array") type = ""

                            return type
                        }
                        is GdAutoload -> element.key
                        else -> ""
                    }
                }

                return ""
            }
            else -> ""
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

    fun getAttrOrCallParentFile(element: PsiElement): PsiFile? {
        var className = getAttrOrCallParentClass(element) ?: return null;
        if (className.startsWith("Array")) {
            className = "Array";
        }

        return GdClassNamingIndex.get(className, element.project, GlobalSearchScope.allScope(element.project))
            .firstOrNull()?.containingFile;
    }

    private fun fromTyped(typed: String): String {
        if (typed.startsWith("Array")) {
            return typed.substring(5).trim('[', ']');
        }

        return typed;
    }

    private fun fromTyped(typed: GdTypedVal?): String {
        if (typed == null) return "";

        val main = typed.typeHintList.first().text;
        if (main != "Array") {
            return main;
        }

        return typed.typeHintList.last().text;
    }

}
