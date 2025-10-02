package gdscript.index.impl.utils

import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileContent
import java.util.*


object GdFileResIndexer : DataIndexer<String, Void, FileContent> {

    override fun map(inputData: FileContent): MutableMap<String, Void> {
        val vf = inputData.file
        val project = inputData.project // may be null in some contexts

        val resource: String? = if (project != null) {
            val index = ProjectFileIndex.getInstance(project)
            val contentRoot = index.getContentRootForFile(vf)
            when {
                contentRoot != null -> {
                    val rel = VfsUtilCore.getRelativePath(vf, contentRoot, '/')
                    rel?.let { "res://$it" }
                }
                else -> null
            }
        } else {
            // No project — safest is to skip indexing key to avoid wrong absolute mapping
            null
        }

        return if (resource != null) Collections.singletonMap(resource, null) else Collections.emptyMap()
    }
}
