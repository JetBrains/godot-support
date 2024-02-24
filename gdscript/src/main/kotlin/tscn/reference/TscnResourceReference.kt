package tscn.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiReferenceBase
import gdscript.completion.GdLookup
import gdscript.index.impl.GdFileResIndex
import gdscript.utils.StringUtil.filterGdScripts
import gdscript.utils.VirtualFileUtil.getPsiFile
import gdscript.utils.VirtualFileUtil.resourcePath

/**
 * Resource "res://" reference to file
 */
class TscnResourceReference : PsiReferenceBase<PsiNamedElement> {

    private var key: String = ""
    private var project: Project

    constructor(element: PsiElement) : super(element as PsiNamedElement, TextRange(0, element.textLength)) {
        key = element.text
        this.project = element.project
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        element.setName(newElementName)

        return element
    }

    override fun bindToElement(element: PsiElement): PsiElement {
        this.handleElementRename(element.containingFile.virtualFile.resourcePath(false))
        return element
    }

    override fun resolve(): PsiElement? {
        return GdFileResIndex.INSTANCE.getFiles(key.trim('"'), project)
            .firstOrNull()
            ?.getPsiFile(project)
    }

    override fun getVariants(): Array<LookupElement> {
        return GdFileResIndex.INSTANCE.getNonEmptyKeys(project)
            .filterGdScripts().map {
                GdLookup.create(
                    "\"$it\"",
                    priority = GdLookup.REMOTE_DEFINED,
                )
            }.toTypedArray()
    }

}
