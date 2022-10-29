package gdscript.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import gdscript.GdFileType

/**
 * Create PsiElements for refactoring purposes
 */
object GdElementFactory {

    /**
     * class_name {NAME}
     */
    fun className(project: Project, name: String): PsiElement {
        return createFile(project, "class_name $name\n").firstChild.firstChild.nextSibling.nextSibling.firstChild;
    }

    fun identifier(project: Project, name: String): PsiElement {
        return createFile(project, "extends $name\n").firstChild.firstChild.nextSibling.nextSibling.firstChild;
    }

    private fun createFile(project: Project, text: String) =
            PsiFileFactory.getInstance(project).createFileFromText("dum.gd", GdFileType, text) as GdFile
}