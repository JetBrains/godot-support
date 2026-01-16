package gdscript.inspection

import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.childrenOfType
import gdscript.GdScriptBundle
import gdscript.inspection.fixes.GdRemoveElementFix
import gdscript.inspection.util.ProblemsHolderExtension.registerUnused
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdClassNaming
import gdscript.psi.GdInheritance
import gdscript.psi.GdVisitor
import gdscript.utils.ProjectUtil.contentScope
import tscn.psi.utils.TscnResourceUtil

class GdUnusedClassInspection : GdUnusedInspection() {

    override val description: String = GdScriptBundle.message("inspection.unused.class.description")
    override fun removeText(symbol: String): String {
        return GdScriptBundle.message("inspection.unused.class.text", symbol)
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : GdVisitor() {

            // support inner classes
            override fun visitClassDeclTl(o: GdClassDeclTl) {
                // check if there are class references
                if (o.classNameNmi == null || anyReference(o.classNameNmi!!, holder.project.contentScope())) return
                if (anyReference(o.containingFile, holder.project.contentScope())) return

                registerUnused(o, o.classNameNmi!!, holder)
            }

            // support root class
            override fun visitClassNaming(o: GdClassNaming) {
                // check if there are class references
                if (o.classNameNmi == null || anyReference(o.classNameNmi!!, holder.project.contentScope())) return
                //  check if file is linked to any scene
                val containingFile = o.containingFile
                if (TscnResourceUtil.findTscnByResources(containingFile).any()) return
                if (anyReference(containingFile, holder.project.contentScope())) return

                registerUnused(containingFile, o.classNameNmi!!, holder)
            }

            // Support root without explicit class_name
            override fun visitInheritance(o: GdInheritance) {
                // Don't do a double check if there is an explicit class_name use that
                if (o.parent.childrenOfType<GdClassNaming>().any() || o.parent is GdClassDeclTl) return
                //  Check if file is linked to any scene
                val containingFile = o.containingFile
                if (TscnResourceUtil.findTscnByResources(containingFile).any()) return
                if (anyReference(containingFile, holder.project.contentScope())) return

                holder.registerUnused(
                    o, description,
                    GdRemoveElementFix(containingFile, removeText(containingFile.name))
                )
            }
        }
    }

}
