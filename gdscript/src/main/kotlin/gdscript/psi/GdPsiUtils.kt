package gdscript.psi

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import gdscript.psi.impl.*
import gdscript.psi.utils.*
import gdscript.structureView.GdPresentationUtil

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
    fun getClassname(element: GdClassNaming?): String {
        return GdClassNamingElementType.getClassname(element);
    }
    @JvmStatic
    fun getParentName(element: GdClassNaming?): String? {
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
    fun getPresentation(element: GdConstDeclTl): ItemPresentation {
        return GdPresentationUtil.presentation(element)
    }
    @JvmStatic
    fun getReturnType(element: GdConstDeclTl): String {
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
    fun getName(element: GdTypeHintNm): String {
        return PsiGdTypeHintUtil.getName(element)
    }
    @JvmStatic
    fun setName(element: GdTypeHintNm, newName: String?): PsiElement {
        return PsiGdTypeHintUtil.setName(element, newName)
    }

    /** Class variable  */
    @JvmStatic
    fun getName(element: GdClassVarDeclTl): String? {
        return PsiGdClassVarUtil.getName(element)
    }
    @JvmStatic
    fun getPresentation(element: GdClassVarDeclTl): ItemPresentation {
        return GdPresentationUtil.presentation(element)
    }
    @JvmStatic
    fun getReturnType(element: GdClassVarDeclTl): String {
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
    fun getName(element: GdMethodDeclTl): String? {
        return PsiGdMethodDeclUtil.getMethodName(element)
    }
    @JvmStatic
    fun getPresentation(element: GdMethodDeclTl): ItemPresentation {
        return GdPresentationUtil.presentation(element)
    }
    @JvmStatic
    fun getReturnType(element: GdMethodDeclTl): String {
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
    @JvmStatic
    fun isConstructor(element: GdMethodDeclTl): Boolean {
        return PsiGdMethodDeclUtil.isConstructor(element)
    }

    /** Reference expression   */
    @JvmStatic
    fun getName(element: GdRefIdNm): String {
        return PsiGdRefIdUtil.getName(element)
    }
    @JvmStatic
    fun setName(element: GdRefIdNm, newName: String?): PsiElement {
        return PsiGdRefIdUtil.setName(element, newName)
    }

    /** Attribute expression   */
    @JvmStatic
    fun getName(element: GdAttExNm): String {
        return PsiGdAttExUtil.getName(element)
    }
    @JvmStatic
    fun setName(element: GdAttExNm, newName: String?): PsiElement {
        return PsiGdAttExUtil.setName(element, newName)
    }

    /** Signal   */
    @JvmStatic
    fun getName(element: GdSignalDeclTl): String {
        return PsiGdSignalUtil.getName(element)
    }
    @JvmStatic
    fun getParameters(element: GdSignalDeclTl): Array<String> {
        return PsiGdSignalUtil.getParameters(element)
    }
    @JvmStatic
    fun getName(element: GdSignalIdNmi): String {
        return PsiGdSignalUtil.getName(element)
    }
    @JvmStatic
    fun setName(element: GdSignalIdNmi, newName: String?): PsiElement {
        return PsiGdSignalUtil.setName(element, newName)
    }
    @JvmStatic
    fun getNameIdentifier(element: GdSignalIdNmi): PsiElement? {
        return PsiGdSignalUtil.getNameIdentifier(element)
    }

    /** Local variable   */
    @JvmStatic
    fun getName(element: GdVarNmi): String {
        return PsiGdVarNmiUtil.getName(element)
    }
    @JvmStatic
    fun setName(element: GdVarNmi, newName: String?): PsiElement {
        return PsiGdVarNmiUtil.setName(element, newName)
    }
    @JvmStatic
    fun getNameIdentifier(element: GdVarNmi): PsiElement? {
        return PsiGdVarNmiUtil.getNameIdentifier(element)
    }

}
