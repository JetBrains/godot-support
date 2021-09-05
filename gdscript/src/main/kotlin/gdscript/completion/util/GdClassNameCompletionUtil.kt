package gdscript.completion.util

import com.intellij.codeInsight.lookup.LookupElement
import gdscript.GdIcon
import gdscript.completion.GdLookup
import gdscript.psi.GdClassNaming

object GdClassNameCompletionUtil {

    fun toLookup(className: GdClassNaming): LookupElement =
        GdLookup.create(className.classname,
            priority = GdLookup.USER_DEFINED,
            icon = GdIcon.getEditorIcon(className.classname),
            //typed = className.parentName
        )

}
