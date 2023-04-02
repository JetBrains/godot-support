package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.jetbrains.rd.util.first
import com.jetbrains.rd.util.firstOrNull
import gdscript.GdKeywords
import gdscript.psi.*
import gdscript.psi.utils.GdClassMemberUtil.constructors
import gdscript.utils.StringUtil.parseFromSquare

object GdExprUtil {

    fun getReturnType(expr: GdExpr): PsiElement? {
        return when (expr) {
//            GdFuncDeclEx -> callable.return;
//            is GdPlusMinusEx -> expr.expr.returnType;
//            is GdCastEx -> PsiGdExprUtil.fromTyped(expr.typedVal);
//            is GdTernaryEx -> {
//                val a = expr.exprList.getOrNull(0)?.returnType ?: "";
//                val b = expr.exprList.getOrNull(2)?.returnType ?: "";
//
//                return if (a == b) a else "";
//            };
//            is GdLogicEx -> GdKeywords.BOOL;
//            is GdNegateEx -> GdKeywords.BOOL;
//            is GdInEx -> GdKeywords.BOOL;
//            is GdComparisonEx -> GdKeywords.BOOL;
//            is GdBitAndEx -> GdKeywords.INT;
//            is GdShiftEx -> GdKeywords.INT;
//            is GdPlusEx -> {
//                val a = expr.exprList.getOrNull(0)?.returnType ?: GdKeywords.FLOAT;
//                val b = expr.exprList.getOrNull(1)?.returnType ?: GdKeywords.FLOAT;
//
//                return if (a == b) a else GdKeywords.FLOAT;
//            };
//            is GdFactorEx -> {
//                val a = expr.exprList.getOrNull(0)?.returnType ?: GdKeywords.FLOAT;
//                val b = expr.exprList.getOrNull(1)?.returnType ?: GdKeywords.FLOAT;
//                if (a == "String") { // String template
//                    return b;
//                }
//
//                return if (a == b) a else GdKeywords.FLOAT;
//            };
//            is GdSignEx -> expr.expr?.returnType ?: "";
//            is GdBitNotEx -> GdKeywords.INT;
//            is GdPlusMinusPreEx -> expr.expr?.returnType ?: GdKeywords.INT;
//            is GdAttributeEx -> expr.exprList.lastOrNull()?.returnType ?: "";
//            is GdIsEx -> PsiGdExprUtil.fromTyped(expr.typedVal)
//            is GdCallEx -> {
//                if (expr.text == "new()") { // TODO může to mít params?
//                    if (expr.parent is GdAttributeEx) {
//                        GdPsiUtils.returnType(expr.parent.firstChild);
//                    } else {
//                        ""
//                    }
//                } else {
//                    expr.expr.returnType
//                }
//            };
//            is GdArrEx -> PsiGdExprUtil.fromTyped(expr.exprList.firstOrNull()?.returnType ?: "");
//            is GdPrimaryEx -> {
//                when (expr.firstChild) {
//                    is GdNodePath -> {
//                        val node = GdNodeUtil.findNode(expr.firstChild as GdNodePath);
//
//                        return node?.element?.type ?: "";
//                    }
//                    is GdDictDecl -> return "Dictionary";
//                    is GdArrayDecl -> {
//                        var type = "";
//                        expr.arrayDecl?.exprList?.forEach {
//                            val itType = it.returnType;
//                            type = if (type == itType || type == "") {
//                                itType
//                            } else {
//                                "Array";
//                            }
//                        }
//                        type = if (type.isNotEmpty() && type != "Array") {
//                            "Array[$type]";
//                        } else {
//                            "Array";
//                        }
//
//                        return type;
//                    }
//                    else -> expr.expr?.returnType ?: "";
//                }
//            };
//            is GdLiteralEx -> {
//                val text = expr.text;
//                when (text) {
//                    GdKeywords.TRUE -> return GdKeywords.BOOL;
//                    GdKeywords.FALSE -> return GdKeywords.BOOL;
//                    GdKeywords.NULL -> return GdKeywords.NULL;
//                    GdKeywords.NAN -> return "inf";
//                    GdKeywords.INF -> return "nan";
//                }
//
//                val elementType = expr.firstChild?.elementType;
//                if (elementType == GdTypes.NUMBER) {
//                    if (text.startsWith("0b")) {
//                        return GdKeywords.INT;
//                    } else if (text.startsWith("0x")) {
//                        return GdKeywords.INT;
//                    } else if (text.contains('e') || text.contains('.')) {
//                        return GdKeywords.FLOAT;
//                    }
//
//                    return GdKeywords.INT;
//                } else if (elementType == GdTypes.STRING) {
//                    return GdKeywords.STR;
//                } else if (elementType == GdTypes.REF_ID_NM) {
//                    if (text == GdKeywords.SELF) {
//                        return GdClassUtil.getOwningClassName(expr)
//                    } else if (text == GdKeywords.SUPER) {
//                        // TODO tohle může vrátit zanoření... :/ Losos.InnerClass -> nějak se to musí vyparsovat
//                        return GdInheritanceUtil.getExtendedClassId(expr);
//                    }
//
//                    if (DumbService.isDumb(expr.project)) {
//                        return "";
//                    }
//
//                    val named: GdNamedElement = expr.refIdNm ?: return "";
//                    return when (val element =
//                        GdClassMemberUtil.findDeclaration(named)) {
//                        is GdClassVarDeclTl -> element.returnType;
//                        is GdVarDeclSt -> element.returnType;
//                        is GdConstDeclTl -> element.returnType;
//                        is GdConstDeclSt -> element.returnType;
//                        is GdMethodDeclTl -> element.returnType;
//                        is GdSignalDeclTl -> "Signal";
//                        is GdEnumDeclTl -> "EnumDictionary";
//                        is GdEnumValue -> GdKeywords.INT;
//                        else -> text;
//                    }
//                }
//
//                return "";
//            };
            else -> null;
        }
    }

    @Deprecated("přesun na typeAccepts")
    fun typesMatch(a: String, b: String): Boolean {
        if (a == b) return true;
        // Array checks
        if (a.startsWith("Array") && b.startsWith("Array")) {
            return a.length <= 5 || b.length <= 5;
        }

        return false;
    }

    fun typeAccepts(from: String, into: String, project: Project): Boolean {
        if (from == into) return true
        if (from.isBlank() || into.isBlank()
            || from == GdKeywords.VARIANT || into == GdKeywords.VARIANT
        ) return true

        // left = right
        var left = into
        var right = from

        var arrays = 0
        if (from.startsWith("Array")) arrays++
        if (into.startsWith("Array")) arrays++

        // Only 1 is an array
        if (arrays == 1) {
            return false
        } else if (arrays > 1) {
            left = left.parseFromSquare()
            right = right.parseFromSquare()
        }

        val classId = GdClassUtil.getClassIdElement(left, project) ?: return true
        val classElement = GdClassUtil.getOwningClassElement(classId)
        // Constructor
        GdClassMemberUtil
            .listClassMemberDeclarations(classElement, constructors = true)
            .constructors()
            .forEach {
                if (it.parameters.firstOrNull()?.value == right) return true
            }

        // Inheritance

        return false
    }

}
