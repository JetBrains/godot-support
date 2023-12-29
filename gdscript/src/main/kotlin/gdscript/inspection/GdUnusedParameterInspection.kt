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
                val owner = PsiTreeUtil.getParentOfType(
                    o,
                    GdMethodDeclTl::class.java,
                    GdFuncDeclEx::class.java,
                    GdSignalDeclTl::class.java,
                )

                if (owner is GdSignalDeclTl) return
                process(o, o.varNmi, holder)
            }
        }
    }

}
