package gdscript.inspection

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import gdscript.GdScriptBundle
import gdscript.psi.GdConstDeclSt
import gdscript.psi.GdVarDeclSt
import gdscript.psi.GdVisitor
import gdscript.utils.ProjectUtil.contentScope

class GdUnusedVariableInspection : GdUnusedInspection() {

    override val description: String = GdScriptBundle.message("inspection.unused.variable.description")

    override fun removeText(symbol: String): String {
        return GdScriptBundle.message("inspection.unused.variable.text", symbol)
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : GdVisitor() {
            override fun visitVarDeclSt(o: GdVarDeclSt) {
                if (o.varNmi == null || anyReference(o.varNmi!!, holder.project.contentScope())) return
                // Avoid unused highlighting - type is unresolved
                if (o.typed == null) return

                registerUnused(o, o.varNmi!!, holder)
            }

            override fun visitConstDeclSt(o: GdConstDeclSt) {
                if (o.varNmi == null || anyReference(o.varNmi!!, holder.project.contentScope())) return
                if (o.typed == null) return

                registerUnused(o, o.varNmi!!, holder)
            }
        }
    }
}
