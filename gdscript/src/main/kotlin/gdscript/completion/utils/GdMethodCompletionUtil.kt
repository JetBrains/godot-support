package gdscript.completion.utils

import com.intellij.codeInsight.completion.CompletionResultSet
import gdscript.GdIcon
import gdscript.completion.GdLookup
import gdscript.psi.GdMethodDeclTl

object GdMethodCompletionUtil {

    fun addMethods(methods: Map<String, GdMethodDeclTl>, result: CompletionResultSet, withFunc: Boolean = false) {
        methods.forEach {
            val item = it.value;
            val params = buildParamHint(item);
            result.addElement(GdLookup.create(
                "${if (withFunc) "func " else ""}${item.name.orEmpty()}${params}${if (item.returnType.isNotEmpty()) " -> ${item.returnType}" else ""}:",
                tail = params,
                presentable = item.name.orEmpty(),
                typed = item.returnType,
                icon = GdIcon.getEditorIcon(GdIcon.METHOD_MARKER),
                priority = GdLookup.USER_DEFINED,
            ))
        }
    }

    fun buildParamHint(method: GdMethodDeclTl): String {
        if (method.isVariadic) {
            return "(vararg)";
        }

        return "(${method.paramList?.text ?: ""})";
    }

}