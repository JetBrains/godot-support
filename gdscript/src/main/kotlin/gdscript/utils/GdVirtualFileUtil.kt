package gdscript.utils

import com.intellij.history.core.Paths
import com.intellij.openapi.project.ProjectLocator
import com.intellij.openapi.vfs.VirtualFile

object GdVirtualFileUtil {

    fun VirtualFile.localPath(): String {
        val project = ProjectLocator.getInstance().guessProjectForFile(this) ?: return "";

        return Paths.relativeIfUnder(this.path, project.basePath) ?: "";
    }

}
