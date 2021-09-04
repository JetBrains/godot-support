package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import gdscript.index.impl.GdClassNamingIndex
import gdscript.index.impl.GdClassVarDeclIndex
import gdscript.index.impl.GdConstDeclIndex
import gdscript.index.impl.GdMethodDeclIndex
import gdscript.psi.GdClassNaming
import gdscript.psi.GdInheritance
import gdscript.psi.GdNamedElement

object PsiGdNamedUtil {

    fun listParents(element: PsiElement): MutableList<GdClassNaming> {
        var parentName: String? =
            PsiTreeUtil.getChildOfType(element.containingFile, GdInheritance::class.java)?.inheritanceName;
        val parents = mutableListOf<GdClassNaming>();

        while (parentName !== null) {
            val parent =
                GdClassNamingIndex.get(parentName, element.project, GlobalSearchScope.allScope(element.project))
                    .firstOrNull();
            if (parent === null) {
                return parents;
            }

            parents.add(parent);
            parentName = parent.parentName;
        }

        return parents;
    }

    fun findInParent(
        element: GdNamedElement,
        method: Boolean = true,
        variable: Boolean = true,
        constant: Boolean = true,
        includingSelf: Boolean = false,
    ): PsiElement? {
        var parentName: String? =
            PsiTreeUtil.getChildOfType(element.containingFile, GdInheritance::class.java)?.inheritanceName;
        val thisName = element.name.orEmpty();
        if (includingSelf) {
            lookFor(element.containingFile, thisName, element.project, constant, variable, method)?.let {
                return it
            }
        }

        while (parentName !== null) {
            val parent =
                GdClassNamingIndex.get(parentName, element.project, GlobalSearchScope.allScope(element.project))
                    .firstOrNull();
            if (parent === null) {
                return null;
            }

            lookFor(parent.containingFile, thisName, element.project, constant, variable, method)?.let {
                return it
            }

            parentName = parent.parentName;
        }

        return null;
    }

    private fun lookFor(file: PsiFile, thisName: String, project: Project, constant: Boolean, variable: Boolean, method: Boolean): PsiElement? {
        if (constant) {
            val parentConst = GdConstDeclIndex.get(thisName, project, GlobalSearchScope.fileScope(file));
            if (!parentConst.isEmpty()) {
                return parentConst.first();
            }
        }

        if (variable) {
            val parentVar = GdClassVarDeclIndex.get(thisName, project, GlobalSearchScope.fileScope(file));
            if (!parentVar.isEmpty()) {
                return parentVar.first();
            }
        }

        if (method) {
            val parentMethod = GdMethodDeclIndex.get(thisName, project, GlobalSearchScope.fileScope(file));
            if (!parentMethod.isEmpty()) {
                return parentMethod.first();
            }
        }

        return null;
    }

}