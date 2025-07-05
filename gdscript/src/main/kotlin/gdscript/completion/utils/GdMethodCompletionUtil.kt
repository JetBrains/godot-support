package gdscript.completion.utils

import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import gdscript.GdIcon
import gdscript.completion.GdLookup
import gdscript.psi.GdFuncDeclEx
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdParam

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
                icon = GdScriptPluginIcons.GDScriptIcons.METHOD_MARKER,
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
            icon = GdScriptPluginIcons.GDScriptIcons.METHOD_MARKER,
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
            icon = GdScriptPluginIcons.GDScriptIcons.METHOD_MARKER,
            priority = GdLookup.USER_DEFINED,
        )
    }

    fun buildParamHint(method: GdMethodDeclTl, wrap: Boolean = false): String {
        if (method.isVariadic) return "(vararg)"
        return buildParamHint(method.paramList?.paramList ?: emptyList(), wrap)
    }

    fun buildParamHint(method: GdFuncDeclEx, wrap: Boolean = false): String {
        return buildParamHint(method.paramList?.paramList ?: emptyList(), wrap)
    }

    private fun buildParamHint(paramList: List<GdParam>, wrap: Boolean = false): String {
        if (paramList.isEmpty()) return "()"
        val wrapper = if (wrap) "\n" else " "
        val spacer = if (wrap) "    " else ""

        val sb = StringBuilder("(")
        if (wrap) sb.append("\n")
        paramList.forEachIndexed { idx, param ->
            sb.append("$spacer${param.text}")
            // not the last param
            if (idx < paramList.size - 1) {
                sb.append(",$wrapper")
            }
        }
        if (wrap) sb.append("\n")

        return sb.append(")").toString();
    }

}
