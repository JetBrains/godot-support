package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdScriptBundle
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
            is GdSetMethodIdRef, is GdGetMethodIdRef -> methodExists(element, holder)
        }
    }

    private fun methodExists(element: PsiElement, holder: AnnotationHolder) {
        if (GdMethodDeclIndex.INSTANCE.getInFile(element).isNotEmpty()) return
        holder
            .newAnnotationGd(
                element.project,
                HighlightSeverity.ERROR,
                GdScriptBundle.message("annotator.method.does.not.exist", element.text)
            )
            .range(element.textRange)
            .withFix(if (element is GdSetMethodIdRef) setMethod(element) else getMethod(element as GdGetMethodIdRef))
            .create()
    }

    private fun setMethod(element: GdSetMethodIdRef): GdCreateMethodAction {
        val paramType = variableType(element)
        val param = "value${if (paramType.isNotBlank()) ": $paramType" else ""}"

        return GdCreateMethodAction(
            element.text,
            parameters = arrayOf(param),
            bodyLines = arrayOf("${variableName(element)} = value") // optional
        )
    }

    private fun getMethod(element: GdGetMethodIdRef): GdCreateMethodAction {
        return GdCreateMethodAction(
            element.text,
            returnType = variableType(element),
            bodyLines = arrayOf("return ${variableName(element)}") // optional
        )
    }

    private fun variableName(element: PsiElement): String? {
        return PsiTreeUtil.getParentOfType(element, GdClassVarDeclTl::class.java)?.name
    }

    private fun variableType(element: PsiElement): String {
        return PsiTreeUtil.getStubOrPsiParentOfType(element, GdClassVarDeclTl::class.java)?.returnType ?: ""
    }

}
