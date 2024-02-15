package gdscript.inspection

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import gdscript.psi.GdSignalDeclTl
import gdscript.psi.GdVisitor
import gdscript.utils.ProjectUtil.contentScope
import tscn.psi.search.TscnSignalSearcher

class GdUnusedSignalDetection : GdUnusedInspection() {

    override val description: String = "Unused signal"
    override val text: String = "Remove [{NAME}] signal"

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : GdVisitor() {
            override fun visitSignalDeclTl(o: GdSignalDeclTl) {
                // check for content scope
                if (o.signalIdNmi == null || anyReference(o.signalIdNmi!!, o.project.contentScope())) return
                // check for tscn references
                if (TscnSignalSearcher(o.signalIdNmi!!).anySignalReference()) return

                registerUnused(o, o.signalIdNmi!!, holder);
            }
        }
    }
}