package tscn.index.impl

import com.intellij.openapi.project.DumbService
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.stubs.StubIndex
import com.intellij.psi.stubs.StubIndexKey
import common.index.StringStubIndexExtensionExt
import tscn.index.TscnIndices
import tscn.psi.TscnHeader

class TscnUidIndex : StringStubIndexExtensionExt<TscnHeader>() {

    companion object {
        val INSTANCE = TscnUidIndex()

        fun getFiles(uid: String, project: Project): Collection<VirtualFile> {
            if (DumbService.isDumb(project)) return emptyList()
            return StubIndex.getElements(
                TscnIndices.UID_INDEX, uid, project,
                GlobalSearchScope.allScope(project),
                TscnHeader::class.java,
            ).mapNotNull { it.containingFile?.virtualFile }
        }
    }

    override fun getKey(): StubIndexKey<String, TscnHeader> = TscnIndices.UID_INDEX

    override fun getVersion(): Int = TscnIndices.VERSION

}
