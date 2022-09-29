package gdscript.completion.utils

import com.intellij.codeInsight.lookup.LookupElement
import gdscript.GdIcon
import gdscript.GdKeywords
import gdscript.completion.GdLookup

object GdEnumCompletionUtil {

    fun createElement(name: String, value: Int? = null): LookupElement {
        return GdLookup.create(name,
            icon = GdIcon.getEditorIcon(GdIcon.ENUM_MARKER),
            typed = if (value != null) GdKeywords.INT else {""},
            tail = if (value != null) "($value)" else {""},
            priority = GdLookup.USER_DEFINED,
        )
    }

}
