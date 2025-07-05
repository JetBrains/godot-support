package tscn.psi.search

import com.intellij.model.PsiElementUsageInfo
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.PsiSearchHelper
import com.intellij.psi.search.PsiSearchScopeUtil
import com.intellij.psi.search.UsageSearchContext.IN_CODE
import com.intellij.usageView.UsageInfo
import gdscript.utils.ProjectUtil.contentScope
import gdscript.utils.VirtualFileUtil.resourcePath
import tscn.TscnFileType
import tscn.psi.search.processor.TscnResourceHeaderCollectingProcessor

class TscnResourceSearcher(project: Project) {

    private val searchHelper = PsiSearchHelper.getInstance(project)
    private val searchScope = PsiSearchScopeUtil.restrictScopeTo(project.contentScope(), TscnFileType)

    fun listReference(file: VirtualFile): List<UsageInfo> {
        val searchFor = "path=\"${file.resourcePath()}\""
        val processor = TscnResourceHeaderCollectingProcessor()

        searchHelper.processElementsWithWord(processor, searchScope, searchFor, IN_CODE, true)

        val checked = mutableSetOf<String>()
        val result = mutableListOf<UsageInfo>()
        processor.result.forEach {
            val resource = it.containingFile.virtualFile.resourcePath()
            if (!checked.contains(resource)) {
                result.add(PsiElementUsageInfo(it))
            }
            checked.add(resource)
        }

        return result
    }

}
