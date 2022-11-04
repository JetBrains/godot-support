package gdscript.utils

import com.intellij.history.core.Paths
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectLocator
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import gdscript.psi.utils.PsiGdResourceUtil

object VirtualFileUtil {

    fun VirtualFile.localPath(): String {
        val project = ProjectLocator.getInstance().guessProjectForFile(this) ?: return "";

        return Paths.relativeIfUnder(this.path.removePrefix("file://"), project.basePath) ?: "";
    }

    fun VirtualFile.resourcePath(): String {
        return PsiGdResourceUtil.resourcePath(this);
    }

    fun VirtualFile.getPsiFile(project: Project): PsiFile? {
        return PsiManager.getInstance(project).findFile(this);
    }

}
