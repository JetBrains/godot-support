package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import gdscript.index.impl.GdClassNamingIndex
import gdscript.index.impl.GdClassVarDeclIndex
import gdscript.index.impl.GdConstDeclIndex
import gdscript.index.impl.GdMethodDeclIndex
import gdscript.psi.GdInheritance
import gdscript.psi.GdNamedIdElement

object PsiGdNamedUtil {

    fun findInParent(
        element: GdNamedIdElement,
        method: Boolean = true,
        variable: Boolean = true,
        constant: Boolean = true,
    ): PsiElement? {
        var file = element.containingFile;
        var parentName: String? =
            PsiTreeUtil.getChildOfType(file, GdInheritance::class.java)?.inheritanceName;
        val thisName = element.name.orEmpty();

        while (parentName !== null) {
            val parent =
                GdClassNamingIndex.get(parentName, element.project, GlobalSearchScope.allScope(element.project))
                    .firstOrNull();
            if (parent === null) {
                return null;
            }

            file = parent.containingFile;
            if (constant) {
                val parentConst = GdConstDeclIndex.get(thisName, element.project, GlobalSearchScope.fileScope(file));
                if (!parentConst.isEmpty()) {
                    return parentConst.first();
                }
            }

            if (variable) {
                val parentVar = GdClassVarDeclIndex.get(thisName, element.project, GlobalSearchScope.fileScope(file));
                if (!parentVar.isEmpty()) {
                    return parentVar.first();
                }
            }

            if (method) {
                val parentMethod = GdMethodDeclIndex.get(thisName, element.project, GlobalSearchScope.fileScope(file));
                if (!parentMethod.isEmpty()) {
                    return parentMethod.first();
                }
            }

            parentName = parent.parentName;
        }

        return null;
    }

}