package tscn.psi.search

import com.intellij.model.PsiElementUsageInfo
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.usageView.UsageInfo
import gdscript.utils.ProjectUtil.contentScope
import gdscript.utils.VirtualFileUtil.resourcePath
import tscn.TscnFileType
import tscn.index.impl.TscnResourceIndex

class TscnResourceSearcher(val project: Project) {
    private val searchScope = GlobalSearchScope.getScopeRestrictedByFileTypes(project.contentScope(), TscnFileType)

    fun listReference(file: VirtualFile): List<UsageInfo> {
        val searchFor = file.resourcePath()

        // Use stub index to find all TscnResourceHeader elements with matching path
        val headers = TscnResourceIndex.INSTANCE.getScoped(searchFor, project, searchScope)
        return headers.map(::PsiElementUsageInfo)
    }
}
