package gdscript.psi.utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.readText
import com.intellij.openapi.vfs.writeText
import com.intellij.psi.search.FilenameIndex
import gdscript.utils.ProjectUtil.contentScope

object GdCfgUtil {

    private const val CFG_FILE_NAME = "global_script_class_cache.cfg"

    fun renameValue(project: Project, oldName: String, newName: String) {
        val file = this.getConfig(project) ?: return
        var content = file.readText()
        content = content.replace("\"$oldName\"", "\"$newName\"")
        file.writeText(content)
    }

    private fun getConfig(project: Project): VirtualFile? {
        return FilenameIndex.getVirtualFilesByName(CFG_FILE_NAME, project.contentScope()).firstOrNull()
    }

}
