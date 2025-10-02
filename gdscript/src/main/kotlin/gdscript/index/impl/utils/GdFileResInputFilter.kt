package gdscript.index.impl.utils

import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.IndexedFile

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

    fun validResource(file: IndexedFile): Boolean {
        return IGNORE_SUFFIX.none { file.fileName.endsWith(it) }
            && IGNORE_PREFFIX.none { file.fileName.startsWith(it) }
    }

    override fun acceptInput(file: IndexedFile): Boolean {
        return validResource(file)
    }
}
