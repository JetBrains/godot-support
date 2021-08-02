package gdscript.reference

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import gdscript.psi.GdRefIdNm

class GdRefIdReference : PsiReferenceBase<GdRefIdNm> {

    private var key: String = "";

    constructor(element: PsiElement, textRange: TextRange) : super(element as GdRefIdNm, textRange) {
        key = element.text.substring(textRange.startOffset, textRange.endOffset)
    }

    override fun handleElementRename(newElementName: String): PsiElement {
        myElement.name = newElementName;

        return myElement;
    }

    override fun resolve(): PsiElement? {
        return null;
        /*return GdClassNamingIndex
            .get(key, myElement.project, GlobalSearchScope.allScope(myElement.project))
            .firstOrNull()?.classNameNm;*/
    }

    override fun getVariants(): Array<Any> {
//        val project = myElement.project;
//        val classNames = PsiGdClassNamingUtil.listClassNaming(project);
//
//        return classNames.mapNotNull {
//            if (it.classname !== "") {
//                GdClassNameCompletionUtil.toLookup(it);
//            } else {
//                null
//            }
//        }.toTypedArray()
        return emptyArray();
    }

}
