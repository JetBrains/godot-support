package tscn.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.PsiTreeUtil
import tscn.TscnFileType

/**
 * Creates PsiElements for refactoring purposes
 */
object TscnElementFactory {

    fun tscnNodeHeaderValueVal(project: Project, name: String): PsiElement {
        val file = createFile(project, "[ext_resource path=\"$name\"]\n")

        return PsiTreeUtil.findChildOfType(file, TscnHeaderValueVal::class.java)!!.firstChild
    }

    /**
     * Create and return a [TscnDataLineNm] element with the given [name].
     *
     * @param project   The project in which to create the element
     * @param name      The name of the new element
     *
     * @return A [TscnDataLineNm] element with the given name.
     */
    fun tscnDataLineNm(project: Project, name: String): PsiElement {
        val file = createFile(project, "[resource]\n$name = \"\"")

        return PsiTreeUtil.findChildOfType(file, TscnDataLineNm::class.java)!!.firstChild
    }

    private fun createFile(project: Project, text: String) =
        PsiFileFactory.getInstance(project).createFileFromText("dum.tscn", TscnFileType, text) as TscnFile

}
