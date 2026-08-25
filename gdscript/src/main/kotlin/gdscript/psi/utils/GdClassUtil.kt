package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.stubChildOfType
import gdscript.index.impl.GdClassDeclIndex
import gdscript.index.impl.GdClassIdIndex
import gdscript.index.impl.GdFileResIndex
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdClassNaming
import gdscript.psi.GdFile
import gdscript.utils.PsiFileUtil.toAbsoluteResource
import gdscript.utils.VirtualFileUtil.getPsiFile
import gdscript.utils.VirtualFileUtil.resourcePath

object GdClassUtil {

    fun getClassIdElement(name: String, element: PsiElement, project: Project): PsiElement? {
        val path = name.toAbsoluteResource(element, project)

        GdClassIdIndex.INSTANCE.getGloballyResolved(path, project).firstOrNull()?.let { return it }
        GdFileResIndex.getFiles(path.trim('"', '\''), project).firstOrNull()?.let { return it.getPsiFile(project) }
        GdClassDeclIndex.INSTANCE.getInFile(name, element, project).firstOrNull()?.let { return it }

        return null
    }

    @Deprecated("For internal usage only called after resolving relative paths")
    fun getClassIdElement(name: String, project: Project): PsiElement? {
        return GdClassIdIndex.INSTANCE.getGloballyResolved(name, project).firstOrNull()
            ?: GdFileResIndex.getFiles(name.trim('"', '\''), project).firstOrNull()
                ?.let { return it.getPsiFile(project) }
    }

    @Deprecated(
        "Use above with supplied project", ReplaceWith(
            "getClassIdElement(name, project)",
            "gdscript.psi.utils.GdClassUtil.getClassIdElement"
        )
    )
    fun getClassIdElement(name: String, element: PsiElement): PsiElement? {
        return getClassIdElement(name, element.project)
    }

    /**
     * @param element current element
     *
     * @return String owning className (resource if not named)
     */
    fun getOwningClassName(element: PsiElement): String {
        return when (val it = getOwningClassElement(element)) {
            is GdClassDeclTl -> it.getName()
            else -> {
                val cln = it.stubChildOfType<GdClassNaming>()
                if (cln != null) return cln.classname

                val file = GdCodeFragmentUtil.effectiveFile(element)
                (file.virtualFile ?: file.originalFile.virtualFile).resourcePath()
            }
        }
    }

    /**
     * @param element GdClassDecl|GdFile
     * @return Full classId to given class "Class.Inner" (can be resource)
     */
    fun getFullClassId(element: PsiElement): String {
        // Hop to context before the GdFile check, as the fragment IS a GdFile.
        val effectiveElement = GdCodeFragmentUtil.effectiveElement(element)
        return when (effectiveElement) {
            is GdClassDeclTl -> effectiveElement.classNameNmi?.classId ?: ""
            is GdFile -> {
                val named = effectiveElement.stubChildOfType<GdClassNaming>()
                if (named != null) {
                    named.classNameNmi?.classId ?: ""
                } else {
                    val file = effectiveElement.virtualFile ?: effectiveElement.originalFile.virtualFile
                    "\"${PsiGdResourceUtil.resourcePath(file)}\""
                }
            }

            else -> getFullClassId(getOwningClassElement(effectiveElement))
        }
    }

    /**
     * @return GdClassDecl|GdFile containing element
     */
    fun getOwningClassElement(element: PsiElement): PsiElement {
        // Hop to context before the GdFile check, as the fragment IS a GdFile.
        val effectiveElement = GdCodeFragmentUtil.effectiveElement(element)
        when (effectiveElement) {
            is GdFile -> return effectiveElement
            is GdClassDeclTl -> return effectiveElement
        }

        val inner = PsiTreeUtil.getStubOrPsiParentOfType(effectiveElement, GdClassDeclTl::class.java)
        if (inner != null) return inner

        return effectiveElement.containingFile
    }

    fun getName(element: GdClassDeclTl): String {
        val stub = element.stub
        if (stub != null) return stub.name()

        return element.classNameNmi?.name.orEmpty()
    }

}
