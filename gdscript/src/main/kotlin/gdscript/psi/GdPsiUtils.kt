package gdscript.psi

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import gdscript.psi.impl.*
import gdscript.psi.utils.*
import gdscript.structureView.GdPresentationUtil

object GdPsiUtils {

    /** ClassName  */
    @JvmStatic fun getName(element: GdClassNameNm?): String? = GdPsiClassNameUtil.getName(element);
    @JvmStatic fun setName(element: GdClassNameNm?, newName: String?): PsiElement? = GdPsiClassNameUtil.setName(element, newName);
    @JvmStatic fun getClassname(element: GdClassNaming?): String = GdClassNamingElementType.getClassname(element);
    @JvmStatic fun getParentName(element: GdClassNaming?): String? = GdClassNamingElementType.getParentName(element);

    /** Inheritance  */
    @JvmStatic fun getInheritanceName(element: GdInheritance?): String? = GdPsiClassNameUtil.getInheritanceName(element);
    @JvmStatic fun getName(element: GdInheritanceIdNmi): String = PsiGdInheritanceUtil.getName(element);
    @JvmStatic fun setName(element: GdInheritanceIdNmi, newName: String?): PsiElement = PsiGdInheritanceUtil.setName(element, newName);
    @JvmStatic fun getNameIdentifier(element: GdInheritanceIdNmi): PsiElement? = PsiGdInheritanceUtil.getNameIdentifier(element);

    /** Const   */
    @JvmStatic fun getName(element: GdConstIdNmiImpl): String = PsiGdConstDeclUtil.getName(element);
    @JvmStatic fun getPresentation(element: GdConstDeclTl): ItemPresentation = GdPresentationUtil.presentation(element);
    @JvmStatic fun getReturnType(element: GdConstDeclTl): String = PsiGdConstDeclUtil.getReturnType(element);
    @JvmStatic fun setName(element: GdConstIdNmiImpl, newName: String?): PsiElement = PsiGdConstDeclUtil.setName(element, newName);
    @JvmStatic fun getNameIdentifier(element: GdConstIdNmiImpl): PsiElement? = PsiGdConstDeclUtil.getNameIdentifier(element);
    @JvmStatic fun getConstName(element: GdConstDeclTl): String? = PsiGdConstDeclUtil.getConstName(element);

    /** Type hint   */
    @JvmStatic fun getName(element: GdTypeHintNm): String = PsiGdTypeHintUtil.getName(element);
    @JvmStatic fun setName(element: GdTypeHintNm, newName: String?): PsiElement = PsiGdTypeHintUtil.setName(element, newName);

    /** Class variable  */
    @JvmStatic fun getName(element: GdClassVarDeclTl): String? = PsiGdClassVarUtil.getName(element);
    @JvmStatic fun getPresentation(element: GdClassVarDeclTl): ItemPresentation = GdPresentationUtil.presentation(element);
    @JvmStatic fun getReturnType(element: GdClassVarDeclTl): String = PsiGdClassVarUtil.getReturnType(element);
    @JvmStatic fun getName(element: GdClassVarIdNmi): String = PsiGdClassVarUtil.getName(element);
    @JvmStatic fun setName(element: GdClassVarIdNmi, newName: String?): PsiElement = PsiGdClassVarUtil.setName(element, newName);
    @JvmStatic fun getNameIdentifier(element: GdClassVarIdNmi): PsiElement? = PsiGdClassVarUtil.getNameIdentifier(element);
    @JvmStatic fun getName(element: GdSetMethodIdNm): String = PsiGdClassVarUtil.getName(element);
    @JvmStatic fun setName(element: GdSetMethodIdNm, newName: String?): PsiElement = PsiGdClassVarUtil.setName(element, newName);
    @JvmStatic fun getName(element: GdGetMethodIdNm): String = PsiGdClassVarUtil.getName(element);
    @JvmStatic fun setName(element: GdGetMethodIdNm, newName: String?): PsiElement = PsiGdClassVarUtil.setName(element, newName);

    /** Method  */
    @JvmStatic fun getName(element: GdMethodDeclTl): String? = PsiGdMethodDeclUtil.getMethodName(element);
    @JvmStatic fun getPresentation(element: GdMethodDeclTl): ItemPresentation = GdPresentationUtil.presentation(element);
    @JvmStatic fun getReturnType(element: GdMethodDeclTl): String = PsiGdMethodDeclUtil.getReturnType(element);
    @JvmStatic fun getParameters(element: GdMethodDeclTl): HashMap<String, String?> = PsiGdMethodDeclUtil.getParameters(element);
    @JvmStatic fun getName(element: GdMethodIdNmi): String = PsiGdMethodDeclUtil.getName(element);
    @JvmStatic fun setName(element: GdMethodIdNmi, newName: String?): PsiElement = PsiGdMethodDeclUtil.setName(element, newName);
    @JvmStatic fun getNameIdentifier(element: GdMethodIdNmi): PsiElement? = PsiGdMethodDeclUtil.getNameIdentifier(element);
    @JvmStatic fun isConstructor(element: GdMethodDeclTl): Boolean = PsiGdMethodDeclUtil.isConstructor(element);

    /** Reference expression   */
    @JvmStatic fun getName(element: GdRefIdNm): String = PsiGdRefIdUtil.getName(element);
    @JvmStatic fun setName(element: GdRefIdNm, newName: String?): PsiElement = PsiGdRefIdUtil.setName(element, newName);

    /** Attribute expression   */
    @JvmStatic fun getName(element: GdAttExNm): String = PsiGdAttExUtil.getName(element);
    @JvmStatic fun setName(element: GdAttExNm, newName: String?): PsiElement = PsiGdAttExUtil.setName(element, newName);

    /** Signal   */
    @JvmStatic fun getName(element: GdSignalDeclTl): String = PsiGdSignalUtil.getName(element);
    @JvmStatic fun getParameters(element: GdSignalDeclTl): Array<String> = PsiGdSignalUtil.getParameters(element);
    @JvmStatic fun getName(element: GdSignalIdNmi): String = PsiGdSignalUtil.getName(element);
    @JvmStatic fun setName(element: GdSignalIdNmi, newName: String?): PsiElement = PsiGdSignalUtil.setName(element, newName);
    @JvmStatic fun getNameIdentifier(element: GdSignalIdNmi): PsiElement? = PsiGdSignalUtil.getNameIdentifier(element);

    /** Local variable   */
    @JvmStatic fun getName(element: GdVarNmi): String = PsiGdVarNmiUtil.getName(element);
    @JvmStatic fun setName(element: GdVarNmi, newName: String?): PsiElement = PsiGdVarNmiUtil.setName(element, newName);
    @JvmStatic fun getNameIdentifier(element: GdVarNmi): PsiElement? = PsiGdVarNmiUtil.getNameIdentifier(element);

    /** Expressions   */
    @JvmStatic fun getReturnType(element: GdExpr): String = PsiGdExprUtil.getReturnType(element);

}
