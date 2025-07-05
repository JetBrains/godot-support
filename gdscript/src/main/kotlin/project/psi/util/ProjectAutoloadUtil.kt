package project.psi.util

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.descendantsOfType
import gdscript.GdFileType
import gdscript.index.impl.GdFileResIndex
import gdscript.utils.VirtualFileUtil.getPsiFile
import project.index.impl.ProjectSectionIndex
import project.psi.ProjectData
import project.psi.model.GdAutoload
import tscn.TscnFileType
import tscn.psi.TscnNodeHeader
import tscn.psi.utils.TscnNodeUtil
import tscn.psi.utils.TscnNodeUtil.getScriptResource

object ProjectAutoloadUtil {

    fun listGlobals(project: Project): List<GdAutoload> {
        return all(project).mapNotNull {
            var path = it.value
            if (path.startsWith("\"*")) {
                path = path.substring(2, path.length - 1)
                val resource = GdFileResIndex.getFiles(path, project).firstOrNull()
                val file = resource?.getPsiFile(project)
                if (file == null) null
                else if (file.fileType is GdFileType) GdAutoload(it.key, file)
                else if (file.fileType is TscnFileType) findGdFileForTscnFile(project, it.key, file)
                else null
            } else {
                null
            }
        }
    }

    fun findFromAlias(name: String, element: PsiElement): PsiFile? {
        if (name.isEmpty()) return null
        return listGlobals(element.project).find { it.key == name }?.element
    }

    private fun all(project: Project): List<ProjectData> {
        val section = ProjectSectionIndex.INSTANCE.getGlobally(ProjectSectionIndex.AUTOLOAD_SECTION, project)
            .firstOrNull() ?: return emptyList()
        return PsiTreeUtil.getStubChildrenOfTypeAsList(section, ProjectData::class.java)
    }

    private fun findGdFileForTscnFile(project: Project, key: String, tscnFile: PsiFile): GdAutoload? {
        tscnFile.descendantsOfType<TscnNodeHeader>().forEach { header ->
            if (TscnNodeUtil.hasScript(header)) {
                val resource = GdFileResIndex.getFiles(getScriptResource(header), project).firstOrNull()
                val gdFile = resource?.getPsiFile(project)
                if (gdFile != null && gdFile.fileType is GdFileType) return GdAutoload(key, gdFile)
            }
        }
        return null
    }
}
