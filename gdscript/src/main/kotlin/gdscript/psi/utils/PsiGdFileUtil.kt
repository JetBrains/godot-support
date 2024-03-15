package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import java.io.File

object PsiGdFileUtil {

    fun filename(file: PsiFile): String {
        val name = file.name

        return name.substring(0, name.lastIndexOf("."))
    }

    @Deprecated("resource util?")
    fun filepath(element: PsiElement): String {
        val basePath = element.project.basePath ?: return ""
        val file = element.containingFile.originalFile
        val directory = file.containingDirectory?.toString()?.removePrefix("PsiDirectory:") ?: return ""

        var d = ""
        if (directory.length > basePath.length) {
            d = directory.substring(basePath.length + 1) + File.separator
        }
        val path = "$d${file.name}"

        return path.replace(File.separator, "/")
    }

}
