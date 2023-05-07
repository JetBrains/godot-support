package gdscript.codeInsight.renamer

import com.intellij.psi.PsiNamedElement
import com.intellij.refactoring.rename.naming.AutomaticRenamer
import com.intellij.refactoring.rename.naming.NameSuggester
import gdscript.utils.StringUtil.camelToSnakeCase

/**
 * Renames file when class_name is renamed
 */
class GdFileAutomaticRenamer : AutomaticRenamer {

    constructor(element: PsiNamedElement, newName: String) {
        myElements.add(element.containingFile)
        suggestAllNames(element.name, newName)
    }

    override fun getDialogTitle(): String {
        return "Rename Containing File"
    }

    override fun getDialogDescription(): String {
        return "Rename a containing with the following name"
    }

    override fun entityName(): String {
        return "Containing File"
    }

    override fun isSelectedByDefault(): Boolean {
        return true
    }

    override fun suggestNameForElement(
        element: PsiNamedElement,
        suggester: NameSuggester,
        newClassName: String,
        oldClassName: String,
    ): String {
        return "${newClassName.camelToSnakeCase()}.gd"
    }

}
