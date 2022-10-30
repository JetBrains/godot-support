package gdscript.completion.utils

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
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

    fun GdMethodDeclTl.lookup(omitFuncKeyword: Boolean = false): LookupElement {
        val params = buildParamHint(this);
        return GdLookup.create(
            "${if (omitFuncKeyword) "" else "func "}${this.name}${params}${if (this.returnType.isNotEmpty()) " -> ${this.returnType}" else ""}:",
            tail = params,
            presentable = this.name,
            typed = this.returnType,
            icon = GdIcon.getEditorIcon(GdIcon.METHOD_MARKER),
            priority = GdLookup.USER_DEFINED,
        )
    }

    fun buildParamHint(method: GdMethodDeclTl): String {
        if (method.isVariadic) return "(vararg)";
        return "(${method.paramList?.text ?: ""})";
    }

}