package common.index

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.ScalarIndexExtension

abstract class ScalarIndexExtensionExt<K> : ScalarIndexExtension<K>() {

    fun getFiles(key: K, project: Project): Collection<VirtualFile> {
        return FileBasedIndex.getInstance().getContainingFiles(name, key, GlobalSearchScope.allScope(project));
    }

}
