package tscn.psi.search

import com.intellij.openapi.project.Project
import com.intellij.usageView.UsageInfo
import gdscript.psi.GdMethodIdNmi

class TscnMethodSearcher(val method: GdMethodIdNmi, project: Project) : AbstractTscnSearcher(project, method.containingFile) {

    fun anyMethodReference() : Boolean {
        // match methods on the "to" field
        if (listConnectionReference("method=\"${method.name}\"", true) { header -> header.to }.any()) {
            return true
        }
        // or search for matches in an animation track
        return listAnimationReference("\"method\": &\"${method.name}\"", true, "method").any()
    }

    /**
     * The connections and the animation method tracks that call the method, as one [UsageInfo] per
     * scene paragraph. Find Usages reads the PolySymbol own references instead - see
     * `tscn.psi.impl.TscnNamedElementImpl.getOwnReferences`. This scan stays for the gutter marker
     * and the unused method inspection, which need the reverse direction that an own reference does
     * not give.
     */
    fun listMethodReferences() : List<UsageInfo> {
        val res = mutableListOf<UsageInfo>()
        res.addAll(listConnectionReference("method=\"${method.name}\"", false) { header -> header.to })
        res.addAll(listAnimationReference("\"method\": &\"${method.name}\"", false, "method"))
        return res
    }

}
