package tscn.psi.search

import com.intellij.usageView.UsageInfo
import gdscript.psi.GdSignalDeclTl

class TscnSignalSearcher(val signal: GdSignalDeclTl) : AbstractTscnSearcher(signal.project, signal.containingFile) {

    fun anySignalReference() : Boolean {
        // match signals on the "from" field
        return listConnectionReference("signal=\"${signal.name}\"", true) { header -> header.from }.any()
    }

    fun listSignalReferences() : List<UsageInfo> {
        return listConnectionReference("signal=\"${signal.name}\"", false) { header -> header.from }
    }
}