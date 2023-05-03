package gdscript.codeInsight.documentation

import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.psi.PsiElement
import gdscript.codeInsight.GdDocumentationUtil
import gdscript.completion.utils.GdMethodCompletionUtil.methodHeader
import gdscript.psi.*
import gdscript.psi.utils.*
import gdscript.psi.utils.GdClassMemberUtil.constants
import gdscript.psi.utils.GdClassMemberUtil.enums
import gdscript.psi.utils.GdClassMemberUtil.methods
import gdscript.psi.utils.GdClassMemberUtil.signals
import gdscript.psi.utils.GdClassMemberUtil.variables

object GdDocFactory {

    fun create(element: PsiElement, brief: Boolean = false): String? {
        return when (element) {
            is GdVarNmi -> variable(element)
            is GdMethodIdNmi,
            is GdFuncDeclIdNmi -> method(element)
            is GdClassNameNmi -> classId(element, brief)

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

    private fun classId(element: GdClassNameNmi, brief: Boolean): String {
        val builder = GdDocBuilder()
                .withPackage(element)

        val parent = GdInheritanceUtil.getExtendedClassId(element)
        val extendInfo = if (parent.isNotBlank()) " extends $parent" else ""
        builder.withPreview("class ${element.classId}${extendInfo}")

        if (!brief) {
            val classElement = GdClassUtil.getOwningClassElement(element)
            appendProperties(builder, classElement)
        }

        return builder.toString()
    }

    private fun annotationPreview(element: PsiElement): String {
        val list = GdAnnotationUtil.collectPreceding(element)
        if (list.isEmpty()) return ""
        return list.asReversed().joinToString("\n") { it.text.trim() } + "\n"
    }

    private fun appendProperties(builder: GdDocBuilder, ownerElement: PsiElement) {
        val declarations = GdClassMemberUtil.listClassMemberDeclarations(ownerElement, null, constructors = true)

        builder.addBodyBlock(
            HtmlChunk.div().children(
                DocumentationMarkup.SECTION_HEADER_CELL.addText("Params:"),
                HtmlChunk.br(),
                DocumentationMarkup.SECTION_CONTENT_CELL.addText("asd"),
                HtmlChunk.br(),
                DocumentationMarkup.SECTION_CONTENT_CELL.addText("qwe"),
            )
        )
        builder.addBodyBlock(
            HtmlChunk.div().children(
                DocumentationMarkup.SECTION_HEADER_CELL.addText("Methods:"),
                HtmlChunk.br(),
                DocumentationMarkup.SECTION_CONTENT_CELL.addText("asd"),
                HtmlChunk.br(),
                DocumentationMarkup.SECTION_CONTENT_CELL.addText("qwe"),
            )
        )

//        val variables = declarations.variables()
//        if (variables.isNotEmpty()) {
//            sb.append("<br />")
//            GdDocumentationUtil.paragraphHeader(sb, "Properties")
//            GdDocumentationUtil.propTable(sb, variables.map { arrayOf(it.returnType, it.name) })
//        }
//
//        val methods = mutableListOf<GdMethodDeclTl>()
//        val constructors = mutableListOf<GdMethodDeclTl>()
//
//        declarations.methods().forEach {
//            if (it.isConstructor) {
//                constructors.add(it)
//            } else {
//                methods.add(it)
//            }
//        }
//
//        if (constructors.isNotEmpty()) {
//            sb.append("<br />")
//            GdDocumentationUtil.paragraphHeader(sb, "Constructors")
//            GdDocumentationUtil.propTable(
//                sb,
//                constructors.map {
//                    arrayOf(it.returnType, String.format("%s(%s)", it.name, it.paramList?.text ?: ""))
//                })
//        }
//
//        if (methods.isNotEmpty()) {
//            sb.append("<br />")
//            GdDocumentationUtil.paragraphHeader(sb, "Methods")
//            GdDocumentationUtil.propTable(
//                sb,
//                methods.map {
//                    arrayOf(it.returnType, String.format("%s(%s)", it.name, it.paramList?.text ?: ""))
//                })
//        }
//
//        // TODO operators
//
//        val signals = declarations.signals()
//        if (signals.isNotEmpty()) {
//            sb.append("<br />")
//            GdDocumentationUtil.paragraphHeader(sb, "Signals")
//            GdDocumentationUtil.signalTable(sb, signals.map {
//                val comments = PsiGdCommentUtils.collectDescriptions(it)
//                var name = it.name
//                if (!name.endsWith(")")) name += "()"
//                arrayOf(name, *comments)
//            })
//        }
//
//        val enums = declarations.enums()
//        if (enums.isNotEmpty()) {
//            sb.append("<br />")
//            GdDocumentationUtil.paragraphHeader(sb, "Enums")
//            GdDocumentationUtil.enumTable(sb, enums.map {
//                val name = PsiGdCommentUtils.collectDescriptions(it, PsiGdCommentUtils.ENUM).firstOrNull() ?: "enum"
//                arrayOf(
//                    Pair(
//                        name,
//                        it.values.map { value -> Pair(value.key, value.value.toString()) }.toTypedArray()
//                    )
//                )
//            })
//        }
//
//        val consts = declarations.constants()
//        if (consts.isNotEmpty()) {
//            sb.append("<br />")
//            GdDocumentationUtil.paragraphHeader(sb, "Constants")
//            sb.append("<ul>")
//            consts.forEach {
//                sb.append("<li>")
//                sb.append(it.name)
//                sb.append(GdDocumentationUtil.grayText(" = ${it.expr?.text}"))
//
//                val descriptions = PsiGdCommentUtils.collectDescriptions(it)
//                if (descriptions.isNotEmpty()) {
//                    sb.append("<br /><a style=\"color: gray;\">")
//                    renderFullDoc(sb, descriptions)
//                    sb.append("</a>")
//                }
//                sb.append("</li>")
//            }
//            sb.append("</ul>")
//        }
    }

}
