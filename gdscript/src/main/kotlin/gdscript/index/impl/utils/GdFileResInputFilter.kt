package gdscript.index.impl.utils

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileFilter

object GdFileResInputFilter : VirtualFileFilter {

    private val IGNORE_SUFFIX = arrayOf(
        ".import",
        ".godot",
    )

    private val IGNORE_PREFIX = arrayOf(
        ".",
    )

    fun validResource(filename: String): Boolean {
        return IGNORE_SUFFIX.none { filename.endsWith(it) }
               && IGNORE_PREFIX.none { filename.startsWith(it) }
    }

    fun validResource(file: VirtualFile): Boolean {
        return IGNORE_SUFFIX.none { file.name.endsWith(it) }
               && IGNORE_PREFIX.none { file.name.startsWith(it) }
    }

    override fun accept(file: VirtualFile): Boolean {
        if (file.isDirectory) return false
        return validResource(file)
    }
}
