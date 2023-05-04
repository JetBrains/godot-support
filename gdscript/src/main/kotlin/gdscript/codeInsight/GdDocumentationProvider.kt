package gdscript.codeInsight

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiNamedElement
import gdscript.codeInsight.documentation.GdDocBuilder
import gdscript.codeInsight.documentation.GdDocFactory
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.GdClassNaming
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdNamedElement
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdClassMemberUtil.constants
import gdscript.psi.utils.GdClassMemberUtil.enums
import gdscript.psi.utils.GdClassMemberUtil.methods
import gdscript.psi.utils.GdClassMemberUtil.signals
import gdscript.psi.utils.GdClassMemberUtil.variables
import gdscript.psi.utils.GdClassUtil
import gdscript.psi.utils.PsiGdCommentUtils
import gdscript.reference.GdClassMemberReference
import gdscript.utils.PsiElementUtil.psi
import gdscript.utils.VirtualFileUtil.localParentPath

class GdDocumentationProvider : AbstractDocumentationProvider() {

    private val dynamicReference = "\\[(member|constant|method|enum) (.+?)]".toRegex()
    private val links = "\\[link (.+?)](.+?)\\[/link]".toRegex()
    private val freeReference = "\\[([A-Z].+?)]".toRegex()

    fun test(element: PsiElement): String {
        return GdDocFactory.create(element, true) ?: ""

//        return GdDocBuilder()
//            .withPackage(element.containingFile.virtualFile.localParentPath())
//            .withOwner(GdClassUtil.getOwningClassElement(element))
//            .withPreview(element)
//            .toString()
    }

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        return test(element)
        if (originalElement != null && originalElement.parent is GdNamedElement) {
            return findDocumentationComment(originalElement.parent as GdNamedElement, PsiGdCommentUtils.DESCRIPTION)
        }

        return renderDocumentationForDeclaration(element, PsiGdCommentUtils.DESCRIPTION)
    }

    override fun generateHoverDoc(element: PsiElement, originalElement: PsiElement?): String? {
        if (originalElement != null && originalElement.parent is PsiNamedElement) {
            val doc = findDocumentationComment(
                originalElement.parent as PsiNamedElement,
                PsiGdCommentUtils.BRIEF_DESCRIPTION
            );
            if (doc != null && doc.isNotEmpty()) {
                return doc
            }

            return findDocumentationComment(originalElement.parent as PsiNamedElement, PsiGdCommentUtils.DESCRIPTION)
        }

        return null
    }

    @Deprecated("remove")
    private fun findDocumentationComment(property: PsiNamedElement, key: String): String? {
        var declaration = GdClassMemberReference(property).resolveDeclaration()
        if (declaration == null) {
            declaration = GdClassNamingIndex.getGlobally(property)
                .firstOrNull() ?: return null
        }

        return renderDocumentationForDeclaration(declaration, key)
    }

    @Deprecated("remove")
    private fun renderDocumentationForDeclaration(element: PsiElement, key: String): String {
        val sb = StringBuilder()
        sb.append(DocumentationMarkup.CONTENT_START)

        val comments = PsiGdCommentUtils.collectDescriptions(element, key)
        val doc = renderFullDoc(sb, comments)

        // For classes collect also all props and methods
        if (element is GdClassNaming && key == PsiGdCommentUtils.DESCRIPTION) {
            appendProperties(sb, element)
        }

        sb.append(DocumentationMarkup.CONTENT_END)
        return doc.toString();
    }

    @Deprecated("remove")
    private fun renderFullDoc(sb: StringBuilder, docLines: Array<String>): StringBuilder {
        var tutorials = false

        /*
        [member position]
        [method _local]
        [method Input.method]
        [enum Input.method]
        [constant NOTIFICATION_READY]
        [Object]

        [b]  styling
        [code]
        [i]

        [gdscript]  examples
        [csharp]
         */

        docLines.forEach {
            var line = it
                .replace("[b]", "<strong>")
                .replace("[/b]", "</strong>")
                .replace("[code]", "<i>")
                .replace("[/code]", "</i>")
                .replace("[codeblocks]", "")
                .replace("[/codeblocks]", "")

                .replace("[gdscript]", "${DocumentationMarkup.DEFINITION_START}<strong>GdScript</strong>")
                .replace("[csharp]", "${DocumentationMarkup.DEFINITION_START}<strong>C#</strong>")
                .replace("[/gdscript]", DocumentationMarkup.DEFINITION_END)
                .replace("[/csharp]", DocumentationMarkup.DEFINITION_END)

            // Links & tutorials
            val match = links.find(line)
            if (match != null) {
                if (!tutorials) {
                    tutorials = true
                    GdDocumentationUtil.paragraphHeader(sb, "Tutorials")
                }
                val link = GdDocumentationUtil.createLink(
                    match.groups[2]?.value ?: "",
                    match.groups[1]?.value ?: "",
                )
                sb.append(link)
                sb.append("<br />")
                return@forEach
            }

            // Replace specific references [member|constant|method _name]
            dynamicReference.findAll(line).forEach matched@{ match ->
                val value = match.groups[2]?.value ?: return@matched
                val link = GdDocumentationUtil.createElementLink(
                    value.split(".").last(),
                    value.replace("@", "_"),
                )
                line = line.replace(match.groups[0]?.value!!, link.toString())
            }

            // Try to replace unspecified references like [Object] or [Node]
            freeReference.findAll(line).forEach matched@{ match ->
                val value = match.groups[1]?.value?.replace("@", "_") ?: return@matched
                val link = GdDocumentationUtil.createElementLink(
                    value,
                    value,
                )
                line = line.replace(match.groups[0]?.value!!, link.toString())
            }

            sb.append(line)
            sb.append("<br />")
            sb.append("<br />")
        }

        return sb
    }

    override fun getDocumentationElementForLink(
        psiManager: PsiManager?,
        link: String?,
        context: PsiElement?,
    ): PsiElement? {
        if (link.isNullOrBlank() || context == null) return null

        val declaration = GdClassMemberUtil.listDeclarations(context, link).firstOrNull()?.psi() ?: return null
        return GdClassMemberReference.resolveId(declaration)
    }

    // TODO ctrl hover nad referencí
