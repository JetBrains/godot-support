package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdMethodIdNmi

object GdMethodUtil {

    /**
     * Finds method in given owner by name for the parent structure
     */
    fun findParentMethodRecursive(element: GdMethodIdNmi, project: Project) : GdMethodDeclTl? {
        var par = GdInheritanceUtil.getExtendedElement(element.parent, project)
        while (par != null) {
            findMethod(par, element.name)?.let { return it }
            par = GdInheritanceUtil.getExtendedElement(par, project)
        }
        return null
    }

    /**
     * Finds method in given owner by name
     */
    fun findMethod(parent: PsiElement, name: String): GdMethodDeclTl? {
        return PsiTreeUtil.getStubChildrenOfTypeAsList(parent, GdMethodDeclTl::class.java)
            .find { it.name == name }
    }

    fun getName(element: GdMethodDeclTl): String {
        val stub = element.stub
        if (stub !== null) stub.name()

        return element.methodIdNmi?.name.orEmpty()
    }

}
