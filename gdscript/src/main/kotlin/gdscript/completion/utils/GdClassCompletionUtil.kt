package gdscript.completion.utils

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.openapi.project.Project
import gdscript.GdIcon
import gdscript.completion.GdLookup
import gdscript.index.impl.GdClassNamingIndex

object GdClassCompletionUtil {

    fun allClasses(project: Project): Array<LookupElement> {
        return GdClassNamingIndex.getAllKeys(project).mapNotNull {
            if (it != "") {
                GdLookup.create(
                    it,
                    priority = GdLookup.USER_DEFINED,
                    icon = GdIcon.getEditorIcon(it),
                )
            } else {
                null
            }
        }.toTypedArray();
    }

}
