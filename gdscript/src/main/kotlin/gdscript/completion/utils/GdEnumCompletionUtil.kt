package gdscript.completion.utils

import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdIcon
import gdscript.GdKeywords
import gdscript.completion.GdLookup
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdEnumValue

object GdEnumCompletionUtil {

    fun lookup(name: String, value: Long? = null): LookupElement {
        return GdLookup.create(
            name,
            icon = GdIcon.getEditorIcon(GdIcon.ENUM_MARKER),
            typed = GdKeywords.INT,
            tail = if (value != null) "($value)" else {
                ""
            },
            priority = GdLookup.USER_DEFINED,
        )
    }

    fun lookup(element: GdEnumValue): LookupElement {
        val decl = PsiTreeUtil.getParentOfType(element, GdEnumDeclTl::class.java);
        val values = decl?.values;
        val name = element.enumValueNmi.name;

        return GdLookup.create(
            name,
            icon = GdIcon.getEditorIcon(GdIcon.ENUM_MARKER),
            typed = GdKeywords.INT,
            tail = if (values != null) "(${values[name] ?: ""})" else {
                ""
            },
            priority = GdLookup.BUILT_IN,
        )
    }

    fun GdEnumDeclTl.lookup(): LookupElement? {
        if (name.isBlank()) return null;

        return GdLookup.create(
            name,
            icon = GdIcon.getEditorIcon(GdIcon.ENUM_MARKER),
            typed = GdKeywords.INT,
            priority = GdLookup.LOCAL_USER_DEFINED,
        )
    }

    fun GdEnumDeclTl.preview(): String {
        var name = this.name
        if (name.isNotBlank()) name = "$name "
        val sb = StringBuilder("enum $name{\n")
        this.values.forEach {
            sb.appendLine("    ${it.key} = ${it.value},")
        }
        sb.append("}")

        return sb.toString()
    }

}
