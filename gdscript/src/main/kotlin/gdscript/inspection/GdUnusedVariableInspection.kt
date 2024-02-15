package gdscript.inspection

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdConstDeclSt
import gdscript.psi.GdVarDeclSt
import gdscript.psi.GdVisitor
import gdscript.utils.ProjectUtil.contentScope

class GdUnusedVariableInspection : GdUnusedInspection() {

    override val description: String = "Unused variable"
    override val text: String = "Remove [{NAME}] variable"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : GdVisitor() {
            override fun visitVarDeclSt(o: GdVarDeclSt) {
                if (o.varNmi == null || anyReference(o.varNmi!!, o.project.contentScope())) return

                registerUnused(o, o.varNmi!!, holder);
            }

            override fun visitConstDeclSt(o: GdConstDeclSt) {
                if (o.varNmi == null || anyReference(o.varNmi!!, o.project.contentScope())) return

                registerUnused(o, o.varNmi!!, holder);
            }
        }
    }

}
