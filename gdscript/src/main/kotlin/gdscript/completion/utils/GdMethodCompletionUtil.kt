package gdscript.completion.utils

import GdScriptPluginIcons
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.polySymbols.PolySymbol
import gdscript.completion.GdLookup
import gdscript.polySymbols.gdCompletionTailText
import gdscript.polySymbols.gdCompletionTypeText
import gdscript.psi.GdFuncDeclEx
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdParam

object GdMethodCompletionUtil {

    /**
     * Builds an "override parent method" completion stub for [symbol] (a METHOD symbol, PSI- or
     * SDK-backed). Uses only plain [PolySymbol] members and the `gdCompletionTailText`/
     * `gdCompletionTypeText` extensions, so PSI and SDK symbols render uniformly - for PSI symbols
     * these are computed via the same [buildParamHint] call [GdMethodDeclTl.lookupDeclaration] uses,
     * so the rendered text matches exactly.
     */
    fun overrideLookupElement(symbol: PolySymbol, omitFuncKeyword: Boolean = false, indent: String? = null): LookupElement {
        val params = symbol.gdCompletionTailText ?: "()"
        val returnType = symbol.gdCompletionTypeText.orEmpty()
        return GdLookup.create(
            "${if (omitFuncKeyword) "" else "func "}${symbol.name}$params${if (returnType.isNotEmpty()) " -> $returnType" else ""}:${if (indent !== null) "\n$indent" else ""}",
            tail = params,
            presentable = symbol.name,
            typed = returnType,
            icon = symbol.icon,
            priority = GdLookup.USER_DEFINED,
        )
    }

    fun GdMethodDeclTl.methodHeader(wrapParams: Boolean = false): String {
        val params = buildParamHint(this, wrapParams)
        return "func ${this.getName()}$params${if (this.returnType.isNotEmpty()) " -> ${this.returnType}" else ""}"
    }

    fun GdMethodDeclTl.shortMethodHeader(): String {
        return "${this.getName()}${buildParamHint(this)}"
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
            val item = it.value
            val params = buildParamHint(item)
            result.addElement(GdLookup.create(
                "${if (withFunc) "func " else ""}${item.getName()}$params${if (item.returnType.isNotEmpty()) " -> ${item.returnType}" else ""}:",
                tail = params,
                presentable = item.getName(),
                typed = item.returnType,
                icon = GdScriptPluginIcons.GDScriptIcons.METHOD_MARKER,
                priority = GdLookup.USER_DEFINED,
            ))
        }
    }

    fun GdMethodDeclTl.lookup(): LookupElement {
        val params = buildParamHint(this)
        return GdLookup.create(
            this.getName(),
            tail = params,
            typed = this.returnType,
            icon = GdScriptPluginIcons.GDScriptIcons.METHOD_MARKER,
            priority = GdLookup.USER_DEFINED,
        )
    }

    fun GdMethodDeclTl.lookupDeclaration(omitFuncKeyword: Boolean = false, indent: String? = null): LookupElement {
        val params = buildParamHint(this)
        return GdLookup.create(
            "${if (omitFuncKeyword) "" else "func "}${this.getName()}$params${if (this.returnType.isNotEmpty()) " -> ${this.returnType}" else ""}:${if (indent !== null) "\n$indent" else ""}",
            tail = params,
            presentable = this.getName(),
            typed = this.returnType,
            icon = GdScriptPluginIcons.GDScriptIcons.METHOD_MARKER,
            priority = GdLookup.USER_DEFINED,
        )
    }

    fun buildParamHint(method: GdMethodDeclTl, wrap: Boolean = false): String {
        if (method.isVariadic) return "(...)"
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

        return sb.append(")").toString()
    }

}
