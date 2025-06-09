package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.jetbrains.rider.godot.community.gdscript.GdFileType

object GdFileUtil {

    fun listTraits(project: Project): List<VirtualFile> {
        return FileTypeIndex.getFiles(GdFileType, GlobalSearchScope.allScope(project))
            .filter { it.nameWithoutExtension.lowercase().endsWith("trait") };
    }

}
