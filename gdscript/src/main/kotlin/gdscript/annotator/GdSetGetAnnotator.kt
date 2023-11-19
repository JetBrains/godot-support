package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.action.GdCreateMethodAction
import gdscript.index.impl.GdMethodDeclIndex
import gdscript.psi.*

/**
 * Checks if referencing method exists
 * and if not create quick-fix
 */
class GdSetGetAnnotator : Annotator {

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is GdSetMethodIdNm, is GdGetMethodIdNm -> methodExists(element as GdNamedElement, holder)
        }
    }

    private fun methodExists(element: GdNamedElement, holder: AnnotationHolder) {
        if (GdMethodDeclIndex.INSTANCE.getInFile(element).isNotEmpty()) return;
        holder
            .newAnnotation(HighlightSeverity.ERROR, "Method [${element.text}] does not exist")
            .range(element.textRange)
            .withFix(if (element is GdSetMethodIdNm) setMethod(element) else getMethod(element as GdGetMethodIdNm))
            .create()
    }

    private fun setMethod(element: GdSetMethodIdNm): GdCreateMethodAction {
        val paramType = variableType(element);
        val param = "value${if (paramType.isNotBlank()) ": $paramType" else ""}";

        return GdCreateMethodAction(
            element.name,
            parameters = arrayOf(param),
            bodyLines = arrayOf("${variableName(element)} = value") // optional ;
        );
    }

    private fun getMethod(element: GdGetMethodIdNm): GdCreateMethodAction {
        return GdCreateMethodAction(
            element.name,
            returnType = variableType(element),
            bodyLines = arrayOf("return ${variableName(element)}") // optional ;
        );
    }

    private fun variableName(element: PsiElement): String? {
        return PsiTreeUtil.getParentOfType(element, GdClassVarDeclTl::class.java)?.name;
    }

    private fun variableType(element: PsiElement): String {
        return PsiTreeUtil.getStubOrPsiParentOfType(element, GdClassVarDeclTl::class.java)?.returnType ?: "";
    }

}
