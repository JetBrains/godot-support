package gdscript.codeInsight

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.findDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import gdscript.codeInsight.documentation.GdDocFactory
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdClassUtil
import gdscript.reference.GdClassMemberReference
import gdscript.utils.PsiElementUtil.psi

class GdDocumentationProvider : AbstractDocumentationProvider() {

    companion object {
        val LINK_ENUM_VALUE = "enumValue"
        val LINK_PACKAGE = "package"
    }

    override fun generateDoc(element: PsiElement, originalElement: PsiElement?): String? {
        return GdDocFactory.create(element, true)
    }

    override fun generateHoverDoc(element: PsiElement, originalElement: PsiElement?): String? {
        return GdDocFactory.create(element, false)
    }

    override fun getDocumentationElementForLink(
            psiManager: PsiManager?,
            link: String?,
            context: PsiElement?,
    ): PsiElement? {
        if (link.isNullOrBlank() || context == null) return null
        val project = context.project
        if (link.contains(":") && !link.startsWith("res://")) {
            val prefix = link.substringBefore(":")
            val subLink = link.substringAfter(":")
            if (prefix == LINK_ENUM_VALUE) {
                val enumName = subLink.substringBefore(".")
                val enumValue = subLink.substringAfter(".")
                GdClassMemberUtil.listDeclarations(context, enumName).firstOrNull()?.let {
                    if (it is GdEnumDeclTl) {
                        return it.enumValueList.find { it.enumValueNmi.name == enumValue }?.enumValueNmi
                    }
                }
            } else if (prefix == LINK_PACKAGE) {
                var directory = ProjectFileIndex.getInstance(project).getContentRootForFile(project.projectFile!!) ?: return null
                if (subLink.contains("/")) directory = directory.findDirectory(subLink.substringAfter("/")) ?: return null

                return PsiManager.getInstance(project).findDirectory(directory)
            }

            return null
        }

        if (context.containingFile != null) {
            GdClassMemberUtil.listDeclarations(context, link).firstOrNull()?.psi()?.let {
                return GdClassMemberReference.resolveId(it)
            }
        }
        GdClassUtil.getClassIdElement(link, project)?.let { return it }

        return null
    }

}
