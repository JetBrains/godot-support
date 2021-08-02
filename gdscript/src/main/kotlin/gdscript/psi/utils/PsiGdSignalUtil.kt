package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiIdentifier
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdElementFactory
import gdscript.psi.GdSignalDeclTl
import gdscript.psi.GdSignalIdNmi
import gdscript.psi.GdTypes

object PsiGdSignalUtil {

    fun getName(element: GdSignalDeclTl): String {
        val stub = element.stub;
        if (stub !== null) {
            return stub.name();
        }

        return element.signalIdNmi?.name ?: "";
    }

    fun getParameters(element: GdSignalDeclTl): Array<String> {
        val stub = element.stub;
        if (stub !== null) {
            return stub.parameters();
        }

        return PsiTreeUtil.findChildrenOfType(element.signalParList, PsiIdentifier::class.java).map {
            it.text
        }.toTypedArray()
    }

    fun getName(element: GdSignalIdNmi): String {
        val valueNode = element.node.findChildByType(GdTypes.IDENTIFIER)

        return valueNode?.text ?: ""
    }

    fun setName(element: GdSignalIdNmi, newName: String?): PsiElement {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        if (keyNode != null) {
            val id = GdElementFactory.identifier(element.project, newName!!)
            element.node.replaceChild(keyNode, id.node)
        }
        return element
    }

    fun getNameIdentifier(element: GdSignalIdNmi): PsiElement? {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER);

        return keyNode?.psi;
    }

}