package gdscript.index.impl

import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.*
import com.intellij.util.io.EnumeratorStringDescriptor
import com.intellij.util.io.KeyDescriptor
import common.index.ScalarIndexExtensionExt
import gdscript.index.Indices
import gdscript.index.impl.utils.GdFileResIndexer
import gdscript.index.impl.utils.GdFileResInputFilter

class GdFileResIndex : ScalarIndexExtensionExt<String>() {

    companion object {
        fun getFiles(key: String, project: Project): Collection<VirtualFile> {
            if (DumbService.isDumb(project)) return emptyList() // looks like inconsistent DumbAware check, why getNonEmptyKeys is not checking it?
            return FileBasedIndex.getInstance()
                .getContainingFiles(Indices.FILE_RES, key, GlobalSearchScope.allScope(project))
        }

        fun getFiles(key: String, element: PsiElement): Collection<VirtualFile> {
            return getFiles(key, element.project)
        }

        fun getNonEmptyKeys(element: PsiElement): List<String> {
            return getNonEmptyKeys(element.project)
        }

        fun getNonEmptyKeys(project: Project): List<String> {
            val inst = FileBasedIndex.getInstance()
            val scope = GlobalSearchScope.allScope(project)

            return inst.getAllKeys(Indices.FILE_RES, project).mapNotNull {
                if (inst.getContainingFiles(Indices.FILE_RES, it, scope).isNotEmpty()) it
                else null
            }
        }
    }

    override val id: ID<String, Void> = Indices.FILE_RES

    override fun getIndexer(): DataIndexer<String, Void, FileContent> {
        return GdFileResIndexer
    }

    override fun getKeyDescriptor(): KeyDescriptor<String> {
        return EnumeratorStringDescriptor.INSTANCE
    }

    override fun getVersion(): Int {
        return Indices.VERSION
    }

    override fun getInputFilter(): FileBasedIndex.InputFilter {
        return GdFileResInputFilter
    }

    override fun dependsOnFileContent(): Boolean {
        return false
    }
}
