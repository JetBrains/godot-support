package gdscript.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import gdscript.completion.GdLookup
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.GdNamedElement
import gdscript.psi.utils.GdCfgUtil
import gdscript.utils.StringUtil.filterGdTscn
import gdscript.utils.VirtualFileUtil.getPsiFile
import gdscript.utils.VirtualFileUtil.resourcePath

/**
 * Resource "res://" reference to file
 */
class GdResourceReference : PsiReferenceBase<GdNamedElement> {

    private var key: String = ""
    private var project: Project

    constructor(element: PsiElement) : super(element as GdNamedElement, TextRange(0, element.textLength)) {
        key = element.text
        this.project = element.project
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        element.setName(newElementName)
        GdCfgUtil.renameValue(project, key.trim('"'), "res://$newElementName")

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
            .filterGdTscn().map {
                GdLookup.create(
                    "\"$it\"",
                    priority = GdLookup.REMOTE_DEFINED,
                )
            }.toTypedArray()
    }

}
