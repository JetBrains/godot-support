package gdscript.utils

import com.intellij.history.core.Paths
import com.intellij.openapi.project.ProjectLocator
import com.intellij.openapi.vfs.VirtualFile
import gdscript.psi.utils.PsiGdResourceUtil

object GdVirtualFileUtil {

    fun VirtualFile.localPath(): String {
        val project = ProjectLocator.getInstance().guessProjectForFile(this) ?: return "";

        return Paths.relativeIfUnder(this.path.removePrefix("file://"), project.basePath) ?: "";
    }

    fun VirtualFile.resourcePath(): String {
        return PsiGdResourceUtil.resourcePath(this);
    }

}
