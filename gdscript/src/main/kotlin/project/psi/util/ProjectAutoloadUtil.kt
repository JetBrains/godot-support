package project.psi.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.ModificationTracker
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.CachedValueProvider
import com.intellij.psi.util.CachedValuesManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.descendantsOfType
import com.jetbrains.rider.godot.community.gdscript.GdFileType
import com.jetbrains.rider.godot.community.gdscript.GdLanguage
import gdscript.index.impl.GdFileResIndex
import gdscript.utils.VirtualFileUtil.getPsiFile
import project.ProjectLanguage
import project.index.impl.ProjectSectionIndex
import project.psi.ProjectData
import project.psi.model.GdAutoload
import tscn.TscnFileType
import tscn.TscnLanguage
import tscn.psi.TscnNodeHeader
import tscn.psi.utils.TscnNodeUtil
import tscn.psi.utils.TscnNodeUtil.getScriptResource

object ProjectAutoloadUtil {

    fun listGlobals(project: Project): List<GdAutoload> {
        return CachedValuesManager.getManager(project).getCachedValue(project) {
            val result = computeGlobals(project)
            val dependencies = buildList {
                // Track PSI modifications in project file (contains autoload section)
                add(PsiManager.getInstance(project).modificationTracker.forLanguage(ProjectLanguage))
                // Track modifications in Gd and Tscn files referenced by autoloads
                add(PsiManager.getInstance(project).modificationTracker.forLanguage(GdLanguage))
                add(PsiManager.getInstance(project).modificationTracker.forLanguage(TscnLanguage))
            }
            val combinedTracker = ModificationTracker { dependencies.sumOf { it.modificationCount } }
            CachedValueProvider.Result.create(result, combinedTracker)
        }
    }

    private fun computeGlobals(project: Project): List<GdAutoload> {
        val t = all(project)
        return t.mapNotNull {
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
