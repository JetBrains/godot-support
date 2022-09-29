package gdscript.completion.utils

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import gdscript.GdIcon
import gdscript.completion.GdLookup
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdParam

object GdMethodCompletionUtil {

    fun addMethods(methods: Map<String, GdMethodDeclTl>, result: CompletionResultSet, withFunc: Boolean = false) {
        methods.forEach {
            val item = it.value;
            val params = "(${item.paramList?.text ?: ""})";
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

    fun lookup(method: GdMethodDeclTl): LookupElement {
        return GdLookup.create(
            method.name.orEmpty(),
            lookup = "()${if (method.paramList?.paramList?.isNotEmpty() == true) "_" else ""}",
            presentable = method.name.orEmpty(),
            typed = method.returnType,
            icon = GdIcon.getEditorIcon(GdIcon.METHOD_MARKER),
            priority = GdLookup.USER_DEFINED,
        );
    }

    fun lookup(param: GdParam): LookupElement {
        return GdLookup.create(
            param.varNmi.name,
            typed = param.returnType,
            icon = GdIcon.getEditorIcon(GdIcon.VAR_MARKER),
            priority = GdLookup.LOCAL_USER_DEFINED,
        );
    }

}