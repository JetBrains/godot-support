package gdscript.polySymbols.completion

import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolQualifiedName
import com.intellij.polySymbols.completion.PolySymbolCodeCompletionItem
import com.intellij.polySymbols.query.PolySymbolCodeCompletionQueryParams
import com.intellij.polySymbols.query.PolySymbolListSymbolsQueryParams
import com.intellij.polySymbols.query.PolySymbolQueryStack
import com.intellij.polySymbols.query.PolySymbolScope
import gdscript.polySymbols.GdClassSymbol
import gdscript.polySymbols.GdPolySymbol
import gdscript.polySymbols.GdPolySymbolsConstants
import gdscript.polySymbols.sdk.xml.GdSdkData

object GdPolySymbolPriorities {
    val BUILT_IN: PolySymbol.Priority = PolySymbol.Priority.custom(10.0)
    val USER_DEFINED: PolySymbol.Priority = PolySymbol.Priority.custom(20.0)
    val LOCAL_USER_DEFINED: PolySymbol.Priority = PolySymbol.Priority.custom(25.0)
}

/** Renders an SDK parameter list as a `(name: Type, …)` hint, matching the PSI completion tail format. */
fun List<GdSdkData.ParameterData>.toCompletionParamHint(variadic: Boolean = false): String {
    if (variadic) return "(...)"
    if (isEmpty()) return "()"
    return joinToString(", ", "(", ")") {
        val type = it.type.enumName?.takeIf { name -> name.isNotEmpty() } ?: it.type.name
        "${it.name}: $type"
    }
}

fun GdSdkData.TypeData.toCompletionTypeText(): String =
    enumName?.takeIf { it.isNotEmpty() } ?: name

fun GdPolySymbol.toCodeCompletionItem(name: String = this.name): PolySymbolCodeCompletionItem =
    PolySymbolCodeCompletionItem.create(name, symbol = this) {
        icon(this@toCodeCompletionItem.icon)
        priority(this@toCodeCompletionItem.priority)
        tailText(this@toCodeCompletionItem.completionTailText)
        typeText(this@toCodeCompletionItem.completionTypeText)
    }


fun PolySymbolScope.gdCodeCompletions(
    qualifiedName: PolySymbolQualifiedName,
    params: PolySymbolCodeCompletionQueryParams,
    stack: PolySymbolQueryStack,
): List<PolySymbolCodeCompletionItem> =
    getSymbols(
        qualifiedName.kind,
        PolySymbolListSymbolsQueryParams.create(params.queryExecutor, expandPatterns = false),
        stack,
    )
        .filterIsInstance<GdPolySymbol>()
        .map { it.toCodeCompletionItem() }

fun PolySymbolCodeCompletionItem.shouldShow(): Boolean{
    return !(this.symbol is GdClassSymbol && GdPolySymbolsConstants.GLOBAL_CLASSES.contains(this.name))
}