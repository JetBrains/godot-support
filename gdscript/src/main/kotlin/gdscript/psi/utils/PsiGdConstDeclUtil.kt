package gdscript.psi.utils

import com.intellij.psi.PsiElement
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdConstDeclTl
import gdscript.psi.GdElementFactory
import gdscript.psi.GdTypes
import gdscript.psi.impl.GdConstIdNmiImpl

object PsiGdConstDeclUtil {

    fun getConstName(element: GdConstDeclTl): String? {
        val stub = element.stub;
        if (stub !== null) {
            return stub.name();
        }

        return element.constIdNmi?.name;
    }

    fun getReturnType(element: GdConstDeclTl): String {
        val stub = element.stub;
        if (stub !== null) {
            return stub.returnType();
        }

        return element.typed?.typeHintNm?.name ?: "";
    }

    fun setName(element: GdConstIdNmiImpl, newName: String?): PsiElement {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        if (keyNode != null) {
            val id = GdElementFactory.identifier(element.project, newName!!);
            element.node.replaceChild(keyNode, id.node);
        }

        return element;
    }

    fun getName(element: GdConstIdNmiImpl): String {
        val valueNode = element.node.findChildByType(GdTypes.IDENTIFIER);

        return valueNode?.text ?: "";
    }

    fun getNameIdentifier(element: GdConstIdNmiImpl): PsiElement? {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER);

        return keyNode?.psi;
    }

}
