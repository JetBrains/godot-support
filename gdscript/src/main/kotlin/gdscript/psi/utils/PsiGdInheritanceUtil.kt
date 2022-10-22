package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import gdscript.index.impl.GdClassNamingIndex
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.GdClassNaming
import gdscript.psi.GdInheritance
import gdscript.psi.GdInheritanceIdNmi

object PsiGdInheritanceUtil {

    fun getPsiFile(inheritance: GdInheritanceIdNmi): PsiFile? {
        val key = inheritance.text.trim('"');
        if (key.startsWith("res://")) {
            val virtual = GdFileResIndex.getFiles(key, inheritance.project).first();

            return PsiManager.getInstance(inheritance.project).findFile(virtual);
        }

        return GdClassNamingIndex.getGlobally(inheritance).firstOrNull()?.containingFile;
    }

    fun getPsiFile(inheritance: String, project: Project): PsiFile? {
        if (inheritance.startsWith("res://")) {
            val virtual = GdFileResIndex.getFiles(inheritance, project).firstOrNull() ?: return null;

            return PsiManager.getInstance(project).findFile(virtual);
        }

        return GdClassNamingIndex.getGlobally(inheritance, project).firstOrNull()?.containingFile;
    }

    fun getParentName(element: PsiFile?): String? {
        val inh = PsiTreeUtil.findChildOfType(element, GdInheritance::class.java)

        return inh?.inheritanceName;
    }

}
