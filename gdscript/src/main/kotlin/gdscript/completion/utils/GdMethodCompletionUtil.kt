package gdscript.completion.utils

import com.intellij.codeInsight.completion.CompletionResultSet
import gdscript.GdIcon
import gdscript.completion.GdLookup
import gdscript.psi.GdMethodDeclTl

object GdMethodCompletionUtil {

    fun addMethods(methods: Map<String, GdMethodDeclTl>, result: CompletionResultSet, withFunc: Boolean = false) {
        methods.forEach {
            val item = it.value;
            result.addElement(GdLookup.create(
                "${if (withFunc) "func " else ""}${item.name.orEmpty()}${item.paramList?.text ?: ""}${if (item.returnType.isNotEmpty()) " -> ${item.returnType}" else ""}:",
                tail = buildParamHint(item),
                presentable = item.name.orEmpty(),
                typed = item.returnType,
                icon = GdIcon.getEditorIcon(GdIcon.METHOD_MARKER),
                priority = GdLookup.USER_DEFINED,
            ))
        }
    }

    fun buildParamHint(method: GdMethodDeclTl): String {
        if (method.isVariadic) {
            return "(args...)";
        }

        return "(${method.paramList?.text ?: ""})";
    }

}