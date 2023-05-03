package gdscript.codeInsight.documentation

import com.intellij.psi.PsiElement
import gdscript.completion.utils.GdMethodCompletionUtil.methodHeader
import gdscript.psi.*
import gdscript.psi.utils.GdAnnotationUtil
import gdscript.psi.utils.GdCommonUtil

object GdDocFactory {

    fun create(element: PsiElement): String? {
        return when (element) {
            is GdVarNmi -> variable(element)
            is GdMethodIdNmi,
            is GdFuncDeclIdNmi -> method(element)

            else -> null
        }
    }

    private fun method(element: PsiElement): String {
        val builder = GdDocBuilder()
                .withOwner(element)
                .withPackage(element)

        val declaration = element.parent
        var code = annotationPreview(declaration)
        code += when (declaration) {
            is GdMethodDeclTl -> declaration.methodHeader(true)
            is GdFuncDeclEx -> declaration.methodHeader(true)
            else -> return ""
        }
        builder.withPreview(code)

        return builder.toString()
    }

    private fun variable(element: PsiElement): String? {
        val builder = GdDocBuilder()
                .withOwner(element)
                .withPackage(element)

        val owner = element.parent
        val code: String? = when (owner) {
            is GdConstDeclTl,
            is GdClassVarDeclTl,
            is GdVarDeclSt,
            is GdConstDeclSt -> {
                var code = annotationPreview(owner) + owner.text.trim()
                if (GdCommonUtil.typed(element) == null) GdCommonUtil.returnType(owner).let { if (it.isNotBlank()) code += ": $it" }
                code
            }

            is GdSetDecl -> null
            is GdParam -> null
            is GdForSt -> null
            is GdBindingPattern -> null
            else -> return null
        }
        builder.withPreview(code)

        return builder.toString()
    }

    private fun annotationPreview(element: PsiElement): String {
        val list = GdAnnotationUtil.collectPreceding(element)
        if (list.isEmpty()) return ""
        return list.asReversed().joinToString("\n") { it.text.trim() } + "\n"
    }

}
