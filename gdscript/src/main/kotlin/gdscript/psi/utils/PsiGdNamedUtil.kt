package gdscript.psi.utils

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.HighlightSeverity
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

    fun isParentallyUnique(element: GdNamedIdElement): PsiElement? {
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
            val parentConst = GdConstDeclIndex.get(thisName, element.project, GlobalSearchScope.fileScope(file));
            if (!parentConst.isEmpty()) {
                return parentConst.first();
            }

            val parentVar = GdClassVarDeclIndex.get(thisName, element.project, GlobalSearchScope.fileScope(file));
            if (!parentVar.isEmpty()) {
                return parentVar.first();
            }

            val parentMethod = GdMethodDeclIndex.get(thisName, element.project, GlobalSearchScope.fileScope(file));
            if (!parentMethod.isEmpty()) {
                return parentMethod.first();
            }

            parentName = parent.parentName;
        }

        return null;
    }
    
}