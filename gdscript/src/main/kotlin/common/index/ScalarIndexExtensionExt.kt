package common.index

import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.indexing.FileBasedIndex
import com.intellij.util.indexing.ID
import com.intellij.util.indexing.ScalarIndexExtension

abstract class ScalarIndexExtensionExt<K : Any> : ScalarIndexExtension<K>() {

    // Cannot use directly getName due to OverrideOnly annotation api violation
    abstract val id: ID<K, Void>

    override fun getName(): ID<K, Void> {
        return id
    }

}
