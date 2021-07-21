package gdscript.psi.impl

import com.intellij.psi.PsiElement
import gdscript.psi.GdClassNameNm
import gdscript.psi.GdClassNaming
import gdscript.psi.GdInheritance
import gdscript.psi.GdMethodDeclTl

object GdPsiUtils {

    /** GdPsiClassNameUtil  */
    @JvmStatic
    fun getName(element: GdClassNameNm?): String? {
        return GdPsiClassNameUtil.getName(element)
    }

    @JvmStatic
    fun setName(element: GdClassNameNm?, newName: String?): PsiElement? {
        return GdPsiClassNameUtil.setName(element, newName)
    }

    @JvmStatic
    fun getNameIdentifier(element: GdClassNameNm?): PsiElement? {
        return GdPsiClassNameUtil.getNameIdentifier(element)
    }

    @JvmStatic
    fun getParentClassName(element: GdInheritance?): String? {
        return GdPsiClassNameUtil.getParentClassName(element)
    }

    @JvmStatic
    fun getClassname(element: GdClassNaming?): String {
        return GdClassNamingElementType.getClassname(element);
    }

    /** GdPsiMethodUtil  */
    @JvmStatic
    fun getMethodName(element: GdMethodDeclTl?): String? {
        return GdPsiMethodUtil.getMethodName(element)
    }

}
