package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import gdscript.index.impl.GdClassNamingIndex
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.*

object PsiGdInheritanceUtil {

    fun isClassName(inheritance: GdInheritanceIdNm): Boolean {
        return inheritance.firstChild.elementType == GdTypes.IDENTIFIER;
    }

    fun getPsiFile(inheritance: GdInheritanceIdNm): PsiFile? {
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

    @Deprecated("moved to classUtils") // TODO move others as well
    fun getFirstParent(element: PsiElement): PsiElement {
        if (element is GdClassDeclTl) {
            return element;
        }

        return PsiGdTreeUtil.findFirstPrecedingElement(element) {
            it is GdFile || it is GdClassDeclTl
        } ?: element.containingFile;
    }

    fun getFirstParentName(element: PsiElement): String? {
        val rootClass = getFirstParent(element);
        if (rootClass is GdFile) {
            return getParentName(rootClass as PsiFile);
        }

        return PsiTreeUtil.findChildOfType(rootClass, GdInheritanceIdNm::class.java)?.name;
    }

}
