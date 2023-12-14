package gdscript.inspection

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdConstDeclSt
import gdscript.psi.GdVarDeclSt
import gdscript.psi.GdVisitor

class GdUnusedVariableInspection : GdUnusedInspection() {

    override val description: String = "Unused variable"
    override val text: String = "Remove [{NAME}] variable"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : GdVisitor() {
            override fun visitVarDeclSt(o: GdVarDeclSt) {
                process(o, o.varNmi, holder)
            }

            override fun visitConstDeclSt(o: GdConstDeclSt) {
                process(o, o.varNmi, holder)
            }
        }
    }

}
