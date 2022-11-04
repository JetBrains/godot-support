package gdscript.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import gdscript.completion.GdLookup
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.GdNamedElement
import gdscript.utils.StringUtil.filterGdScripts
import gdscript.utils.VirtualFileUtil.getPsiFile

/**
 * Resource "res://" reference to file
 */
class GdResourceReference : PsiReferenceBase<GdNamedElement> {

    private var key: String = "";

    constructor(element: PsiElement) : super(element as GdNamedElement, TextRange(0, element.textLength)) {
        key = element.text;
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        // TODO renaming a file..
        element.setName(newElementName);

        return element;
    }

    override fun resolve(): PsiElement? {
        return GdFileResIndex.getFiles(key.trim('"'), element.project)
            .firstOrNull()
            ?.getPsiFile(element.project);
    }

    override fun getVariants(): Array<LookupElement> {
        return GdFileResIndex.getNonEmptyKeys(element.project)
            .filterGdScripts().map {
                GdLookup.create(
                    it,
                    priority = GdLookup.REMOTE_DEFINED,
                )
            }.toTypedArray();
    }

}
