package gdscript.psi

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import gdscript.psi.impl.*
import gdscript.psi.utils.*
import gdscript.structureView.GdPresentationUtil

object GdPsiUtils {

    // TODO ii ??
    fun returnType(element: PsiElement?): String {
        return when(element) {
            is GdConstDeclTl -> element.returnType;
            is GdClassVarDeclTl -> element.returnType;
            is GdVarDeclSt -> element.returnType;
            is GdConstDeclSt -> element.returnType;
            is GdMethodDeclTl -> element.returnType;
            is GdExpr -> element.returnType;
            else -> "";
        }
    }

    // TODO projít vše, co není z elementType? a překopat

    /** ClassName  */
    @JvmStatic fun getName(element: GdClassNameNmi?): String = PsiGdClassUtil.getName(element);
    @JvmStatic fun getClassname(element: GdClassNaming): String = GdClassNamingElementType.getClassname(element);
    @JvmStatic fun getParentName(element: GdClassNaming): String = GdClassNamingElementType.getParentName(element);

    /** ClassDeclTl */
    @JvmStatic fun getName(element: GdClassDeclTl): String = GdClassUtil.getName(element);
    @JvmStatic fun getParentName(element: GdClassDeclTl): String = GdClassDeclElementType.getParentName(element);

    /** ClassId */
    @JvmStatic fun getClassId(element: GdClassNameNmi): String = GdClassIdElementType.getClassId(element);
    @JvmStatic fun getParentName(element: GdClassNameNmi): String? = GdClassIdElementType.getParentName(element);
    @JvmStatic fun isInner(element: GdClassNameNmi): Boolean = PsiGdClassUtil.isInner(element);

    /** Inheritance */
    @JvmStatic fun getInheritancePath(element: GdInheritance): String = GdInheritanceElementType.inheritancePath(element);
    @JvmStatic fun getPsiFile(element: GdInheritanceIdNm): PsiFile? = PsiGdInheritanceUtil.getPsiFile(element);
    @JvmStatic fun isClassName(element: GdInheritanceIdNm): Boolean = PsiGdInheritanceUtil.isClassName(element);

    /** Enum  */
    @JvmStatic fun getName(element: GdEnumDeclTl): String = GdEnumUtil.getName(element);
    @JvmStatic fun getValues(element: GdEnumDeclTl): HashMap<String, Int> = PsiGdEnumUtil.values(element);
    @JvmStatic fun getPresentation(element: GdEnumDeclTl): ItemPresentation = GdPresentationUtil.presentation(element);

    /** Const */
    @JvmStatic fun getPresentation(element: GdConstDeclTl): ItemPresentation = GdPresentationUtil.presentation(element);
    @JvmStatic fun getReturnType(element: GdConstDeclTl): String = PsiGdConstDeclUtil.getReturnType(element);
    @JvmStatic fun getName(element: GdConstDeclTl): String = GdConstDeclUtil.getName(element);

    /** Named */
    @JvmStatic fun getName(element: GdNamedElement): String = GdCommonUtil.getName(element);
    @JvmStatic fun setName(element: GdNamedElement, newName: String): PsiElement = GdCommonUtil.setName(element, newName);
    @JvmStatic fun getNameIdentifier(element: GdNamedIdElement): PsiElement? = PsiGdNamedUtil.getNameIdentifier(element);

    /** Type hint */
    @JvmStatic fun getName(element: GdTypeHintNm): String = PsiGdTypeHintUtil.getName(element);
    @JvmStatic fun getReturnType(element: GdTypedVal): String = GdTypedUtil.getReturnType(element);

    /** Class variable */
    @JvmStatic fun getName(element: GdClassVarDeclTl): String = GdClassVarUtil.getName(element);
    @JvmStatic fun getPresentation(element: GdClassVarDeclTl): ItemPresentation = GdPresentationUtil.presentation(element);
    @JvmStatic fun getReturnType(element: GdClassVarDeclTl): String = PsiGdClassVarUtil.getReturnType(element);

    /** Local variable */
    @JvmStatic fun getName(element: GdVarDeclSt): String = GdVarDeclStUtil.getName(element);
    @JvmStatic fun getReturnType(element: GdVarDeclSt): String = PsiGdLocalVarUtil.getReturnType(element);

    /** Local constant */
    @JvmStatic fun getName(element: GdConstDeclSt): String = PsiGdLocalConstUtil.getName(element);
    @JvmStatic fun getReturnType(element: GdConstDeclSt): String = PsiGdLocalConstUtil.getReturnType(element);

    /** Method  */
    @JvmStatic fun isStatic(element: GdMethodDeclTl): Boolean = PsiGdMethodDeclUtil.isStatic(element);
    @JvmStatic fun isVariadic(element: GdMethodDeclTl): Boolean = PsiGdMethodDeclUtil.isVariadic(element);
    @JvmStatic fun getName(element: GdMethodDeclTl): String = GdMethodUtil.getName(element);
    @JvmStatic fun getPresentation(element: GdMethodDeclTl): ItemPresentation = GdPresentationUtil.presentation(element);
    @JvmStatic fun getReturnType(element: GdMethodDeclTl): String = PsiGdMethodDeclUtil.getReturnType(element);
    @JvmStatic fun getParameters(element: GdMethodDeclTl): HashMap<String, String?> = PsiGdMethodDeclUtil.getParameters(element);
    @JvmStatic fun isConstructor(element: GdMethodDeclTl): Boolean = PsiGdMethodDeclUtil.isConstructor(element);

    /** Method param */
    @JvmStatic fun getReturnType(element: GdParam): String = PsiGdMethodDeclUtil.getReturnType(element);

    /** Signal */
    @JvmStatic fun getName(element: GdSignalDeclTl): String = PsiGdSignalUtil.getName(element);
    @JvmStatic fun getParameters(element: GdSignalDeclTl): Array<String> = PsiGdSignalUtil.getParameters(element);

    /** Statements */
    @JvmStatic fun getType(element: GdFlowSt): String = GdStmtUtil.getType(element);

    /** Expressions */
    @JvmStatic fun getReturnType(element: GdExpr): String = PsiGdExprUtil.getReturnType(element);

    /** Lambdas */
    @JvmStatic fun getReturnType(element: GdFuncDeclEx): String = PsiGdLocalFuncUtil.getReturnType(element);
    @JvmStatic fun getParameters(element: GdFuncDeclEx): HashMap<String, String?> = PsiGdLocalFuncUtil.getParameters(element);

}
