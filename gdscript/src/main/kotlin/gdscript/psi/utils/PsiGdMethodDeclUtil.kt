package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.*

object PsiGdMethodDeclUtil {

    fun getMethodName(element: GdMethodDeclTl): String? {
        val stub = element.stub;
        if (stub !== null) {
            return stub.name();
        }

        return element.methodIdNmi?.name;
    }

    fun getReturnType(element: GdMethodDeclTl): String? {
        val stub = element.stub;
        if (stub !== null) {
            return stub.returnType();
        }

        return element.returnHint?.typeHintNm?.name;
    }

    fun getParameters(element: GdMethodDeclTl): HashMap<String, String?> {
        val stub = element.stub;
        if (stub !== null) {
            return stub.parameters();
        }
        val params = HashMap<String, String?>();
        var child = element.paramList?.firstChild;
        while (child != null) {
            if (child is GdParam) {
                val id = child.firstChild?.text;
                params[id.orEmpty()] = child.typed?.typeHintNm?.name;
            }
            child = child.nextSibling;
        }

        return params;
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
