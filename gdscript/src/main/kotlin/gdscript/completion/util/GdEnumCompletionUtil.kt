package gdscript.completion.util

import com.intellij.codeInsight.lookup.LookupElement
import gdscript.GdIcon
import gdscript.GdKeywords
import gdscript.completion.GdLookup
import gdscript.psi.GdEnumDeclTl

object GdEnumCompletionUtil {

    fun lookup(enum: GdEnumDeclTl): Array<LookupElement> {
        val name = enum.enumDeclNmi;

        return if (name != null) {
            arrayOf(createElement(name.name));
        } else {
            enum.values.map {
                createElement(it.key, it.value)
            }.toTypedArray();
        }
    }

    private fun createElement(name: String, value: Int? = null): LookupElement {
        return GdLookup.create(name,
            icon = GdIcon.getEditorIcon(GdIcon.ENUM_MARKER),
            typed = if (value != null) GdKeywords.INT else {""},
            tail = if (value != null) "($value)" else {""},
            priority = GdLookup.USER_DEFINED,
        )
    }

}
