package gdscript.reference

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.elementType
import gdscript.completion.utils.GdCompletionUtil
import gdscript.completion.utils.GdFileCompletionUtil
import gdscript.index.impl.GdClassNamingIndex
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.GdInheritanceIdNm
import gdscript.psi.GdTypes
import gdscript.psi.utils.PsiGdClassUtil

class GdInheritanceNmReference : PsiReferenceBase<GdInheritanceIdNm> {

    private var key: String = "";

    constructor(element: PsiElement, textRange: TextRange) : super(element as GdInheritanceIdNm, textRange) {
        key = element.text.substring(textRange.startOffset, textRange.endOffset).trim('"');
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        // TODO rename resource ??... rename file?
        if (isResource()) return element;

        myElement.name = newElementName;

        return element;
    }

    override fun resolve(): PsiElement? {
        val project = element.project;
        if (isResource()) {
            val virtualFile = GdFileResIndex.getFiles(key, project).firstOrNull() ?: return null;

            return PsiManager.getInstance(project).findFile(virtualFile);
        }

        return GdClassNamingIndex
            .get(key, project, GlobalSearchScope.allScope(project))
            .firstOrNull()?.classNameNmi; // TODO ii
    }

    override fun getVariants(): Array<LookupElement> {
        val project = myElement.project;

        if (isResource()) {
            return GdFileCompletionUtil.listFileResources(project, true, true);
        }

        val classNames = PsiGdClassUtil.listClassNaming(project);

        return classNames.mapNotNull {
            if (it.classname !== "") {
                GdCompletionUtil.lookup(it);
            } else {
                null
            }
        }.toTypedArray()
    }

    private fun isResource(): Boolean {
        return element.firstChild.elementType == GdTypes.STRING;
    }

}
