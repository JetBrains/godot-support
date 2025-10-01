package gdscript.completion.utils

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.project.Project
import gdscript.GdIcon
import gdscript.completion.GdLookup
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.GdClassDeclTl

/**
 * GdClassNaming & GdClassDeclTl look-ups
 */
object GdClassCompletionUtil {

    fun allRootClasses(project: Project): Array<LookupElement> {
        return GdClassNamingIndex.INSTANCE.getNonEmptyKeys(project).map {
            GdLookup.create(it, priority = GdLookup.USER_DEFINED, icon = GdIcon.getEditorIcon(it))
        }.toTypedArray()
    }

    fun GdClassDeclTl.lookup(): LookupElement {
        val name = this.name
        return GdLookup.create(name, priority = GdLookup.USER_DEFINED, icon = GdIcon.getEditorIcon(name))
    }

}
