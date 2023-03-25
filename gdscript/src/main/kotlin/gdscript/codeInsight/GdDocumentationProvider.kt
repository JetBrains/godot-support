package gdscript.codeInsight

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiNamedElement
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.PsiGdCommentUtils
import gdscript.reference.GdClassMemberReference
import java.awt.Image

class GdDocumentationProvider : AbstractDocumentationProvider() {

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        if (originalElement != null && originalElement.parent is PsiNamedElement) {
            return findDocumentationComment(originalElement.parent as PsiNamedElement, PsiGdCommentUtils.DESCRIPTION);
        }

        return renderDocumentationForDeclaration(element, PsiGdCommentUtils.DESCRIPTION);
    }

    override fun generateHoverDoc(element: PsiElement, originalElement: PsiElement?): String? {
        if (originalElement != null && originalElement.parent is PsiNamedElement) {
            val doc = findDocumentationComment(
                originalElement.parent as PsiNamedElement,
                PsiGdCommentUtils.BRIEF_DESCRIPTION
            );
            if (doc != null && doc.isNotEmpty()) {
                return doc;
            }

            return findDocumentationComment(originalElement.parent as PsiNamedElement, PsiGdCommentUtils.DESCRIPTION);
        }

        return null;
    }

    private fun findDocumentationComment(property: PsiNamedElement, key: String): String? {
        var declaration = GdClassMemberReference(property).resolveDeclaration();
        if (declaration == null) {
            declaration = GdClassNamingIndex.getGlobally(property)
                .firstOrNull() ?: return null;
        }

        return renderDocumentationForDeclaration(declaration, key)
    }

    private fun renderDocumentationForDeclaration(element: PsiElement, key: String): String {
        val comments = PsiGdCommentUtils.collectDescriptions(element, key)

        return renderFullDoc(comments);
    }

    private fun renderFullDoc(docLines: Array<String>): String {
        val sb = StringBuilder();
        sb.append(DocumentationMarkup.CONTENT_START);

        val dynamicReference = "\\[(member|constant|method) (.+)]".toRegex()

//        sb.append(GdDocumentationUtil.createElementLink(sb))

        /*
        [member position]
        [method _local]
        [method Input.method]
        [constant NOTIFICATION_READY]
        [Object]

        [b]  styling
        [code]
        [i]

        [gdscript]  examples
        [csharp]
         */

        docLines.forEach {
            val line = it
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
            val references = dynamicReference.findAll(line)
            references.forEach { match ->
                val value = match.groups[2]?.value ?: ""
                sb.append(
                    GdDocumentationUtil.createElementLink(
                        value.split(".").last(),
                        value.replace("@", "_"),
                    ),
                )
            }
            //.replace("\\[method (.+?)]".toRegex(), "{@link $1}")

            sb.append(line);
            sb.append("<br />");
        }
        sb.append(DocumentationMarkup.CONTENT_END);

        return sb.toString();
    }

    override fun getDocumentationElementForLink(
        psiManager: PsiManager?,
        link: String?,
        context: PsiElement?,
    ): PsiElement? {
        if (link.isNullOrBlank() || context == null) return null
        val asd =GdClassMemberUtil.listDeclarations(
            context,
            link,
        )

        return GdClassMemberUtil.listDeclarations(
            context,
            link,
        ).firstOrNull()
    }



    override fun getLocalImageForElement(element: PsiElement, imageSpec: String): Image? {
        return super.getLocalImageForElement(element, imageSpec)
    }


    // TODO ctrl hover nad referencí
//    override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String? {
//        return super.getQuickNavigateInfo(element, originalElement)
//    }

}
