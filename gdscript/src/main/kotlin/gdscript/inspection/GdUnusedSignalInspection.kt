package gdscript.inspection

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import gdscript.GdScriptBundle
import gdscript.psi.GdSignalDeclTl
import gdscript.psi.GdVisitor
import gdscript.utils.ProjectUtil.contentScope
import tscn.psi.search.TscnSignalSearcher

class GdUnusedSignalInspection : GdUnusedInspection() {

    override val description: String = GdScriptBundle.message("inspection.unused.signal.description")
    override fun removeText(symbol: String): String {
        return GdScriptBundle.message("inspection.unused.signal.text", symbol)
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : GdVisitor() {
            override fun visitSignalDeclTl(o: GdSignalDeclTl) {
                // check for content scope
                if (o.signalIdNmi == null || anyReference(o.signalIdNmi!!, holder.project.contentScope())) return
                // check for tscn references
                if (TscnSignalSearcher(o.signalIdNmi!!, holder.project).anySignalReference()) return

                registerUnused(o, o.signalIdNmi!!, holder)
            }
        }
    }
}