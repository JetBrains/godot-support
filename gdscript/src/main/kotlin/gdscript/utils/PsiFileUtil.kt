package gdscript.utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import gdscript.utils.VirtualFileUtil.resourcePath
import java.nio.file.Paths
import kotlin.io.path.name

object PsiFileUtil {

    fun PsiFile.isInSdk(): Boolean {
        return ProjectFileIndex.getInstance(this.project).isInLibrary(this.virtualFile)
    }

    /**
     *  In case of relative paths ("debug/frames.gd") resource is not indexed -> thus convert relative path to absolute "res://root/debug/frames.gg"
     */
    fun String.toAbsoluteResource(element: PsiElement, project: Project): String {
        if (this.startsWith("res://") || this.startsWith("\"res://")) return this

        val trimmed = this.trim('"')
        val fileName = trimmed.substringAfterLast("/")
        val myPath = Paths.get(element.containingFile.originalFile.virtualFile.parent.path)

        FilenameIndex.getVirtualFilesByName(Paths.get(fileName).name, GlobalSearchScope.allScope(project)).find {
            val itPath = Paths.get(it.path)
            try {
                val relative = myPath.relativize(itPath)
                return@find relative.toString().replace('\\', '/') == trimmed
            } catch (e: Exception) {
            }
            false
        }?.let {
            return it.resourcePath()
        }

        return this
    }

}
