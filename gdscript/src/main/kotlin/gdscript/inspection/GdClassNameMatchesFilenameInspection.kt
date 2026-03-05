package gdscript.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalQuickFixBackedByIntentionAction
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import gdscript.GdScriptBundle
import gdscript.action.quickFix.GdFileClassNameAction
import gdscript.inspection.util.ProblemsHolderExtension.registerWeakWarning
import gdscript.psi.GdClassNameNmi
import gdscript.psi.GdClassNaming
import gdscript.psi.GdVisitor
import gdscript.psi.utils.PsiGdFileUtil
import gdscript.utils.StringUtil.snakeToPascalCase

class GdClassNameMatchesFilenameInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : GdVisitor() {
            override fun visitClassNameNmi(className: GdClassNameNmi) {
                if (className.parent !is GdClassNaming) return

                val name = className.name
                val filename = PsiGdFileUtil.filename(className.containingFile).snakeToPascalCase()
                if (filename.lowercase() != name.lowercase()) {
                    holder.registerWeakWarning(
                        className,
                        GdScriptBundle.message("inspection.class.name.does.not.match.filename"),
                        LocalQuickFixBackedByIntentionAction(GdFileClassNameAction(filename, className)),
                    )
                }
            }
        }
    }
}
