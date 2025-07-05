package project.index.impl.utils

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.util.indexing.FileBasedIndex
import project.ProjectFileType

object ProjectFileInputFilter : FileBasedIndex.InputFilter {

    override fun acceptInput(file: VirtualFile): Boolean {
        return file.name.endsWith(ProjectFileType.defaultExtension)
    }

}
