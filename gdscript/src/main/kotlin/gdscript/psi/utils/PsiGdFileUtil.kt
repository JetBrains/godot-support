package gdscript.psi.utils

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import gdscript.utils.getMainProjectBasePath
import java.io.File
import kotlin.io.path.pathString

object PsiGdFileUtil {

    fun filename(file: PsiFile): String {
        val name = file.name

        return name.substring(0, name.lastIndexOf("."))
    }

    @Deprecated("resource util?")
    fun filepath(element: PsiElement): String {
        val basePath = element.project.getMainProjectBasePath() ?: return ""
        val file = element.containingFile.originalFile
        val directory = file.containingDirectory?.toString()?.removePrefix("PsiDirectory:") ?: return ""

        var d = ""
        if (directory.length > basePath.pathString.length) {
            d = directory.substring(basePath.pathString.length + 1) + File.separator
        }
        val path = "$d${file.name}"

        return path.replace(File.separator, "/")
    }

}
