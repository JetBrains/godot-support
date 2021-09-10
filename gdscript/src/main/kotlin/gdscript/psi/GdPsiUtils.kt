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

    /** Enum  */
    @JvmStatic fun getValues(element: GdEnumDeclTl): HashMap<String, Int> = PsiGdEnumUtil.values(element);
    @JvmStatic fun getPresentation(element: GdEnumDeclTl): ItemPresentation = GdPresentationUtil.presentation(element);

    /** Const   */
    @JvmStatic fun getPresentation(element: GdConstDeclTl): ItemPresentation = GdPresentationUtil.presentation(element);
    @JvmStatic fun getReturnType(element: GdConstDeclTl): String = PsiGdConstDeclUtil.getReturnType(element);
    @JvmStatic fun getConstName(element: GdConstDeclTl): String? = PsiGdConstDeclUtil.getConstName(element);

    /** Named   */
    @JvmStatic fun getName(element: GdNamedElement): String = PsiGdNamedUtil.getName(element);
    @JvmStatic fun setName(element: GdNamedElement, newName: String?): PsiElement = PsiGdNamedUtil.setName(element, newName);
    @JvmStatic fun getNameIdentifier(element: GdNamedIdElement): PsiElement? = PsiGdNamedUtil.getNameIdentifier(element);

    /** Type hint   */
    @JvmStatic fun getName(element: GdTypeHintNm): String = PsiGdTypeHintUtil.getName(element);
    @JvmStatic fun setName(element: GdTypeHintNm, newName: String?): PsiElement = PsiGdTypeHintUtil.setName(element, newName);

    /** Class variable  */
    @JvmStatic fun getName(element: GdClassVarDeclTl): String? = PsiGdClassVarUtil.getName(element);
    @JvmStatic fun getPresentation(element: GdClassVarDeclTl): ItemPresentation = GdPresentationUtil.presentation(element);
    @JvmStatic fun getReturnType(element: GdClassVarDeclTl): String = PsiGdClassVarUtil.getReturnType(element);

    /** Method  */
    @JvmStatic fun getName(element: GdMethodDeclTl): String? = PsiGdMethodDeclUtil.getMethodName(element);
    @JvmStatic fun getPresentation(element: GdMethodDeclTl): ItemPresentation = GdPresentationUtil.presentation(element);
    @JvmStatic fun getReturnType(element: GdMethodDeclTl): String = PsiGdMethodDeclUtil.getReturnType(element);
    @JvmStatic fun getParameters(element: GdMethodDeclTl): HashMap<String, String?> = PsiGdMethodDeclUtil.getParameters(element);
    @JvmStatic fun isConstructor(element: GdMethodDeclTl): Boolean = PsiGdMethodDeclUtil.isConstructor(element);

    /** Signal   */
    @JvmStatic fun getName(element: GdSignalDeclTl): String = PsiGdSignalUtil.getName(element);
    @JvmStatic fun getParameters(element: GdSignalDeclTl): Array<String> = PsiGdSignalUtil.getParameters(element);

    /** Expressions   */
    @JvmStatic fun getReturnType(element: GdExpr): String = PsiGdExprUtil.getReturnType(element);

}
