package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import gdscript.psi.*

/**
 * Shared utils among Named and other elements
 */
object GdCommonUtil {

    fun getName(element: GdNamedElement): String {
        return element.text;
    }

    fun setName(element: PsiNamedElement, newName: String): PsiElement {
        val keyNode = element.node.firstChildNode;
        if (keyNode != null) {
            val id = when(element) {
                is GdClassVarIdNmi -> GdElementFactory.classVarIdNmi(element.project, newName);
                is GdConstIdNmi -> GdElementFactory.constIdNmi(element.project, newName);
                is GdEnumDeclNmi -> GdElementFactory.enumDeclNmi(element.project, newName);
                is GdEnumValueNmi -> GdElementFactory.enumValueNmi(element.project, newName);
                is GdFuncDeclIdNmi -> GdElementFactory.funcDeclIdNmi(element.project, newName);
                is GdGetMethodIdNm -> GdElementFactory.getMethodIdNm(element.project, newName);
                is GdInheritanceIdNm -> GdElementFactory.inheritanceIdNm(element.project, newName);
                is GdInheritanceSubIdNm -> GdElementFactory.inheritanceSubIdNm(element.project, newName);
                is GdMethodIdNmi -> GdElementFactory.methodIdNmi(element.project, newName);
                is GdRefIdNm -> GdElementFactory.refIdNm(element.project, newName);
                is GdSetMethodIdNm -> GdElementFactory.setMethodIdNm(element.project, newName);
                is GdSignalIdNmi -> GdElementFactory.signalIdNmi(element.project, newName);
                is GdTypeHintArrayNm -> GdElementFactory.typeHintArrayNm(element.project, newName);
                is GdTypeHintNm -> GdElementFactory.typeHintNm(element.project, newName);
                is GdVarNmi -> GdElementFactory.varNmi(element.project, newName);
                is GdPreloadNm -> GdElementFactory.preloadNm(element.project, newName);
                else -> return element;
            }
            element.node.replaceChild(keyNode, id.node);
        }

        return element;
    }

}
