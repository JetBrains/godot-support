package gdscript.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.findFileOrDirectory
import com.intellij.openapi.vfs.isFile
import com.intellij.psi.ElementManipulators
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.utils.GdCfgUtil
import gdscript.utils.VirtualFileUtil.getPsiFile
import gdscript.utils.VirtualFileUtil.resourcePath
import gdscript.utils.stringContentRange

/**
 * Resource "res://" reference to file
 * GodotEditor also supports relative to current script notation, but it is not recommended
 */
class GdResourceReference : PsiReferenceBase<PsiElement> {

    private val key: String
    private var resKey: String = ""
    private var project: Project

    constructor(element: PsiElement) : super(element, stringContentRange(element.text)) {
        this.project = element.project
        key = rangeInElement.substring(element.text)
        resKey = key

        if (!resKey.startsWith("res://")){
            element.containingFile?.virtualFile?.parent?.let {
                var file = try {
                    it.findFileOrDirectory(resKey)
                }
                catch (e: Exception) {
                    thisLogger().warn("Failed to resolve relative resource path '$resKey' from parent '${it.path}': Exception", e)
                    null
                }
                if (file != null && file.isFile){
                    resKey = file.resourcePath(true)
                }
            }
        }
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        // TODO ignored relative paths
        if (!key.startsWith("res://")) return element
        GdCfgUtil.renameValue(project, key, "res://$newElementName")

        return ElementManipulators.handleContentChange(element, rangeInElement, "res://$newElementName")
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

    override fun getVariants(): Array<LookupElement> = emptyArray()

}
