package gdscript.psi

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import gdscript.psi.impl.GdClassDeclElementType
import gdscript.psi.impl.GdClassIdElementType
import gdscript.psi.impl.GdClassNamingElementType
import gdscript.psi.impl.GdInheritanceElementType
import gdscript.psi.utils.*
import gdscript.structureView.GdPresentationUtil

object GdPsiUtils {

    /** ClassName  */
    @JvmStatic fun getClassname(element: GdClassNaming): String = GdClassNamingElementType.getClassname(element)
    @JvmStatic fun getParentName(element: GdClassNaming): String = GdClassNamingElementType.getParentName(element)

    /** ClassDeclTl */
    @JvmStatic fun getName(element: GdClassDeclTl): String = GdClassUtil.getName(element)
    @JvmStatic fun getParentName(element: GdClassDeclTl): String = GdClassDeclElementType.getParentName(element)

    /** ClassId */
    @JvmStatic fun getClassId(element: GdClassNameNmi): String = GdClassIdElementType.getClassId(element)
    @JvmStatic fun getParentName(element: GdClassNameNmi): String? = GdClassIdElementType.getParentName(element)
    @JvmStatic fun isInner(element: GdClassNameNmi): Boolean = PsiGdClassUtil.isInner(element)

    /** Inheritance */
    @JvmStatic fun getInheritancePath(element: GdInheritance): String = GdInheritanceElementType.inheritancePath(element)
    @JvmStatic fun getPsiFile(element: GdInheritanceIdNm): PsiFile? = PsiGdInheritanceUtil.getPsiFile(element)
    @JvmStatic fun isClassName(element: GdInheritanceIdNm): Boolean = PsiGdInheritanceUtil.isClassName(element)

    /** Enum  */
    @JvmStatic fun getName(element: GdEnumDeclTl): String = GdEnumUtil.getName(element)
    @JvmStatic fun getValues(element: GdEnumDeclTl): HashMap<String, Long> = PsiGdEnumUtil.values(element)
    @JvmStatic fun getPresentation(element: GdEnumDeclTl): ItemPresentation = GdPresentationUtil.presentation(element)

    /** Const */
    @JvmStatic fun getPresentation(element: GdConstDeclTl): ItemPresentation = GdPresentationUtil.presentation(element)
    @JvmStatic fun getReturnType(element: GdConstDeclTl): String = PsiGdConstDeclUtil.getReturnType(element)
    @JvmStatic fun getName(element: GdConstDeclTl): String = GdConstDeclUtil.getName(element)

    /** Named */
    @JvmStatic fun getName(element: GdNamedElement): String = GdCommonUtil.getName(element)
    @JvmStatic fun setName(element: GdNamedElement, newName: String): PsiElement = GdCommonUtil.setName(element, newName)
    @JvmStatic fun getNameIdentifier(element: GdNamedIdElement): PsiElement = GdCommonUtil.getNameIdentifier(element)
    @JvmStatic fun setName(element: GdFile, newName: String): PsiElement = GdCommonUtil.setName(element, newName)

    /** Type hint */
    @JvmStatic fun getReturnType(element: GdTypedVal): String = GdTypedUtil.getReturnType(element)

    /** Class variable */
    @JvmStatic fun getName(element: GdClassVarDeclTl): String = GdClassVarUtil.getName(element)
    @JvmStatic fun getPresentation(element: GdClassVarDeclTl): ItemPresentation = GdPresentationUtil.presentation(element)
    @JvmStatic fun getReturnType(element: GdClassVarDeclTl): String = PsiGdClassVarUtil.getReturnType(element)
    @JvmStatic fun isAnnotated(element: GdClassVarDeclTl, annotator: String): Boolean = PsiGdClassVarUtil.isAnnotated(element, annotator)
    @JvmStatic fun isStatic(element: GdClassVarDeclTl): Boolean = PsiGdClassVarUtil.isStatic(element)

    /** Local variable */
    @JvmStatic fun getName(element: GdVarDeclSt): String = GdVarDeclStUtil.getName(element)
    @JvmStatic fun getReturnType(element: GdVarDeclSt): String = PsiGdLocalVarUtil.getReturnType(element)

    /** Local constant */
    @JvmStatic fun getName(element: GdConstDeclSt): String = GdConstDeclUtil.getName(element)
    @JvmStatic fun getReturnType(element: GdConstDeclSt): String = PsiGdLocalConstUtil.getReturnType(element)
    @JvmStatic fun getReturnExpr(element: GdConstDeclSt): PsiElement? = PsiGdLocalConstUtil.getReturnExpr(element)

    /** Method  */
    @JvmStatic fun isStatic(element: GdMethodDeclTl): Boolean = PsiGdMethodDeclUtil.isStatic(element)
    @JvmStatic fun isVariadic(element: GdMethodDeclTl): Boolean = PsiGdMethodDeclUtil.isVariadic(element)
    @JvmStatic fun getName(element: GdMethodDeclTl): String = GdMethodUtil.getName(element)
    @JvmStatic fun getPresentation(element: GdMethodDeclTl): ItemPresentation = GdPresentationUtil.presentation(element)
    @JvmStatic fun getReturnType(element: GdMethodDeclTl): String = PsiGdMethodDeclUtil.getReturnType(element)
    @JvmStatic fun getParameters(element: GdMethodDeclTl): LinkedHashMap<String, String?> = PsiGdMethodDeclUtil.getParameters(element)
    @JvmStatic fun isConstructor(element: GdMethodDeclTl): Boolean = PsiGdMethodDeclUtil.isConstructor(element)

    /** Method param */
    @JvmStatic fun getReturnType(element: GdParam): String = PsiGdMethodDeclUtil.getReturnType(element)

    /** Signal */
    @JvmStatic fun getName(element: GdSignalDeclTl): String = GdSignalUtil.getName(element)
    @JvmStatic fun getParameters(element: GdSignalDeclTl): LinkedHashMap<String, String?> = PsiGdSignalUtil.getParameters(element)

    /** Statements */
    @JvmStatic fun getType(element: GdFlowSt): String = GdStmtUtil.getType(element)

    /** Expressions */
    @JvmStatic fun getReturnType(element: GdExpr): String = PsiGdExprUtil.getReturnType(element)
    @JvmStatic fun getReturnTypeOrRes(element: GdExpr, allowResource: Boolean = false): String = PsiGdExprUtil.getReturnType(element, allowResource)
    @JvmStatic fun getReturnType(element: GdArgExpr): String = PsiGdExprUtil.getReturnType(element.expr)

    /** Lambdas */
    @JvmStatic fun getReturnType(element: GdFuncDeclEx): String = PsiGdLocalFuncUtil.getReturnType(element)
    // TODO remove?
    @JvmStatic fun getReturnExpr(element: GdFuncDeclEx): PsiElement? = PsiGdLocalFuncUtil.getReturnExpr(element)
    @JvmStatic fun getParameters(element: GdFuncDeclEx): LinkedHashMap<String, String?> = PsiGdLocalFuncUtil.getParameters(element)

}
