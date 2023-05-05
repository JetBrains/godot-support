package gdscript.codeInsight.documentation

import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import gdscript.codeInsight.GdDocumentationProvider
import gdscript.completion.utils.GdEnumCompletionUtil.preview
import gdscript.completion.utils.GdMethodCompletionUtil.methodHeader
import gdscript.completion.utils.GdMethodCompletionUtil.shortMethodHeader
import gdscript.psi.*
import gdscript.psi.utils.*
import gdscript.psi.utils.GdClassMemberUtil.constants
import gdscript.psi.utils.GdClassMemberUtil.enums
import gdscript.psi.utils.GdClassMemberUtil.methods
import gdscript.psi.utils.GdClassMemberUtil.signals
import gdscript.psi.utils.GdClassMemberUtil.variables
import gdscript.psi.utils.GdCommentUtil.briefDescriptionBlock
import gdscript.psi.utils.GdCommentUtil.descriptionBlock
import gdscript.psi.utils.GdCommentUtil.descriptionText
import gdscript.psi.utils.GdCommentUtil.parameterBlock
import gdscript.psi.utils.GdCommentUtil.returnBlock
import gdscript.psi.utils.GdCommentUtil.tutorialBlock

object GdDocFactory {

    fun create(element: PsiElement, fullDoc: Boolean = false): String? {
        return when (element) {
            is GdVarNmi -> variable(element, fullDoc)

            is GdMethodIdNmi,
            is GdFuncDeclIdNmi,
            -> method(element, fullDoc)

            is GdEnumDeclNmi,
            is GdEnumValueNmi,
            -> enum(element, fullDoc)

            is GdClassNameNmi,
            is PsiFile,
            -> classOrFile(element, fullDoc)

            else -> null
        }
    }

    private fun method(element: PsiElement, fullDoc: Boolean): String {
        val builder = GdDocBuilder(element)
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

        val descriptions = GdCommentUtil.collectAllDescriptions(declaration)
        if (fullDoc) {
            builder.addBodyBlock(
                    descriptions.descriptionBlock(),
                    descriptions.parameterBlock(),
                    descriptions.returnBlock(),
            )
        } else {
            builder.addBodyBlock(descriptions.briefDescriptionBlock())
        }

        return builder.toString()
    }

    private fun variable(element: GdVarNmi, fullDoc: Boolean): String? {
        val builder = GdDocBuilder(element)
        val withType = { el: PsiElement ->
            val returnType = GdCommonUtil.returnType(el)
            if (returnType.isBlank()) ""
            else ": $returnType"
        }

        val annotations = annotationPreview(element.parent)
        when (val owner = element.parent) {
            is GdConstDeclTl -> {
                builder.withOwner(element)
                builder.withPreview("${annotations}const ${element.name}${withType(owner)}")
            }

            is GdClassVarDeclTl -> {
                builder.withOwner(element)
                builder.withPreview("${annotations}var ${element.name}${withType(owner)}")
            }

            is GdVarDeclSt -> builder.withPreview("${annotations}var ${element.name}${withType(owner)}")
            is GdConstDeclSt -> builder.withPreview("${annotations}const ${element.name}${withType(owner)}")
            is GdSetDecl,
            is GdParam,
            -> builder.withPreview("${annotations}var ${element.name}${withType(owner)}")

            is GdForSt -> builder.withPreview("${annotations}var ${element.name}${withType(owner)}")
            is GdBindingPattern -> builder.withPreview("${annotations}var ${element.name}")
            else -> return null
        }

        val descriptions = GdCommentUtil.collectAllDescriptions(element.parent)
        if (fullDoc) {
            builder.addBodyBlock(
                    descriptions.descriptionBlock(),
                    descriptions.returnBlock(),
            )
        } else {
            builder.addBodyBlock(descriptions.briefDescriptionBlock())
        }

        return builder.toString()
    }

