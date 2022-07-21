package gdscript.completion.util

import com.intellij.codeInsight.lookup.LookupElement
import gdscript.GdIcon
import gdscript.completion.GdLookup
import gdscript.psi.GdConstDeclTl

object GdConstCompletionUtil {

    fun lookup(constant: GdConstDeclTl): LookupElement =
        GdLookup.create(constant.constName.orEmpty(),
            icon = GdIcon.getEditorIcon(GdIcon.CONST_MARKER),
            typed = constant.returnType,
            priority = GdLookup.USER_DEFINED,
        )

}
