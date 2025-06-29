package gdscript.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.findFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import gdscript.completion.GdLookup
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.GdElementFactory
import gdscript.psi.utils.GdCfgUtil
import gdscript.utils.StringUtil.filterGdTscn
import gdscript.utils.VirtualFileUtil.getPsiFile
import gdscript.utils.VirtualFileUtil.resourcePath

/**
 * Resource "res://" reference to file
 * GodotEditor also supports relative to current script notation, but it is not recommended
 */
class GdResourceReference : PsiReferenceBase<PsiElement> {

    private var key: String = ""
    private var resKey: String = ""
    private var project: Project

    constructor(element: PsiElement) : super(element, TextRange(0, element.textLength)) {
        this.project = element.project
        key = element.text
        resKey = key.trim('"', '\'')

        if (!resKey.startsWith("res://")){
            element.containingFile?.virtualFile?.parent?.let {
                var file = it.findFile(resKey)
                if (file != null){
                    resKey = file.resourcePath(true)
                }
            }
        }
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        // TODO ignored relative paths
        if (!key.startsWith("\"res://")) return element
        GdCfgUtil.renameValue(project, key.trim('"', '\''), "res://$newElementName")

        return element.replace(GdElementFactory.typeStringVal(project, newElementName))
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
