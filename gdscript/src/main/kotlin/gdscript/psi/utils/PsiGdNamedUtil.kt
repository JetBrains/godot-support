package gdscript.psi.utils

import com.intellij.psi.PsiElement
import gdscript.psi.*

object PsiGdNamedUtil {

    fun getNameIdentifier(element: GdNamedIdElement): PsiElement? {
        val keyNode = element.node.findChildByType(GdTypes.IDENTIFIER);
        return keyNode?.psi;
    }

}