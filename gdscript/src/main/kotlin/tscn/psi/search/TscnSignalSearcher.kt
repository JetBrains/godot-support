package tscn.psi.search

import com.intellij.openapi.project.Project
import gdscript.psi.GdSignalIdNmi

class TscnSignalSearcher(val signal: GdSignalIdNmi, project: Project) : AbstractTscnSearcher(project, signal.containingFile) {

    fun anySignalReference() : Boolean {
        // match signals on the "from" field
        return listConnectionReference("signal=\"${signal.name}\"", true) { header -> header.from }.any()
    }
}
