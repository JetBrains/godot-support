package project.index.impl.utils

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.indexing.FileBasedIndex
import gdscript.utils.VirtualFileUtil.localPath
import project.ProjectFileType

object ProjectFileInputFilter : FileBasedIndex.InputFilter {

    override fun acceptInput(file: VirtualFile): Boolean {
        val filename = file.localPath();
        if (filename.isBlank()) return false

        return filename.endsWith(ProjectFileType.defaultExtension)
    }

}
