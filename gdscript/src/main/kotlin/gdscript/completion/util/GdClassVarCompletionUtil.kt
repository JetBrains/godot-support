package gdscript.completion.util

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import gdscript.GdIcon
import gdscript.GdKeywords
import gdscript.completion.GdLookup
import gdscript.psi.GdClassVarDeclTl
import gdscript.psi.GdVarDeclSt
import gdscript.psi.GdVarNmi

object GdClassVarCompletionUtil {

    fun annotations(result: CompletionResultSet, withPrefix: Boolean = true) {
        GdKeywords.ANNOTATIONS.forEach {
            result
                .addElement(
                    GdLookup.create(
                        if (withPrefix) "@$it" else it,
                        //lookup = if (GdKeywords.ANNOTATIONS_PARAMETRIZED.contains(it)) "" else " ",
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

    fun lookup(variable: GdVarDeclSt): LookupElement =
        GdLookup.create(variable.name,
            icon = GdIcon.getEditorIcon(GdIcon.VAR_MARKER),
            typed = variable.returnType,
            priority = GdLookup.LOCAL_USER_DEFINED,
        )

    fun lookup(variable: GdVarNmi): LookupElement =
        GdLookup.create(variable.name,
            icon = GdIcon.getEditorIcon(GdIcon.VAR_MARKER),
//            typed = variable.returnType, // TODO
            priority = GdLookup.LOCAL_USER_DEFINED,
        )

}
