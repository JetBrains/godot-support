package gdscript.inspection

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdMethodIdNmi
import gdscript.psi.GdVisitor
import gdscript.psi.utils.GdInheritanceUtil

class GdUnusedMethodInspection : GdUnusedInspection() {

    override val description: String = "Unused method"
    override val text: String = "Remove [{NAME}] method"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : GdVisitor() {
            override fun visitMethodIdNmi(o: GdMethodIdNmi) {
                if (methodExistsInParentStructure(o)) return

                process(o.parent, o, holder)
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
}
