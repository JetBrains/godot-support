package gdscript.psi.utils

import com.intellij.psi.PsiElement
import gdscript.psi.*

object PsiGdMethodDeclUtil {

    fun getMethodName(element: GdMethodDeclTl): String? {
        val stub = element.stub;
        if (stub !== null) {
            return stub.name();
        }

        return element.methodIdNmi?.name;
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

}
