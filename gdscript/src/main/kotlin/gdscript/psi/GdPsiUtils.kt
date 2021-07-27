package gdscript.psi

import com.intellij.psi.PsiElement
import gdscript.psi.impl.GdClassNamingElementType
import gdscript.psi.impl.GdClassNamingImpl
import gdscript.psi.impl.GdPsiClassNameUtil
import gdscript.psi.impl.GdPsiMethodUtil
import gdscript.psi.utils.PsiGdInheritanceUtil

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
    fun getInheritanceName(element: GdInheritance?): String? {
        return GdPsiClassNameUtil.getInheritanceName(element)
    }

    @JvmStatic
    fun getClassname(element: GdClassNamingImpl?): String {
        return GdClassNamingElementType.getClassname(element);
    }

    /** PsiGdInheritanceUtil  */
    @JvmStatic
    fun getName(element: GdInheritanceIdNmi): String {
        return PsiGdInheritanceUtil.getName(element)
    }
    @JvmStatic
    fun setName(element: GdInheritanceIdNmi, newName: String?): PsiElement {
        return PsiGdInheritanceUtil.setName(element, newName)
    }
    @JvmStatic
    fun getNameIdentifier(element: GdInheritanceIdNmi): PsiElement? {
        return PsiGdInheritanceUtil.getNameIdentifier(element)
    }

    /** GdPsiMethodUtil  */
    @JvmStatic
    fun getMethodName(element: GdMethodDeclTl?): String? {
        return GdPsiMethodUtil.getMethodName(element)
    }

}
