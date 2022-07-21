package gdscript.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import gdscript.GdFileType

object GdElementFactory {

    fun identifier(project: Project, name: String): PsiElement {
        return createFile(project, "extends $name\n").firstChild.firstChild.nextSibling.nextSibling.firstChild;
    }

    private fun createFile(project: Project, text: String) =
            PsiFileFactory.getInstance(project).createFileFromText("dum.gd", GdFileType.INSTANCE, text) as GdFile
}