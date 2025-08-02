package gdscript.utils

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.util.io.toNioPathOrNull
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import gdscript.utils.VirtualFileUtil.resourcePath
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

        val thisPath = this.trim('"').toNioPathOrNull()?.normalize() ?: return this
        val dirPath = element.containingFile.originalFile.virtualFile.parent?.toNioPath() ?: return this

        FilenameIndex.getVirtualFilesByName(thisPath.name, GlobalSearchScope.allScope(project)).find {
            val itPath = it.toNioPath()
            try {
                val relative = dirPath.relativize(itPath)
                return@find relative == thisPath
            }
            catch (e: Exception) {
                thisLogger().trace(e)
            }
            false
        }?.let {
            return it.resourcePath()
        }

        return this
    }

}
