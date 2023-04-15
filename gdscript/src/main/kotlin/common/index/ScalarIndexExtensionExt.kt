package common.index

import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.ID
import com.intellij.util.indexing.ScalarIndexExtension

abstract class ScalarIndexExtensionExt<K : Any> : ScalarIndexExtension<K>() {

    // Cannot use directly getName due to OverrideOnly annotation api violation
    abstract val id: ID<K, Void>;

    fun getFiles(key: K, project: Project): Collection<VirtualFile> {
        if (DumbService.isDumb(project)) return emptyList();
        return FileBasedIndex.getInstance().getContainingFiles(id, key, GlobalSearchScope.allScope(project));
    }

    fun getNonEmptyKeys(project: Project): List<K> {
        val inst = FileBasedIndex.getInstance()
        val scope = GlobalSearchScope.allScope(project)

        return inst.getAllKeys(id, project).mapNotNull {
            if (inst.getContainingFiles(id, it, scope).isNotEmpty()) {
                it
            } else {
                null
            }
        }
    }

    override fun getName(): ID<K, Void> {
        return id
    }

    companion object {
        fun <K : Any> getNonEmptyKeys(id: ID<K, Void>, project: Project): List<K> {
            val instance = FileBasedIndex.getInstance()
            val scope = GlobalSearchScope.allScope(project)

            return instance.getAllKeys(id, project).mapNotNull {
                if (instance.getContainingFiles(id, it, scope).isNotEmpty())
                    it
                else null
            }
        }
    }

}
