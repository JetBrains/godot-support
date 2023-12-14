package gdscript.inspection

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdMethodIdNmi
import gdscript.psi.GdVisitor

class GdUnusedMethodInspection : GdUnusedInspection() {

    override val description: String = "Unused method"
    override val text: String = "Remove [{NAME}] method"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : GdVisitor() {
            override fun visitMethodIdNmi(o: GdMethodIdNmi) {
                process(o.parent, o, holder)
            }
        }
    }

}
