package tscn.psi.search

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.usageView.UsageInfo
import gdscript.psi.GdMethodIdNmi
import gdscript.utils.VirtualFileUtil.resourcePath

class TscnMethodSearcher(val method: GdMethodIdNmi, project: Project) : AbstractTscnSearcher(project, method.containingFile) {

    fun anyMethodReference() : Boolean {
        // match methods on the "to" field
        if (listConnectionReference("method=\"${method.name}\"", true) { header -> header.to }.any()) {
            return true
        }
        // or search for matches in an animation track
        return listAnimationReference("\"method\": &\"${method.name}\"", true, "method").any();
    }

    fun listMethodReferences() : List<UsageInfo> {
        val res = mutableListOf<UsageInfo>()
        res.addAll(listConnectionReference("method=\"${method.name}\"", false) { header -> header.to })
        res.addAll(listAnimationReference("\"method\": &\"${method.name}\"", false, "method"))
        return res
    }

}
