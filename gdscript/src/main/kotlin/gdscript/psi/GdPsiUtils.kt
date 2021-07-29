package gdscript.psi

import com.intellij.psi.PsiElement
import gdscript.psi.impl.*
import gdscript.psi.utils.*

object GdPsiUtils {

    /** ClassName  */
    @JvmStatic
    fun getName(element: GdClassNameNm?): String? {
        return GdPsiClassNameUtil.getName(element)
    }
    @JvmStatic
    fun setName(element: GdClassNameNm?, newName: String?): PsiElement? {
        return GdPsiClassNameUtil.setName(element, newName)
    }
    @JvmStatic
    fun getClassname(element: GdClassNamingImpl?): String {
        return GdClassNamingElementType.getClassname(element);
    }
    @JvmStatic
    fun getParentName(element: GdClassNamingImpl?): String? {
        return GdClassNamingElementType.getParentName(element);
    }

    /** Inheritance  */
    @JvmStatic
    fun getInheritanceName(element: GdInheritance?): String? {
        return GdPsiClassNameUtil.getInheritanceName(element)
    }
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

    /** Const   */
    @JvmStatic
    fun getName(element: GdConstIdNmiImpl): String {
        return PsiGdConstDeclUtil.getName(element)
    }
    @JvmStatic
    fun getReturnType(element: GdConstDeclTl): String? {
        return PsiGdConstDeclUtil.getReturnType(element)
    }
    @JvmStatic
    fun setName(element: GdConstIdNmiImpl, newName: String?): PsiElement {
        return PsiGdConstDeclUtil.setName(element, newName)
    }
    @JvmStatic
    fun getNameIdentifier(element: GdConstIdNmiImpl): PsiElement? {
        return PsiGdConstDeclUtil.getNameIdentifier(element)
    }
    @JvmStatic
    fun getConstName(element: GdConstDeclTl): String? {
        return PsiGdConstDeclUtil.getConstName(element);
    }

    /** Type hint   */
    @JvmStatic
    fun getName(element: GdTypeHintNmImpl): String {
        return PsiGdTypeHintUtil.getName(element)
    }
    @JvmStatic
    fun setName(element: GdTypeHintNmImpl, newName: String?): PsiElement {
        return PsiGdTypeHintUtil.setName(element, newName)
    }

    /** Class variable  */
    @JvmStatic
    fun getVarName(element: GdClassVarDeclTl): String? {
        return PsiGdClassVarUtil.getVarName(element)
    }
    @JvmStatic
    fun getReturnType(element: GdClassVarDeclTl): String? {
        return PsiGdClassVarUtil.getReturnType(element)
    }
    @JvmStatic
    fun getName(element: GdClassVarIdNmi): String {
        return PsiGdClassVarUtil.getName(element)
    }
    @JvmStatic
    fun setName(element: GdClassVarIdNmi, newName: String?): PsiElement {
        return PsiGdClassVarUtil.setName(element, newName)
    }
    @JvmStatic
    fun getNameIdentifier(element: GdClassVarIdNmi): PsiElement? {
        return PsiGdClassVarUtil.getNameIdentifier(element)
    }
    @JvmStatic
    fun getName(element: GdSetMethodIdNm): String {
        return PsiGdClassVarUtil.getName(element)
    }
    @JvmStatic
    fun setName(element: GdSetMethodIdNm, newName: String?): PsiElement {
        return PsiGdClassVarUtil.setName(element, newName)
    }
    @JvmStatic
    fun getName(element: GdGetMethodIdNm): String {
        return PsiGdClassVarUtil.getName(element)
    }
    @JvmStatic
    fun setName(element: GdGetMethodIdNm, newName: String?): PsiElement {
        return PsiGdClassVarUtil.setName(element, newName)
    }

    /** Method  */
    @JvmStatic
    fun getMethodName(element: GdMethodDeclTl): String? {
        return PsiGdMethodDeclUtil.getMethodName(element)
    }
    @JvmStatic
    fun getReturnType(element: GdMethodDeclTl): String? {
        return PsiGdMethodDeclUtil.getReturnType(element)
    }
    @JvmStatic
    fun getParameters(element: GdMethodDeclTl): HashMap<String, String?> {
        return PsiGdMethodDeclUtil.getParameters(element)
    }
    @JvmStatic
    fun getName(element: GdMethodIdNmi): String {
        return PsiGdMethodDeclUtil.getName(element)
    }
    @JvmStatic
    fun setName(element: GdMethodIdNmi, newName: String?): PsiElement {
        return PsiGdMethodDeclUtil.setName(element, newName)
    }
    @JvmStatic
    fun getNameIdentifier(element: GdMethodIdNmi): PsiElement? {
        return PsiGdMethodDeclUtil.getNameIdentifier(element)
    }

}
