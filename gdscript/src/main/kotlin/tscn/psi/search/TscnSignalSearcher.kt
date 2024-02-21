package tscn.psi.search

import com.intellij.openapi.project.Project
import com.intellij.usageView.UsageInfo
import gdscript.psi.GdSignalIdNmi

class TscnSignalSearcher(val signal: GdSignalIdNmi, project: Project) : AbstractTscnSearcher(project, signal.containingFile) {

    fun anySignalReference() : Boolean {
        // match signals on the "from" field
        return listConnectionReference("signal=\"${signal.name}\"", true) { header -> header.from }.any()
    }

    fun listSignalReferences() : List<UsageInfo> {
        return listConnectionReference("signal=\"${signal.name}\"", false) { header -> header.from }
    }
}