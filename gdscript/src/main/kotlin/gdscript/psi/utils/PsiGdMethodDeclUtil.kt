package gdscript.psi.utils

import com.intellij.psi.PsiElement
import gdscript.psi.GdElementFactory
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdMethodIdNmi
import gdscript.psi.GdTypes

object PsiGdMethodDeclUtil {

    fun getMethodName(element: GdMethodDeclTl): String? {
        val stub = element.stub;
        if (stub !== null) {
            return stub.name();
        }

        return element.methodIdNmi?.name;
    }

    fun getReturnType(element: GdMethodDeclTl): String {
        val stub = element.stub;
        if (stub !== null) {
            return stub.returnType();
        }

        return element.returnHint?.typeHintNm?.name ?: "";
    }

    fun getParameters(element: GdMethodDeclTl): HashMap<String, String?> {
        val stub = element.stub;
        if (stub !== null) {
            return stub.parameters();
        }

        return PsiGdParameterUtil.toHashMap(element.paramList)
    }

    fun setName(element: GdMethodIdNmi, newName: String?): PsiElement {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        if (keyNode != null) {
            val id = GdElementFactory.identifier(element.project, newName!!);
            element.node.replaceChild(keyNode, id.node);
        }

        return element;
    }

    fun getName(element: GdMethodIdNmi): String {
        val valueNode = element.node.findChildByType(GdTypes.IDENTIFIER);

        return valueNode?.text ?: "";
    }

    fun getNameIdentifier(element: GdMethodIdNmi): PsiElement? {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER);

        return keyNode?.psi;
    }

    fun isConstructor(element: GdMethodDeclTl): Boolean = element.name == "_init";

}
