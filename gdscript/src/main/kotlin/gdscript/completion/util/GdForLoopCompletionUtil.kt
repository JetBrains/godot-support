package gdscript.completion.util

import com.intellij.codeInsight.lookup.LookupElement
import gdscript.GdIcon
import gdscript.completion.GdLookup
import gdscript.psi.GdForSt
import gdscript.utils.StringUtils.parseFromSquare

object GdForLoopCompletionUtil {

    fun lookup(loop: GdForSt): LookupElement =
        GdLookup.create(loop.varNmi.name,
            icon = GdIcon.getEditorIcon(GdIcon.VAR_MARKER),
            typed = loop.expr?.returnType?.parseFromSquare() ?: "",
            priority = GdLookup.LOCAL_USER_DEFINED,
        )

}
