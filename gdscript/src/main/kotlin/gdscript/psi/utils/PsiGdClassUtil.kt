package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdClassNameNmi
import gdscript.psi.GdClassNaming
import gdscript.psi.GdTypes

object PsiGdClassUtil {

    fun getName(element: GdClassNameNmi?): String {
        if (element == null) return "";

        val valueNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        return valueNode?.text ?: ""
    }

    fun isInner(element: GdClassNameNmi): Boolean {
        return element.parent is GdClassDeclTl;
    }

    /**
     * Returns one of: GdClassDecl, GdClassNaming, GdFile
     */
    fun getParentClassElement(element: PsiElement): PsiElement {
        val inner = PsiTreeUtil.getStubOrPsiParentOfType(element, GdClassDeclTl::class.java)
        if (inner != null) return inner

        return PsiTreeUtil.getStubChildOfType(element.containingFile, GdClassNaming::class.java)
            ?: element.containingFile
    }

}
