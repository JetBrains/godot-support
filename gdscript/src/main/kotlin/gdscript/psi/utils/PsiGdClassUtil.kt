package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.*
import gdscript.psi.GdElementFactory
import gdscript.psi.impl.GdClassNamingElementType

object PsiGdClassUtil {

    fun getName(element: GdClassNameNmi?): String {
        if (element == null) return "";

        val valueNode = element.node.findChildByType(GdTypes.IDENTIFIER)
        return valueNode?.text ?: ""
    }

    fun isInner(element: GdClassNameNmi): Boolean {
        return element.parent is GdClassDeclTl;
    }

    @Deprecated("getParentClassElement")
    fun getParentContainer(element: PsiElement): PsiElement {
        if (element is GdClassDeclTl) {
            return element;
        }

        return PsiTreeUtil.getStubOrPsiParentOfType(element, GdClassDeclTl::class.java) ?: element.containingFile;
    }

    /**
     * Returns one of: GdClassDecl, GdClassNaming, GdFile
     */
    fun getParentClassElement(element: PsiElement): PsiElement {
        val inner = PsiTreeUtil.getStubOrPsiParentOfType(element, GdClassDeclTl::class.java);
        if (inner != null) return inner;

        return PsiTreeUtil.getStubChildOfType(element.containingFile, GdClassNaming::class.java)
            ?: element.containingFile;
    }

    fun getClass(element: PsiElement): String? {
        val container = getParentContainer(element);
        if (container is GdClassDeclTl) {
            return container.classNameNmi?.name;
        }

        return PsiTreeUtil.getStubChildOfType(element, GdClassNaming::class.java)?.classname;
    }

}
