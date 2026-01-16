package gdscript.codeInsight.renamer

import com.intellij.psi.PsiElement
import com.intellij.refactoring.rename.naming.AutomaticRenamer
import com.intellij.refactoring.rename.naming.AutomaticRenamerFactory
import com.intellij.usageView.UsageInfo
import gdscript.GdScriptBundle
import gdscript.psi.GdClassNameNmi
import gdscript.psi.GdClassNaming
import gdscript.psi.GdNamedElement

/**
 * Renames file when class_name is renamed
 */
class GdFileAutomaticRenamerFactory : AutomaticRenamerFactory {

    override fun isApplicable(element: PsiElement): Boolean {
        return element is GdClassNameNmi && element.parent is GdClassNaming
    }

    override fun getOptionName(): String {
        return GdScriptBundle.message("rename.containing.file.option.name")
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun setEnabled(enabled: Boolean) {
    }

    override fun createRenamer(
        element: PsiElement,
        newName: String,
        usages: MutableCollection<UsageInfo>,
    ): AutomaticRenamer {
        return GdFileAutomaticRenamer(element as GdNamedElement, newName)
    }
}
