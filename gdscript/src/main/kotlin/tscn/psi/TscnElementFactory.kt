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

    private fun createFile(project: Project, text: String) =
        PsiFileFactory.getInstance(project).createFileFromText("dum.tscn", TscnFileType, text) as TscnFile

}
