package gdscript.completion.utils

import com.intellij.codeInsight.lookup.LookupElement
import gdscript.GdIcon
import gdscript.completion.GdLookup
import gdscript.psi.GdConstDeclSt
import gdscript.psi.GdConstDeclTl

object GdConstCompletionUtil {

    fun lookup(constant: GdConstDeclTl): LookupElement =
        GdLookup.create(constant.constName.orEmpty(),
            icon = GdIcon.getEditorIcon(GdIcon.CONST_MARKER),
            typed = constant.returnType,
            priority = GdLookup.USER_DEFINED,
        )

    fun lookup(constant: GdConstDeclSt): LookupElement =
        GdLookup.create(constant.name,
            icon = GdIcon.getEditorIcon(GdIcon.CONST_MARKER),
            typed = constant.returnType,
            priority = GdLookup.LOCAL_USER_DEFINED,
        )

}
