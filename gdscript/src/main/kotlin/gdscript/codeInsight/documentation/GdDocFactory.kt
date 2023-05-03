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

    private fun variable(element: GdVarNmi): String? {
        val builder = GdDocBuilder()
        val withType = { el: PsiElement ->
            val returnType = GdCommonUtil.returnType(el)
            if (returnType.isBlank()) ""
            else ": $returnType"
        }

        when (val owner = element.parent) {
            is GdConstDeclTl -> {
                builder.withOwner(element)
                builder.withPreview("const ${element.name}${withType(owner)}")
            }

            is GdClassVarDeclTl -> {
                builder.withOwner(element)
                builder.withPreview(annotationPreview(owner) + "var ${element.name}${withType(owner)}")
            }

            is GdVarDeclSt -> builder.withPreview("var ${element.name}${withType(owner)}")
            is GdConstDeclSt -> builder.withPreview("const ${element.name}${withType(owner)}")
            is GdSetDecl,
            is GdParam -> builder.withPreview("var ${element.name}${withType(owner)}")
            is GdForSt -> builder.withPreview("var ${element.name}${withType(owner)}")
            is GdBindingPattern -> builder.withPreview("var ${element.name}")
            else -> return null
        }

        return builder.toString()
    }

    private fun annotationPreview(element: PsiElement): String {
        val list = GdAnnotationUtil.collectPreceding(element)
        if (list.isEmpty()) return ""
        return list.asReversed().joinToString("\n") { it.text.trim() } + "\n"
    }

}
