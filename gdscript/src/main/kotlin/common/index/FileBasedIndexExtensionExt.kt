package common.index

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileBasedIndexExtension
import com.intellij.util.indexing.ID

abstract class FileBasedIndexExtensionExt<K, V> : FileBasedIndexExtension<K, V>() {

    // Cannot use directly getName due to OverrideOnly annotation api violation
    abstract val id: ID<K, V>;

    fun getFiles(key: K, project: Project): Collection<VirtualFile> {
        return FileBasedIndex.getInstance().getContainingFiles(id, key, GlobalSearchScope.allScope(project));
    }

    fun getValues(key: K, project: Project): Collection<V> {
        return FileBasedIndex.getInstance().getValues(id, key, GlobalSearchScope.allScope(project));
    }

    override fun getName(): ID<K, V> {
        return id;
    }

}
