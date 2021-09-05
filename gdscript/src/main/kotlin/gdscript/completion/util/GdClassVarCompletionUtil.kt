package gdscript.completion.util

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import gdscript.GdIcon
import gdscript.GdKeywords
import gdscript.completion.GdLookup
import gdscript.psi.GdClassVarDeclTl

object GdClassVarCompletionUtil {

    fun annotations(result: CompletionResultSet, withPrefix: Boolean = true) {
        GdKeywords.ANNOTATIONS.forEach {
            result
                .addElement(
                    GdLookup.create(
                        if (withPrefix) "@$it" else it,
                        lookup = " ",
                        color = GdLookup.ANNOTATOR_COLOR,
                        priority = GdLookup.USER_DEFINED,
                    )
                );
        }
    }

    fun lookup(variable: GdClassVarDeclTl): LookupElement =
        GdLookup.create(variable.name.orEmpty(),
            icon = GdIcon.getEditorIcon(GdIcon.VAR_MARKER),
            typed = variable.returnType,
            priority = GdLookup.USER_DEFINED,
        )

}