    private fun classOrFile(element: PsiElement, fullDoc: Boolean): String {
        val builder = GdDocBuilder(element)
                .withPackage(element)

        val parent = GdInheritanceUtil.getExtendedClassId(element)
        val extendInfo = if (parent.isNotBlank()) " extends $parent" else ""
        if (element is GdClassNameNmi) {
            builder.withPreview("class ${element.classId}${extendInfo}")
        } else if (extendInfo.isNotBlank()) {
            builder.withPreview(extendInfo)
        }

        val descriptions = GdCommentUtil.collectAllDescriptions(element.parent)
        if (fullDoc) {
            builder.addBodyBlock(descriptions.descriptionBlock())
            builder.addBodyBlock(descriptions.tutorialBlock())
            appendProperties(builder, GdClassUtil.getOwningClassElement(element))
        } else {
            builder.addBodyBlock(descriptions.briefDescriptionBlock())
        }

        return builder.toString()
    }

    /**
     * @param element GdEnumDeclNmi|GdEnumValueNmi
     */
    private fun enum(element: PsiElement, fullDoc: Boolean): String {
        val declaration = PsiTreeUtil.getParentOfType(element, GdEnumDeclTl::class.java) ?: return ""
        val builder = GdDocBuilder(element)
                .withPackage(element)
                .withOwner(element)
                .withPreview(annotationPreview(declaration) + declaration.preview())

        val descriptions = GdCommentUtil.collectAllDescriptions(declaration)
        if (fullDoc) {
            builder.addBodyBlock(descriptions.descriptionBlock())
        } else {
            builder.addBodyBlock(descriptions.briefDescriptionBlock())
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

        val variables = declarations.variables()
        builder.addBodyBlock(GdDocUtil.propertyTable("variables", variables.map {
            Pair(GdDocUtil.elementLink(it.returnType), GdDocUtil.elementLink(it.name))
        }))

        val methods = mutableListOf<GdMethodDeclTl>()
        val constructors = mutableListOf<GdMethodDeclTl>()
        declarations.methods().forEach {
            if (it.isConstructor) constructors.add(it)
            else methods.add(it)
        }

        builder.addBodyBlock(GdDocUtil.propertyTable("constructors", constructors.map {
            Pair(GdDocUtil.elementLink(it.returnType), GdDocUtil.elementLink(it.name, it.shortMethodHeader()))
        }))
        builder.addBodyBlock(GdDocUtil.propertyTable("methods", methods.map {
            Pair(GdDocUtil.elementLink(it.returnType), GdDocUtil.elementLink(it.name, it.shortMethodHeader()))
        }))

        // TODO operators

        val signals = declarations.signals()
        builder.addBodyBlock(GdDocUtil.descriptionListTable("signals", signals.map {
            var name = it.name
            if (!name.endsWith(")")) name += "()"
            Pair(
                    GdDocUtil.elementLink(name.substringBefore("("), name),
                    HtmlChunk.raw(GdCommentUtil.collectAllDescriptions(it).descriptionText()),
            )
        }))

        val enums = arrayOf(
                *declarations.enums(),
                *declarations.filterIsInstance<GdEnumValue>().map { it.parent as GdEnumDeclTl }.distinct().toTypedArray()
        )

        builder.addBodyBlock(GdDocUtil.descriptionListsTable("enums", enums.map {
            var name = it.name
            var isNamed = true
            if (name.isBlank()) {
                isNamed = false
                name = GdCommentUtil.collectAllDescriptions(it)[GdCommentUtil.ENUM]?.firstOrNull() ?: ""
            }

            Pair(
                    HtmlChunk.fragment(
                            HtmlChunk.text("enum "),
                            if (isNamed) GdDocUtil.elementLink(it.name) else HtmlChunk.text(name),
                            GdDocUtil.appendDescription(it),
                    ),
                    it.enumValueList.map { value ->
                        val enumValueName = value.enumValueNmi.name
                        val enumValue = it.values[enumValueName]
                        HtmlChunk.fragment(
                                if (isNamed) GdDocUtil.elementLink("${GdDocumentationProvider.LINK_ENUM_VALUE}:$name.$enumValueName", enumValueName)
                                else GdDocUtil.elementLink(enumValueName),
                                DocumentationMarkup.GRAYED_ELEMENT.addText(" = $enumValue"),
                                GdDocUtil.appendDescription(value),
                        )
                    }
            )
        }))

        val consts = declarations.constants()
        builder.addBodyBlock(GdDocUtil.descriptionListTable("constants", consts.map {
            Pair(
                    GdDocUtil.elementLink(it.name, it.text.trim()),
                    HtmlChunk.raw(GdCommentUtil.collectAllDescriptions(it).descriptionText()),
            )
        }))

        // TODO variables & methods with descriptions
    }

}
