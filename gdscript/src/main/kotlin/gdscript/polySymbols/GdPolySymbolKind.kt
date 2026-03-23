package gdscript.polySymbols

import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.PolySymbolNamespace

object GdPolySymbolNamespace {
    const val NAMESPACE: PolySymbolNamespace = "GDScript"
}

object GdPolySymbolKind {
    /** A group of symbols that can be extended: classes */
    val INHERITANCE_SYMBOLS: PolySymbolKind = PolySymbolKind[GdPolySymbolNamespace.NAMESPACE, "inheritance-symbol"]
    /** A group of symbols that can be used as type hints: classes, type_alias(loads), enums */
    val TYPE_HINTS: PolySymbolKind = PolySymbolKind[GdPolySymbolNamespace.NAMESPACE, "type-hint"]
    /** A group of symbols representing elements that can be qualifiers or qualified: class members */
    val QUALIFIABLE_SYMBOLS: PolySymbolKind = PolySymbolKind[GdPolySymbolNamespace.NAMESPACE, "qualifiable-symbol"]

    // Symbols present in SDK and PSI
    val CLASS: PolySymbolKind = PolySymbolKind[GdPolySymbolNamespace.NAMESPACE, "class"]
    val CONSTRUCTOR: PolySymbolKind = PolySymbolKind[GdPolySymbolNamespace.NAMESPACE, "constructor"]
    val METHOD: PolySymbolKind = PolySymbolKind[GdPolySymbolNamespace.NAMESPACE, "method"]
    val PROPERTY: PolySymbolKind = PolySymbolKind[GdPolySymbolNamespace.NAMESPACE, "property"]
    val CONSTANT: PolySymbolKind = PolySymbolKind[GdPolySymbolNamespace.NAMESPACE, "constant"]
    val SIGNAL: PolySymbolKind = PolySymbolKind[GdPolySymbolNamespace.NAMESPACE, "signal"]
    val ENUM: PolySymbolKind = PolySymbolKind[GdPolySymbolNamespace.NAMESPACE, "enum"]
    val ENUM_VALUE: PolySymbolKind = PolySymbolKind[GdPolySymbolNamespace.NAMESPACE, "enum-value"]

    // SDK exclusive symbols
    val ANNOTATION: PolySymbolKind = PolySymbolKind[GdPolySymbolNamespace.NAMESPACE, "annotation"]
    val OPERATOR: PolySymbolKind = PolySymbolKind[GdPolySymbolNamespace.NAMESPACE, "operator"]

    // Symbols used for reference resolve, PSI only
    val RESOURCE_CLASS: PolySymbolKind = PolySymbolKind[GdPolySymbolNamespace.NAMESPACE, "resource-class"]
    val LOADED_CLASS_ALIAS: PolySymbolKind = PolySymbolKind[GdPolySymbolNamespace.NAMESPACE, "type-alias"] // load, preload
    val AUTOLOAD: PolySymbolKind = PolySymbolKind[GdPolySymbolNamespace.NAMESPACE, "autoload"]

    // Local symbols, not class members, PSI only
    // TODO maybe squash all of these into local variable, not sure of the benefit of having them separated like in the PSI
    val BINDING_PATTERN: PolySymbolKind = PolySymbolKind[GdPolySymbolNamespace.NAMESPACE, "binding-pattern"]
    val FOR_VARIABLE: PolySymbolKind = PolySymbolKind[GdPolySymbolNamespace.NAMESPACE, "for-variable"]
    val LOCAL_VARIABLE: PolySymbolKind = PolySymbolKind[GdPolySymbolNamespace.NAMESPACE, "local-variable"]
    val PARAMETER: PolySymbolKind = PolySymbolKind[GdPolySymbolNamespace.NAMESPACE, "parameter"]
}
