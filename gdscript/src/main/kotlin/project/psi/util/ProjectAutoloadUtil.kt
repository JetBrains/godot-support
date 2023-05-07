package project.psi.util

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import gdscript.index.impl.GdFileResIndex
import gdscript.utils.VirtualFileUtil.getPsiFile
import project.index.impl.ProjectSectionIndex
import project.psi.ProjectData
import project.psi.model.GdAutoload

object ProjectAutoloadUtil {

    fun listGlobals(element: PsiElement): List<GdAutoload> {
        return all(element).mapNotNull {
            var path = it.value
            if (path.startsWith("\"*")) {
                path = path.substring(2, path.length - 1)
                val resource = GdFileResIndex.INSTANCE.getFiles(path, element.project).firstOrNull()
                val file = resource?.getPsiFile(element.project)
                if (file != null) GdAutoload(it.key, file)
                else null
            } else {
                null
            }
        }
    }

    fun findFromResource(name: String, element: PsiElement): PsiFile? {
        val project = element.project
        val globalName = "\"*${name.substring(1)}"
        var path = all(element).find { it.value == globalName }?.value ?: return null
        path = path.substring(2, path.length - 1)

        return GdFileResIndex.INSTANCE.getFiles(path, project).firstOrNull()?.getPsiFile(project)
    }

    fun findFromAlias(name: String, element: PsiElement): PsiFile? {
        val project = element.project
        var path = all(element).find { it.key == name }?.value ?: return null
        path = path.substring(2, path.length - 1)

        return GdFileResIndex.INSTANCE.getFiles(path, project).firstOrNull()?.getPsiFile(project)
    }

    private fun all(element: PsiElement): List<ProjectData> {
        val section = ProjectSectionIndex.INSTANCE.getGlobally(ProjectSectionIndex.AUTOLOAD_SECTION, element)
            .firstOrNull() ?: return emptyList()
        return PsiTreeUtil.getStubChildrenOfTypeAsList(section, ProjectData::class.java)
    }

}
