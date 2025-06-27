package gdscript.index.impl.utils

import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.IndexedFile
import gdscript.utils.RiderGodotSupportPluginUtil
import gdscript.utils.VirtualFileUtil.localPath
import gdscript.utils.hasCompletedTrue

object GdFileResInputFilter : FileBasedIndex.ProjectSpecificInputFilter {

    val IGNORE_SUFFIX = arrayOf(
        ".import",
        ".godot",
    )

    val IGNORE_PREFFIX = arrayOf(
        ".",
    )

    fun validResource(filename: String): Boolean {
        return IGNORE_SUFFIX.none { filename.endsWith(it) }
                && IGNORE_PREFFIX.none { filename.startsWith(it) }
    }

    override fun acceptInput(file: IndexedFile): Boolean {
        val virtualFile = file.file
        if (!virtualFile.isInLocalFileSystem) return false
        val project = file.project ?: return false

        // todo: hasCompletedTrue becomes true with a significant delay, so we might miss part of the files
        val isGodotProject = RiderGodotSupportPluginUtil.isGodotProject(project).hasCompletedTrue()
        //thisLogger().info("isGodotProject: $isGodotProject for ${virtualFile.path}")
        if (!isGodotProject) return false

        // todo: localPath has the same flaw about isGodotProject
        val filename = virtualFile.localPath()
        if (filename.isBlank()) {
            return false
        }

        return validResource(filename)
    }

}
