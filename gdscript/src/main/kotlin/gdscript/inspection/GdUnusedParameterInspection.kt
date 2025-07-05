package gdscript.inspection

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.psi.*

class GdUnusedParameterInspection : GdUnusedInspection() {

    override val description: String = "Unused parameter"
    override val text: String = "Remove [{NAME}] parameter"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : GdVisitor() {
            override fun visitParam(o: GdParam) {
                // ignore unused parameters
                if (o.varNmi.name.startsWith("_")) return

                val owner = PsiTreeUtil.getParentOfType(
                    o,
                    GdMethodDeclTl::class.java,
                    GdFuncDeclEx::class.java,
                    GdSignalDeclTl::class.java,
                )

                if (owner is GdSignalDeclTl) return
                // only check for references in the owning element
                if (owner == null || anyReference(o.varNmi, owner.useScope)) return

                registerUnusedWithUnderscoreFix(o, o.varNmi, holder)
            }
        }
    }

}
