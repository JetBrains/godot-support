package gdscript.codeInsight.documentation

import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdFileType
import gdscript.codeInsight.GdDocumentationProvider
import gdscript.completion.utils.GdEnumCompletionUtil.preview
import gdscript.completion.utils.GdMethodCompletionUtil.methodHeader
import gdscript.completion.utils.GdMethodCompletionUtil.shortMethodHeader
import gdscript.index.impl.utils.GdFileResInputFilter
import gdscript.psi.*
import gdscript.psi.types.GdDocumented
import gdscript.psi.utils.*
import gdscript.psi.utils.GdClassMemberUtil.constants
import gdscript.psi.utils.GdClassMemberUtil.enums
import gdscript.psi.utils.GdClassMemberUtil.methods
import gdscript.psi.utils.GdClassMemberUtil.signals
import gdscript.psi.utils.GdClassMemberUtil.variables
import gdscript.utils.VirtualFileUtil.localParentPath
import gdscript.utils.VirtualFileUtil.resourcePath
import project.ProjectFileType
import tscn.TscnFileType

object GdDocFactory {

    fun create(element: Any?, fullDoc: Boolean = false): String? {
        return when (element) {
            is GdVarNmi -> variable(element, fullDoc)

            is GdMethodIdNmi,
            is GdFuncDeclIdNmi,
            -> method(element as PsiElement, fullDoc)

            is GdEnumDeclNmi,
            is GdEnumValueNmi,
            -> enum(element as PsiElement, fullDoc)

            is GdClassNameNmi,
            is PsiFile,
            -> classOrFile(element as PsiElement, fullDoc)

            is GdSignalIdNmi,
            -> signal(element, fullDoc)

            is PsiDirectory,
            -> directory(element, fullDoc)

            else -> null
        }
    }

    private fun method(element: PsiElement, fullDoc: Boolean): String {
        val builder = GdDocBuilder(element)
            .withOwner(element)
            .withPackage(element)

        val declaration = element.parent as GdDocumented
        var code = annotationPreview(declaration as PsiElement)
        code += when (declaration) {
            is GdMethodDeclTl -> declaration.methodHeader(true)
            is GdFuncDeclEx -> declaration.methodHeader(true)
            else -> return ""
        }

        builder.withPreview(code)

        if (fullDoc) {
            builder.addBodyBlock(GdDocUtil.paragraph(declaration.description()))
        } else {
            builder.addBodyBlock(GdDocUtil.paragraph(declaration.brief()))
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

        if (fullDoc) {
            builder.addBodyBlock(GdDocUtil.paragraph(GdCommentUtil.description(element.parent)))
        } else {
            builder.addBodyBlock(GdDocUtil.paragraph(GdCommentUtil.brief(element.parent)))
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

        val declaration = element.parent
        // TODO GdFile or GdClassNaming
        if (declaration is GdDocumented) {
            if (fullDoc) {
                builder.addBodyBlock(GdDocUtil.paragraph(declaration.description()))
                builder.addBodyBlock(GdDocUtil.listTable(
                    "tutorials",
                    declaration.tutorials().map {
                      HtmlChunk.link(it.url, it.name.removeSurrounding("(", ")"))
                    },
                ))
                appendProperties(builder, GdClassUtil.getOwningClassElement(element))
            } else {
                builder.addBodyBlock(GdDocUtil.paragraph(declaration.brief()))
            }
        }

        return builder.toString()
    }

    private fun directory(element: PsiDirectory, fillDoc: Boolean): String {
        val builder = GdDocBuilder()
        val currentPath = element.virtualFile.localParentPath()

        val scripts = mutableListOf<HtmlChunk>()
        val scenes = mutableListOf<HtmlChunk>()
        val others = mutableListOf<HtmlChunk>()

        element.files.forEach {
            val name = it.name
            when (it.fileType) {
                is GdFileType -> scripts.add(GdDocUtil.elementLink(it.virtualFile.resourcePath(), name))
                is TscnFileType -> scenes.add(HtmlChunk.text(name))
                is ProjectFileType -> {}
                else -> {
                    if (GdFileResInputFilter.validResource(name)) {
                        others.add(HtmlChunk.text(name))
                    }
                }
            }
        }

        val directories = element.subdirectories.mapNotNull {
            val name = it.name
            if (!name.startsWith("."))
                GdDocUtil.packageLink("$currentPath/$name", name)
            else null
        }

        builder.addBodyBlock(
            GdDocUtil.listTable("scripts", scripts),
            GdDocUtil.listTable("scenes", scenes),
            GdDocUtil.listTable("packages", directories),
            GdDocUtil.listTable("other", others),
        )

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

        if (fullDoc) {
            builder.addBodyBlock(GdDocUtil.paragraph(declaration.description()))
        } else {
            builder.addBodyBlock(GdDocUtil.paragraph(declaration.brief()))
        }

        return builder.toString()
    }

    /**
     * @param element GdSignalNmi
     */
    private fun signal(element: PsiElement, fullDoc: Boolean): String {
        val declaration = PsiTreeUtil.getParentOfType(element, GdSignalDeclTl::class.java) ?: return ""
        val builder = GdDocBuilder(element)
            .withPackage(element)
            .withOwner(element)
            .withPreview(declaration.text)

        if (fullDoc) {
            builder.addBodyBlock(GdDocUtil.paragraph(declaration.description()))
        } else {
            builder.addBodyBlock(GdDocUtil.paragraph(declaration.brief()))
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
                HtmlChunk.raw(GdGodotDocUtil.parseStyles(it.description())),
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
                name = GdCommentUtil.collectAllDescriptions(it)[GdCommentUtil.ENUM]?.firstOrNull() ?: "" // TODO
            }

            Pair(
                HtmlChunk.fragment(
                    HtmlChunk.text("enum "),
                    if (isNamed) GdDocUtil.elementLink(it.name) else HtmlChunk.text(name),
                    GdDocUtil.appendDescription(it)
                ),
                it.enumValueList.map { value ->
                    val enumValueName = value.enumValueNmi.name
                    val enumValue = it.values[enumValueName]
                    HtmlChunk.fragment(
                        if (isNamed) GdDocUtil.elementLink(
                            "${GdDocumentationProvider.LINK_ENUM_VALUE}:$name.$enumValueName",
                            enumValueName
                        )
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
                HtmlChunk.raw(GdGodotDocUtil.parseStyles(it.description())),
            )
        }))

        // TODO variables & methods with descriptions
    }

}
