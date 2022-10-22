package common.index

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
        return FileBasedIndex.getInstance().getContainingFiles(id, key, GlobalSearchScope.allScope(project));
    }

    override fun getName(): ID<K, Void> {
        return id;
    }

}
