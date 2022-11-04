package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.*
import gdscript.utils.VirtualFileUtil.resourcePath

object GdClassUtil {

    /**
     * @param element current element
     *
     * @return String owning classId (resource if not named)
     */
    fun getOwningClassId(element: PsiElement): String {
        return when(val it = getOwningClassElement(element)) {
            is GdClassDeclTl -> it.name;
            else -> {
                val cln = PsiTreeUtil.getStubChildOfType(it, GdClassNaming::class.java);
                if (cln != null) return cln.classname;

                element.containingFile.virtualFile.resourcePath();
            }
        }
    }

    /**
     * @return GdClassDecl|GdFile containing element
     */
    fun getOwningClassElement(element: PsiElement): PsiElement {
        val inner = PsiTreeUtil.getStubOrPsiParentOfType(element, GdClassDeclTl::class.java);
        if (inner != null) return inner;

        return element.containingFile;
    }

    fun getName(element: GdClassDeclTl): String {
        val stub = element.stub;
        if (stub != null) return stub.name();
        return element.classNameNmi?.name.orEmpty();
    }

}