//    override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String? {
//        return super.getQuickNavigateInfo(element, originalElement)
//    }

    @Deprecated("remove")
    private fun appendProperties(sb: StringBuilder, classElement: GdClassNaming) {
        val declarations = GdClassMemberUtil.listClassMemberDeclarations(classElement, null, constructors = true)
        val variables = declarations.variables()
        if (variables.isNotEmpty()) {
            sb.append("<br />")
            GdDocumentationUtil.paragraphHeader(sb, "Properties")
            GdDocumentationUtil.propTable(sb, variables.map { arrayOf(it.returnType, it.name) })
        }

        val methods = mutableListOf<GdMethodDeclTl>()
        val constructors = mutableListOf<GdMethodDeclTl>()

        declarations.methods().forEach {
            if (it.isConstructor) {
                constructors.add(it)
            } else {
                methods.add(it)
            }
        }

        if (constructors.isNotEmpty()) {
            sb.append("<br />")
            GdDocumentationUtil.paragraphHeader(sb, "Constructors")
            GdDocumentationUtil.propTable(
                sb,
                constructors.map {
                    arrayOf(it.returnType, String.format("%s(%s)", it.name, it.paramList?.text ?: ""))
                })
        }

        if (methods.isNotEmpty()) {
            sb.append("<br />")
            GdDocumentationUtil.paragraphHeader(sb, "Methods")
            GdDocumentationUtil.propTable(
                sb,
                methods.map {
                    arrayOf(it.returnType, String.format("%s(%s)", it.name, it.paramList?.text ?: ""))
                })
        }

        // TODO operators

        val signals = declarations.signals()
        if (signals.isNotEmpty()) {
            sb.append("<br />")
            GdDocumentationUtil.paragraphHeader(sb, "Signals")
            GdDocumentationUtil.signalTable(sb, signals.map {
                val comments = PsiGdCommentUtils.collectDescriptions(it)
                var name = it.name
                if (!name.endsWith(")")) name += "()"
                arrayOf(name, *comments)
            })
        }

        val enums = declarations.enums()
        if (enums.isNotEmpty()) {
            sb.append("<br />")
            GdDocumentationUtil.paragraphHeader(sb, "Enums")
            GdDocumentationUtil.enumTable(sb, enums.map {
                val name = PsiGdCommentUtils.collectDescriptions(it, PsiGdCommentUtils.ENUM).firstOrNull() ?: "enum"
                arrayOf(
                    Pair(
                        name,
                        it.values.map { value -> Pair(value.key, value.value.toString()) }.toTypedArray()
                    )
                )
            })
        }

        val consts = declarations.constants()
        if (consts.isNotEmpty()) {
            sb.append("<br />")
            GdDocumentationUtil.paragraphHeader(sb, "Constants")
            sb.append("<ul>")
            consts.forEach {
                sb.append("<li>")
                sb.append(it.name)
                sb.append(GdDocumentationUtil.grayText(" = ${it.expr?.text}"))

                val descriptions = PsiGdCommentUtils.collectDescriptions(it)
                if (descriptions.isNotEmpty()) {
                    sb.append("<br /><a style=\"color: gray;\">")
                    renderFullDoc(sb, descriptions)
                    sb.append("</a>")
                }
                sb.append("</li>")
            }
            sb.append("</ul>")
        }
    }

}
