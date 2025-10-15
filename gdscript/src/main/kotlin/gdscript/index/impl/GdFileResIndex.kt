package gdscript.index.impl

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import gdscript.index.impl.utils.GdFileResInputFilter


/* it is not a real index anymore*/
class GdFileResIndex {

    companion object {
        fun getFiles(key: String, project: Project): Collection<VirtualFile> {
            val relPath = key.removePrefix("res://")
            val contentRoots = ProjectRootManager.getInstance(project).getContentRoots()
            for (root in contentRoots) {
                val file = root.findFileByRelativePath(relPath)
                if (file != null) return listOf(file)
            }
            return emptyList()
        }

        fun getFiles(key: String, element: PsiElement): Collection<VirtualFile> {
            return getFiles(key, element.project)
        }

        fun getNonEmptyKeys(element: PsiElement): List<String> {
            return getNonEmptyKeys(element.project)
        }

        fun getNonEmptyKeys(project: Project): List<String> {
            val fileIndex = ProjectFileIndex.getInstance(project)
            val results = mutableListOf<String>()
            fileIndex.iterateContent(
                {
                    val res = fileIndex.getContentRootForFile(it)?.let { root ->
                                    VfsUtilCore.getRelativePath(it, root)?.let { path ->
                                        "res://$path"
                                    }
                                }
                    if (res != null) {
                        results.add(res)
                    }
                    true
                }, { GdFileResInputFilter.accept(it) })
            return results
        }
    }
}
