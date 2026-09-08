package gdscript.polySymbols.completion

import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.completion.PolySymbolCodeCompletionItem
import gdscript.polySymbols.GdPolySymbolKind
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

fun PolySymbolCodeCompletionItem.shouldShow(): Boolean{
    return !(this.symbol?.kind == GdPolySymbolKind.CLASS && GdPolySymbolsConstants.GLOBAL_CLASSES.contains(this.name))
}