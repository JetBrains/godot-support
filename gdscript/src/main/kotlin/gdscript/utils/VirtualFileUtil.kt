package gdscript.utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectLocator
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import gdscript.psi.utils.PsiGdResourceUtil
import java.nio.file.Path
import kotlin.io.path.pathString
import kotlin.io.path.relativeToOrNull

object VirtualFileUtil {

    fun VirtualFile.localPath(): String {
        val project = ProjectLocator.getInstance().guessProjectForFile(this) ?: return ""
        val basePath = project.getMainProjectBasePath() ?: return ""
        return fileSystem.getNioPath(this)?.relativeToOrNull(basePath)?.pathString ?: return ""
        // TODO this causes issues - I can't query the Fileindex when indexing... but how to have multiple projects?
        // val projectRoot = ProjectRootFileIndex.getProjectRoot(path, project)
    }

    fun VirtualFile.localParentPath(): String {
        val project = ProjectLocator.getInstance().guessProjectForFile(this) ?: return ""
        val path = "${project.name}/${localPath()}"

        return path.substringBeforeLast('/')
    }

    fun VirtualFile.resourcePath(withPrefix: Boolean = true): String {
        return PsiGdResourceUtil.resourcePath(this, withPrefix)
    }

    fun VirtualFile.getPsiFile(element: PsiElement): PsiFile? {
        return this.getPsiFile(element.project)
    }

    fun VirtualFile.getPsiFile(project: Project): PsiFile? {
        return PsiManager.getInstance(project).findFile(this)
    }

}
