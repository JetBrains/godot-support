package gdscript.psi.utils

import com.intellij.openapi.project.DumbService
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import gdscript.GdKeywords
import gdscript.index.impl.GdClassNamingIndex
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.*
import gdscript.utils.GdExprUtil.left
import gdscript.utils.GdExprUtil.right
import gdscript.utils.GdOperand
import gdscript.utils.VirtualFileUtil.getPsiFile
import gdscript.utils.VirtualFileUtil.resourcePath
import project.psi.model.GdAutoload
import java.nio.file.Paths

object PsiGdExprUtil {

    fun getReturnType(expr: GdExpr, allowResource: Boolean = false): String {
        return when (expr) {
            is GdFuncDeclEx -> GdKeywords.CALLABLE
            is GdPlusMinusEx -> expr.expr.returnType
            is GdCastEx -> fromTyped(expr.typedVal)
            is GdTernaryEx -> {
                val a = expr.exprList.getOrNull(0)?.returnType ?: ""
                val b = expr.exprList.getOrNull(2)?.returnType ?: ""

                return if (a == b) a else ""
            }
            is GdLogicEx -> GdKeywords.BOOL
            is GdNegateEx -> GdKeywords.BOOL
            is GdInEx -> GdKeywords.BOOL
            is GdShiftEx -> GdKeywords.INT
            is GdBitAndEx -> GdKeywords.INT
            is GdComparisonEx -> GdOperand.getReturnType(
                expr.exprList.left(), expr.exprList.right(), expr.operator.text, expr.project,
            )
            is GdPlusEx -> GdOperand.getReturnType(
                expr.exprList.left(), expr.exprList.right(), expr.sign.text, expr.project,
            )
            is GdFactorEx -> GdOperand.getReturnType(
                expr.exprList.left(), expr.exprList.right(), expr.factorSign.text, expr.project,
            )
            is GdSignEx -> expr.expr?.returnType ?: ""
            is GdBitNotEx -> GdKeywords.INT
            is GdPlusMinusPreEx -> expr.expr?.returnType ?: GdKeywords.INT
            is GdAttributeEx -> expr.exprList.lastOrNull()?.returnType ?: ""
            is GdIsEx -> fromTyped(expr.typedVal)
            is GdCallEx -> {
                if (expr.text == "new()") { // TODO může to mít params?
                    if (expr.parent is GdAttributeEx) {
                        GdCommonUtil.returnType(expr.parent.firstChild)
                    } else {
                        ""
                    }
                } else {
                    val method = expr.expr.text
                    if (method == "get_node" || method == "get_node_or_null" || method == "get_first_node_in_group") {
                        //TODO zkusit vyparsovat Node z .tscn
                        return "Node"
                    } else if (method == "get_nodes_in_group") {
                        return "Array[Variant]"
                    } else if (method == "instantiate") {
                        return GdKeywords.VARIANT
                    } else if (method == "get_child") {
                        return "Node"
                    } else if (method == "get_parent") {
                        //TODO zkusit vyparsovat Node z .tscn
                        return "Node"
                    } else if (method == "load" || method == "preload") {
                        val res = expr.argList?.argExprList?.firstOrNull()
                        if (res != null) {
                            var resource = res.text.trim('"')
                            if (!resource.startsWith("res://") && expr.containingFile.originalFile.virtualFile?.parent != null) {
                                val myPath = Paths.get(expr.containingFile.originalFile.virtualFile.parent.path)
                                FilenameIndex.getVirtualFilesByName(com.intellij.history.core.Paths.getNameOf(resource), GlobalSearchScope.allScope(expr.project)).find {
                                    val itPath = Paths.get(it.path)
                                    try {
                                        val relative = myPath.relativize(itPath)
                                        return@find relative.toString().replace('\\', '/') == resource
                                    } catch (e: Exception) {}
                                    false
                                }?.let {
                                    resource = it.resourcePath()
                                }
                            }

                            GdFileResIndex.INSTANCE.getFiles(resource, expr)
                                .firstOrNull()
                                ?.getPsiFile(expr)
                                ?.let { GdClassUtil.getOwningClassName(it) }
                                ?.let { return it }
                        }

                        return ""
                    }
                    expr.expr.returnType
                }
            }
            is GdArrEx -> {
                val arrayType = expr.exprList.firstOrNull()?.returnType ?: return GdKeywords.VARIANT
                if (arrayType.startsWith("Array[")) return fromTyped(arrayType)

                return GdOperand.getReturnType(arrayType, GdKeywords.INT, "[]", expr.project)
            }
            is GdPrimaryEx -> {
                when (expr.firstChild) {
                    is GdNodePath -> {
                        if (expr.firstChild.text.contains(':')) return GdKeywords.VARIANT
                        val node = GdNodeUtil.findNode(expr.firstChild as GdNodePath) ?: return GdKeywords.VARIANT

                        node.script?.let {
                            GdFileResIndex.INSTANCE.getFiles(it, expr).firstOrNull()
                                ?.getPsiFile(expr)
                                ?.let { GdClassUtil.getFullClassId(it) }
                                ?.let {
                                    if (!it.startsWith("\"res://") || allowResource) return it
                                }
                        }

                        return node.element.type
                    }
                    is GdDictDecl -> return "Dictionary"
                    is GdArrayDecl -> {
//                        var type = ""
//                        expr.arrayDecl?.exprList?.forEach {
//                            val itType = it.returnType
//                            type = if (type == itType || type == "") {
//                                itType
//                            } else {
//                                "Array"
//                            }
//                        }
//                        type = if (type.isNotEmpty() && type != "Array") {
//                            "Array[$type]"
//                        } else {
//                            "Array[Variant]"
//                        }

                        return "Array[Variant]"
                    }
                    else -> expr.expr?.returnType ?: ""
                }
            }
            is GdLiteralEx -> {
                val text = expr.text
                when (text) {
                    GdKeywords.TRUE -> return GdKeywords.BOOL
                    GdKeywords.FALSE -> return GdKeywords.BOOL
                    GdKeywords.NULL -> return GdKeywords.NULL
                    GdKeywords.NAN -> return "inf"
                    GdKeywords.INF -> return "nan"
                }

                val elementType = expr.firstChild?.elementType
                if (elementType == GdTypes.NUMBER) {
                    if (text.startsWith("0b")) {
                        return GdKeywords.INT
                    } else if (text.startsWith("0x")) {
                        return GdKeywords.INT
                    } else if (text.contains('e') || text.contains('.')) {
                        return GdKeywords.FLOAT
                    }

                    return GdKeywords.INT
                } else if (elementType == GdTypes.STRING_VAL) {
                    return GdKeywords.STR
                } else if (elementType == GdTypes.STRING) {
                    return GdKeywords.STR
                } else if (elementType == GdTypes.STRING_NAME) {
                    return GdKeywords.STR_NAME
                } else if (elementType == GdTypes.NODE_PATH) {
                    return GdKeywords.STR
                } else if (elementType == GdTypes.NODE_PATH_LIT) {
                    return GdKeywords.NODE_PATH
                } else if (elementType == GdTypes.REF_ID_NM) {
                    if (text == GdKeywords.SELF) {
                        return GdClassUtil.getOwningClassName(expr)
                    } else if (text == GdKeywords.SUPER) {
                        // TODO tohle může vrátit zanoření... :/ Losos.InnerClass -> nějak se to musí vyparsovat
                        return GdInheritanceUtil.getExtendedClassId(expr)
                    }

                    if (DumbService.isDumb(expr.project)) {
                        return ""
                    }

                    val named: GdNamedElement = expr.refIdNm ?: return ""
                    return when (val element =
                        GdClassMemberUtil.findDeclaration(named)) {
                        is GdClassVarDeclTl -> parseLoadedType(expr, element.returnType)
                        is GdVarDeclSt -> parseLoadedType(expr, element.returnType)
                        is GdConstDeclTl -> parseLoadedType(expr, element.returnType)
                        is GdConstDeclSt -> parseLoadedType(expr, element.returnType)
                        is GdMethodDeclTl -> {
                            if (PsiTreeUtil.nextVisibleLeaf(expr)?.elementType == GdTypes.LRBR)
                                parseLoadedType(expr, element.returnType)
                            else "Callable"
                        }
                        is GdParam -> parseLoadedType(expr, element.returnType)
                        is GdSignalDeclTl -> "Signal"
                        is GdEnumDeclTl -> "EnumDictionary"
                        is GdEnumValue -> GdKeywords.INT
                        is GdClassNaming -> text
                        is GdForSt -> {
                            if (element.typed != null) {
                                return fromTyped(element.typed)
                            }

                            return GdOperand.getReturnType(element.expr?.returnType ?: "", GdKeywords.INT, "[]", expr.project)
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

    // TODO unify with doc builder
    fun fromTyped(typed: GdTyped?): String {
        return typed?.text?.trim(':', ' ') ?: ""
    }

    fun getAttrOrCallParentClass(element: PsiElement): String? {
        if (element is GdRefIdNm
            && element.parent != null
            && element.parent is GdLiteralEx
        ) {
            val root = element.parent.parent ?: return null
            if (root is GdAttributeEx && element.parent.prevSibling != null) {
                return GdCommonUtil.returnType(root.firstChild)
            }
            if (root is GdCallEx && root.prevSibling != null && root.parent is GdAttributeEx) {
                return GdCommonUtil.returnType(root.parent.firstChild)
            }
        }

        return null
    }

    fun getAttrOrCallParentFile(element: PsiElement): PsiFile? {
        var className = getAttrOrCallParentClass(element) ?: return null
        if (className.startsWith("Array")) {
            className = "Array"
        }

        return GdClassNamingIndex.INSTANCE.get(className, element.project, GlobalSearchScope.allScope(element.project))
            .firstOrNull()?.containingFile
    }

    private fun fromTyped(typed: String): String {
        if (typed.startsWith("Array")) {
            return typed.substring(5).trim('[', ']')
        }

        return typed
    }

    private fun fromTyped(typed: GdTypedVal?): String {
        if (typed == null) return ""

        val main = typed.typeHintList.first().text
        if (main != "Array") {
            return main
        }

        return typed.typeHintList.last().text
    }

    private fun parseLoadedType(element: PsiElement, type: String): String {
        GdClassMemberUtil.listDeclarations(element, type, false, true, true, true)
            .firstOrNull()
            ?.let { if (it is PsiElement) return GdCommonUtil.returnType(it) }
        return type
    }

}
