package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdMethodDeclTl

object GdMethodUtil {

    /**
     * Finds method in given owner by name
     */
    fun findMethod(parent: PsiElement, name: String): GdMethodDeclTl? {
        return PsiTreeUtil.getStubChildrenOfTypeAsList(parent, GdMethodDeclTl::class.java)
            .find { it.name == name }
    }

    fun getName(element: GdMethodDeclTl): String {
        val stub = element.stub;
        if (stub !== null) stub.name();

        return element.methodIdNmi?.name.orEmpty();
    }

}
