package gdscript.inspection

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdMethodIdNmi
import gdscript.psi.GdVisitor
import gdscript.psi.utils.GdClassUtil
import gdscript.psi.utils.GdInheritanceUtil
import gdscript.psi.utils.PsiGdMethodIdUtil
import gdscript.utils.ProjectUtil.globalSearchScope
import tscn.psi.utils.TscnConnectionUtil.getMethod
import tscn.psi.utils.TscnResourceUtil.findConnectionsForResource
import tscn.psi.utils.TscnResourceUtil.findTscnByResources

class GdUnusedMethodInspection : GdUnusedInspection() {

    override val description: String = "Unused method"
    override val text: String = "Remove [{NAME}] method"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : GdVisitor() {
            override fun visitMethodIdNmi(o: GdMethodIdNmi) {
                // ignore constructor
                if (PsiGdMethodIdUtil.isConstructor(o)) return
                // ignore warnings for inherited methods
                if (methodExistsInParentStructure(o)) return
                // check first on the scope of the owner
                val owner = GdClassUtil.getOwningClassElement(o)
                if (anyReference(o, owner.useScope)) return
                // check for global scope
                if (anyReference(o, o.project.globalSearchScope())) return
                // check for tscn references
                if (methodConnectedInTscnResource(o)) return

                registerUnused(o.parent, o, holder)
            }
        }
    }

    private fun methodExistsInParentStructure(element: GdMethodIdNmi) : Boolean {

        var par = element.parent
        while (par != null) {
            if (par.children
                    .filterIsInstance<GdMethodDeclTl>()
                    .any { method -> method.name == element.name }) return true

            par = GdInheritanceUtil.getExtendedElement(par)
        }

        return false
    }

    private fun methodConnectedInTscnResource(element: GdMethodIdNmi) : Boolean {
        val tscnResources = findTscnByResources(element)
        tscnResources.forEach{ resource ->
            var connections = findConnectionsForResource(resource)
            if (connections.filter { con -> getMethod(con) == element.name }.any()) {
                return true
            }
        }
        return false
    }
}
