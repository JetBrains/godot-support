package gdscript.inspection

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdMethodIdNmi
import gdscript.psi.GdVisitor
import gdscript.psi.utils.GdMethodUtil
import gdscript.psi.utils.PsiGdMethodIdUtil
import gdscript.utils.ProjectUtil.contentScope
import tscn.psi.search.TscnMethodSearcher

class GdUnusedMethodInspection : GdUnusedInspection() {

    override val description: String = "Unused method"
    override val text: String = "Remove [{NAME}] method"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : GdVisitor() {
            override fun visitMethodIdNmi(o: GdMethodIdNmi) {
                // ignore constructor
                if (PsiGdMethodIdUtil.isConstructor(o)) return
                // ignore warnings for inherited methods
                if (GdMethodUtil.findParentMethodRecursive(o, holder.project) != null) return
                // check for content scope
                if (anyReference(o, holder.project.contentScope())) return
                // check for tscn references
                if (TscnMethodSearcher(o, holder.project).anyMethodReference()) return

                registerUnused(o.parent, o, holder)
            }
        }
    }

}
