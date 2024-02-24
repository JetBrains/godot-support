package tscn.psi.utils

import com.intellij.psi.PsiElement
import tscn.psi.TscnElementFactory
import tscn.psi.TscnHeaderValueVal
import tscn.psi.TscnNamedElement

object TscnCommonUtil {

    fun getName(element: PsiElement): String {
        return when (element) {
            is TscnNamedElement -> element.text
            else -> ""
        }
    }

    fun setName(element: PsiElement, newName: String): PsiElement {
        val keyNode = element.node.firstChildNode
        if (keyNode != null) {
            val id = when(element) {
                is TscnHeaderValueVal -> TscnElementFactory.tscnNodeHeaderValueVal(element.project, newName)
                else -> return element
            }
            element.node.replaceChild(keyNode, id.node)
        }

        return element
    }

}
