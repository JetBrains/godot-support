package gdscript.inspection

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdParam
import gdscript.psi.GdVisitor

class GdUnusedParameterInspection : GdUnusedInspection() {

    override val description: String = "Unused parameter"
    override val text: String = "Remove [{NAME}] parameter"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : GdVisitor() {
            override fun visitParam(o: GdParam) {
                process(o, o.varNmi, holder)
            }
        }
    }

}
