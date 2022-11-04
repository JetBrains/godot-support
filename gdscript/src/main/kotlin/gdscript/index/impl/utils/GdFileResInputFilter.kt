package gdscript.index.impl.utils

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.indexing.FileBasedIndex
import gdscript.utils.VirtualFileUtil.localPath

object GdFileResInputFilter : FileBasedIndex.InputFilter {

    val IGNORE_SUFFIX = arrayOf(
        ".import",
        ".godot",
    );

    val IGNORE_PREFFIX = arrayOf(
        ".",
    );

    override fun acceptInput(file: VirtualFile): Boolean {
        val filename = file.localPath();
        if (filename.isBlank()) {
            return false;
        }

        return IGNORE_SUFFIX.find {
            filename.endsWith(it)
        } == null && IGNORE_PREFFIX.find {
            filename.startsWith(it)
        } == null;
    }

}
