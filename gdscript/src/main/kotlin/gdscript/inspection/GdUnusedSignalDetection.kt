package gdscript.inspection

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdSignalDeclTl
import gdscript.psi.GdVisitor
import gdscript.utils.ProjectUtil.globalSearchScope
import tscn.psi.utils.TscnConnectionUtil.getSignal
import tscn.psi.utils.TscnResourceUtil.findConnectionsForResource
import tscn.psi.utils.TscnResourceUtil.findTscnByResources

class GdUnusedSignalDetection : GdUnusedInspection() {

    override val description: String = "Unused signal"
    override val text: String = "Remove [{NAME}] signal"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : GdVisitor() {
            override fun visitSignalDeclTl(o: GdSignalDeclTl) {
                // check for global scope
                if (o.signalIdNmi == null || anyReference(o.signalIdNmi!!, o.project.globalSearchScope())) return
                // check for tscn files
                if (signalConnectedInTscnResource(o)) return

                registerUnused(o, o.signalIdNmi!!, holder);
            }

            private fun signalConnectedInTscnResource(element: GdSignalDeclTl) : Boolean {
                val tscnResources = findTscnByResources(element)
                tscnResources.forEach{ resource ->
                    var connections = findConnectionsForResource(resource)
                    if (connections.filter { con -> getSignal(con) == element.name }.any()) {
                        return true
                    }
                }
                return false
            }
        }
    }
}