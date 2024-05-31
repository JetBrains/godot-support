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
    private var resKey: String = ""
    private var project: Project

    constructor(element: PsiElement) : super(element as GdNamedElement, TextRange(0, element.textLength)) {
        this.project = element.project
        key = element.text
        resKey = key.trim('"', '\'')

        if (!resKey.startsWith("res://")){
            element.containingFile?.virtualFile?.resourcePath()?.let {
                var self = it
                self = self.substring(0, self.lastIndexOf('/'))
                resKey = "$self/${key.trim('"', '\'')}"
            }
        }
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        // TODO ignored relative paths
        if (!key.startsWith("\"res://")) return element
        element.setName(newElementName)
        GdCfgUtil.renameValue(project, key.trim('"', '\''), "res://$newElementName")

        return element
    }

    override fun bindToElement(element: PsiElement): PsiElement {
        this.handleElementRename(element.containingFile.virtualFile.resourcePath(false))
        return element
    }

    override fun resolve(): PsiElement? {
        return GdFileResIndex.getFiles(resKey, project)
            .firstOrNull()
            ?.getPsiFile(project)
    }

    override fun getVariants(): Array<LookupElement> {
        return GdFileResIndex.getNonEmptyKeys(project)
            .filterGdTscn().map {
                GdLookup.create(
                    "\"$it\"",
                    priority = GdLookup.REMOTE_DEFINED,
                )
            }.toTypedArray()
    }

}
