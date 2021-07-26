package gdscript.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import gdscript.GdFileType

object GdElementFactory {

    fun createClassName(project: Project, name: String): PsiElement {
        return createFile(project, "extends a\nclass_name $name\n").lastChild.firstChild.nextSibling.nextSibling.firstChild;
    }

    fun inheritanceName(project: Project, name: String): PsiElement {
        val asd = createFile(project, "extends $name\n");
        return createFile(project, "extends $name\n").firstChild.nextSibling.firstChild;
    }

    private fun createFile(project: Project, text: String) =
            PsiFileFactory.getInstance(project).createFileFromText("dum.gd", GdFileType.INSTANCE, text) as GdFile
}