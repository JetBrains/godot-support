package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import gdscript.GdKeywords
import gdscript.psi.GdArgExpr
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdClassNameNmi
import gdscript.psi.GdClassNaming
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdConstDeclSt
import gdscript.psi.GdConstDeclTl
import gdscript.psi.GdElementFactory
import gdscript.psi.GdEnumDeclNmi
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdEnumValue
import gdscript.psi.GdEnumValueNmi
import gdscript.psi.GdExpr
import gdscript.psi.GdForSt
import gdscript.psi.GdFuncDeclEx
import gdscript.psi.GdFuncDeclIdNmi
import gdscript.psi.GdInheritanceIdRef
import gdscript.psi.GdInheritanceSubIdRef
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdMethodIdNmi
import gdscript.psi.GdNamedElement
import gdscript.psi.GdNamedIdElement
import gdscript.psi.GdParam
import gdscript.psi.GdRefIdRef
import gdscript.psi.GdSetDecl
import gdscript.psi.GdSignalDeclTl
import gdscript.psi.GdSignalIdNmi
import gdscript.psi.GdStringValRef
import gdscript.psi.GdTyped
import gdscript.psi.GdTypedVal
import gdscript.psi.GdVarDeclSt
import gdscript.psi.GdVarNmi
import gdscript.utils.GdOperand

/**
 * Shared utils among Named and other elements
 */
object GdCommonUtil {

    fun getName(element: GdNamedElement): String {
        return element.text
    }

    fun getName(element: PsiElement): String {
        return when (element) {
            is GdNamedElement -> element.text
            is GdConstDeclTl -> element.name
            is GdClassVarDeclTl -> element.name
            is GdConstDeclSt -> element.name
            is GdVarDeclSt -> element.name
            else -> ""
        }
    }

    fun getNameIdentifier(element: GdNamedIdElement): PsiElement {
        return element.firstChild
    }

    fun setName(element: PsiNamedElement, newName: String): PsiElement {
        val project = element.project
        val keyNode = element.node.firstChildNode
        if (keyNode != null) {
            val id = when(element) {
                is GdClassNameNmi -> {
                    GdCfgUtil.renameValue(project, element.name, newName)
                    GdElementFactory.classNameNmi(project, newName)
                }
                is GdEnumDeclNmi -> GdElementFactory.enumDeclNmi(project, newName)
                is GdEnumValueNmi -> GdElementFactory.enumValueNmi(project, newName)
                is GdFuncDeclIdNmi -> GdElementFactory.funcDeclIdNmi(project, newName)
                is GdInheritanceIdRef -> GdElementFactory.inheritanceIdNm(project, newName)
                is GdInheritanceSubIdRef -> GdElementFactory.inheritanceSubIdNm(project, newName)
                is GdMethodIdNmi -> GdElementFactory.methodIdNmi(project, newName)
                is GdRefIdRef -> GdElementFactory.refIdNm(project, newName)
                is GdSignalIdNmi -> GdElementFactory.signalIdNmi(project, newName)
                is GdStringValRef -> GdElementFactory.typeStringVal(project, newName)
                is GdVarNmi -> GdElementFactory.varNmi(project, newName)
                else -> return element
            }
            element.node.replaceChild(keyNode, id.node)
        }

        return element
    }

    fun returnType(element: PsiElement?): String {
        return when(element) {
            is GdConstDeclTl -> element.returnType
            is GdClassVarDeclTl -> element.returnType
            is GdMethodDeclTl -> element.returnType
            is GdFuncDeclEx -> element.returnType
            is GdParam -> element.returnType
            is GdArgExpr -> element.returnType
            is GdVarDeclSt -> element.returnType
            is GdConstDeclSt -> element.returnType
            is GdExpr -> element.returnType
            is GdTypedVal -> element.returnType
            is GdClassNaming -> element.classname
            is GdClassDeclTl -> element.classNameNmi?.classId.orEmpty()
            is GdEnumDeclTl -> "EnumDictionary"
            is GdEnumValue -> GdKeywords.INT
            is GdSignalDeclTl -> "Signal"
            is GdForSt -> {
                if (element.typed != null) {
                    return element.typed?.text?.trim(':', ' ') ?: ""
                }

                val forExpr = element.expr?.returnType ?: ""
                if (forExpr.startsWith("Array")) {
                    return GdOperand.getReturnType(forExpr, GdKeywords.INT, "[]", element.project)
                } else if (forExpr.startsWith("Dictionary")) {
                    return GdOperand.getReturnType(forExpr, GdKeywords.VARIANT, "[]", element.project)
                } else {
                    return forExpr
                }
            }
            null -> return ""
            else -> throw NotImplementedError(element.toString())
        }
    }

    fun typed(element: PsiElement?): GdTyped? {
        return when(element) {
            is GdConstDeclTl -> element.typed
            is GdClassVarDeclTl -> element.typed
            is GdSetDecl -> element.typed
            is GdParam -> element.typed
            is GdVarDeclSt -> element.typed
            is GdConstDeclSt -> element.typed
            else -> null
        }
    }

}
