package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.stubChildOfType
import gdscript.index.impl.GdClassIdIndex
import gdscript.index.impl.GdFileResIndex
import gdscript.polySymbols.resolve.GdSymbolResolverUtil
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
        // Hop to context before the GdFile/PsiFile checks, as the fragment IS both.
        val effectiveElement = GdCodeFragmentUtil.effectiveElement(element)
        return when (effectiveElement) {
            is GdClassNaming -> effectiveElement.parentName
            is GdClassDeclTl -> effectiveElement.parentName
            is GdFile -> effectiveElement.stubChildOfType<GdInheritance>()?.inheritancePath.orEmpty()
            is PsiFile -> ""
            else -> getExtendedClassId(PsiGdClassUtil.getParentClassElement(effectiveElement))
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
            val parent = GdClassIdIndex.INSTANCE.getGlobally(parentId, element).firstOrNull()
                ?: return GdSymbolResolverUtil.isExtendingCanonical(parentId, element.project, element, className)
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
