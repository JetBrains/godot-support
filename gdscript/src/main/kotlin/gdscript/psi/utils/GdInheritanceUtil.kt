package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import gdscript.index.impl.GdClassIdIndex
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdClassNaming
import gdscript.psi.GdFile
import gdscript.psi.GdInheritance
import gdscript.utils.VirtualFileUtil.getPsiFile

object GdInheritanceUtil {

    /**
     * Get extended classId
     *
     * @param element: GdClassDeclTL|GdClassNaming|GdFile
     */
    fun getExtendedClassId(element: PsiElement): String {
        return when (element) {
            is GdClassNaming -> element.parentName
            is GdClassDeclTl -> element.parentName
            is GdFile -> PsiTreeUtil.getStubChildOfType(element, GdInheritance::class.java)?.inheritancePath.orEmpty()
            is PsiFile -> ""
            else -> getExtendedClassId(PsiGdClassUtil.getParentClassElement(element))
        }
    }

    /**
     * @param element GdClassDeclTL|GdClassNaming|GdFile
     *
     * @return GdClassDeclTL|GdFile
     */
    @Deprecated("Switch to getExtendedElement(element, project) to promote efficient project reference usage",
            ReplaceWith("getExtendedElement(element, project)", "gdscript.psi.utils.GdInheritanceUtil.getExtendedElement", ))
    fun getExtendedElement(element: PsiElement): PsiElement? {
        return getExtendedElement(
            getExtendedClassId(element),
            element,
            element.project,
        )
    }

    /**
     * @param element GdClassDeclTL|GdClassNaming|GdFile
     * @param project
     *
     * @return GdClassDeclTL|GdFile
     */
    fun getExtendedElement(element: PsiElement, project: Project): PsiElement? {
        return getExtendedElement(getExtendedClassId(element), element, project)
    }

    fun isExtending(element: PsiElement, className: String): Boolean {
        if (GdClassUtil.getOwningClassName(element) == className) return true

        var parentId = getExtendedClassId(element)
        while (parentId.isNotBlank()) {
            if (parentId == className) return true
            val parent = GdClassIdIndex.INSTANCE.getGlobally(parentId, element).firstOrNull() ?: return false
            parentId = getExtendedClassId(parent)
        }

        return false
    }

    /**
     * @param classId FQN like MyClass.DataClass or "res://Item.gd"
     *
     * @return GdClassDeclTL|GdFile
     */
    fun getExtendedElement(classId: String, element: PsiElement, project: Project): PsiElement? {
        val classEl = GdClassUtil.getClassIdElement(classId, element, project)
        // Extending directly named class (includes "res://Item.gd".InnerClass)
        if (classEl != null) {
            return if (classEl.parent is GdClassDeclTl) {
                classEl.parent
            } else {
                classEl.containingFile
            }
        }

        // In case of unnamed "res://Item.gd" check for the resource itself
        val file = GdFileResIndex.getFiles(classId.trim('"', '\''), project).firstOrNull() ?: return null

        return file.getPsiFile(project)
    }

}
