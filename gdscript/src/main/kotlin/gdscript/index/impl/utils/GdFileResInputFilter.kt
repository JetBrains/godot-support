package gdscript.index.impl.utils

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.indexing.FileBasedIndex
import gdscript.utils.VirtualFileUtil.localPath

object GdFileResInputFilter : FileBasedIndex.InputFilter {

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

    override fun acceptInput(file: VirtualFile): Boolean {
        val filename = file.localPath()
        if (filename.isBlank()) {
            return false
        }

        return validResource(filename)
    }

}
