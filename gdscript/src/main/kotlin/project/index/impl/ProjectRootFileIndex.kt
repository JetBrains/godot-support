package project.index.impl

import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.DataIndexer
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.FileContent
import com.intellij.util.indexing.ID
import com.intellij.util.io.EnumeratorStringDescriptor
import com.intellij.util.io.KeyDescriptor
import common.index.ScalarIndexExtensionExt
import project.index.ProjectIndices
import project.index.impl.utils.ProjectFileIndexer
import project.index.impl.utils.ProjectFileInputFilter
import com.intellij.history.core.Paths

class ProjectRootFileIndex : ScalarIndexExtensionExt<String>() {

    companion object {
        fun getNonEmptyKeys(project: Project): List<String> {
            return getNonEmptyKeys(ProjectIndices.PROJECT_ROOT_INDEX, project)
        }

        fun getProjectRoot(elementPath: String, project: Project): String {
            return getNonEmptyKeys(ProjectIndices.PROJECT_ROOT_INDEX, project)
                .find { Paths.isParent(it, elementPath) } ?: project.basePath ?: ""
        }
    }

    override val id: ID<String, Void>
        get() = ProjectIndices.PROJECT_ROOT_INDEX

    override fun getIndexer(): DataIndexer<String, Void, FileContent> {
        return ProjectFileIndexer
    }

    override fun getKeyDescriptor(): KeyDescriptor<String> {
        return EnumeratorStringDescriptor.INSTANCE
    }

    override fun getVersion(): Int {
        return ProjectIndices.VERSION
    }

    override fun getInputFilter(): FileBasedIndex.InputFilter {
        return ProjectFileInputFilter
    }

    override fun dependsOnFileContent(): Boolean {
        return false
    }

}
