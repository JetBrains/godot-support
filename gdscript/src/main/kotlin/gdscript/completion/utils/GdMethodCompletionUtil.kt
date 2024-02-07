package gdscript.completion.utils

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import gdscript.GdIcon
import gdscript.completion.GdLookup
import gdscript.psi.GdFuncDeclEx
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdParamList

object GdMethodCompletionUtil {

    fun GdMethodDeclTl.methodHeader(wrapParams: Boolean = false): String {
        val params = buildParamHint(this, wrapParams)
        return "func ${this.name}$params${if (this.returnType.isNotEmpty()) " -> ${this.returnType}" else ""}"
    }

    fun GdMethodDeclTl.shortMethodHeader(): String {
        return "${this.name}${buildParamHint(this)}"
    }

    fun GdFuncDeclEx.methodHeader(wrapParams: Boolean = false): String {
        val params = buildParamHint(this, wrapParams)
        return "func ${this.funcDeclIdNmi?.text ?: ""}$params${if (this.returnType.isNotEmpty()) " -> ${this.returnType}" else ""}"
    }

    fun GdFuncDeclEx.shortMethodHeader(): String {
        return "${this.funcDeclIdNmi?.text ?: ""}${buildParamHint(this)}"
    }

    fun addMethods(methods: Map<String, GdMethodDeclTl>, result: CompletionResultSet, withFunc: Boolean = false) {
        methods.forEach {
            val item = it.value;
            val params = buildParamHint(item);
            result.addElement(GdLookup.create(
                "${if (withFunc) "func " else ""}${item.name}$params${if (item.returnType.isNotEmpty()) " -> ${item.returnType}" else ""}:",
                tail = params,
                presentable = item.name,
                typed = item.returnType,
                icon = GdIcon.getEditorIcon(GdIcon.METHOD_MARKER),
                priority = GdLookup.USER_DEFINED,
            ))
        }
    }

    fun GdMethodDeclTl.lookup(): LookupElement {
        val params = buildParamHint(this);
        return GdLookup.create(
            this.name,
            tail = params,
            typed = this.returnType,
            icon = GdIcon.getEditorIcon(GdIcon.METHOD_MARKER),
            priority = GdLookup.USER_DEFINED,
        )
    }

    fun GdMethodDeclTl.lookupDeclaration(omitFuncKeyword: Boolean = false, indent: String? = null): LookupElement {
        val params = buildParamHint(this);
        return GdLookup.create(
            "${if (omitFuncKeyword) "" else "func "}${this.name}$params${if (this.returnType.isNotEmpty()) " -> ${this.returnType}" else ""}:${if (indent !== null) "\n$indent" else ""}",
            tail = params,
            presentable = this.name,
            typed = this.returnType,
            icon = GdIcon.getEditorIcon(GdIcon.METHOD_MARKER),
            priority = GdLookup.USER_DEFINED,
        )
    }

    fun buildParamHint(method: GdMethodDeclTl, wrap: Boolean = false): String {
        if (method.isVariadic) return "(vararg)"
        return buildParamHint(method.paramList, wrap)
    }

    fun buildParamHint(method: GdFuncDeclEx, wrap: Boolean = false): String {
        return buildParamHint(method.paramList, wrap)
    }

    private fun buildParamHint(paramList: GdParamList?, wrap: Boolean = false): String {
        if (paramList?.paramList?.isEmpty() ?: true) return "()"
        val wrapper = if (wrap) "\n" else " "
        val spacer = if (wrap) "    " else ""

        val sb = StringBuilder("($wrapper")
        paramList!!.paramList.forEach {
            sb.append("$spacer${it.text},$wrapper")
        }

        sb.append(")")
        return sb.toString().replace(",)", ")")
    }

}
