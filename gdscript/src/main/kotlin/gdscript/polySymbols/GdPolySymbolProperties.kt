package gdscript.polySymbols

import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolProperty
import com.intellij.psi.PsiElement

// Existing GdPolySymbol members, exposed to external consumers without a GdPolySymbol cast. The
// GdPolySymbol declarations carry the matching @PolySymbol.Property annotation; PolySymbolPropertyGetter
// walks superclasses when building its per-concrete-class accessor map and invokes the resolved
// java.lang.reflect.Method virtually, so one annotation on the base class's declaration is found and
// correctly dispatches to every subclass's own override.
object GdReturnTypeProperty : PolySymbolProperty<String>("gd-return-type", String::class.java)
object GdCompletionTailTextProperty : PolySymbolProperty<String>("gd-completion-tail-text", String::class.java)
object GdCompletionTypeTextProperty : PolySymbolProperty<String>("gd-completion-type-text", String::class.java)
object GdDeclaringClassIdProperty : PolySymbolProperty<String>("gd-declaring-class-id", String::class.java)

val PolySymbol.gdReturnType: String? get() = this[GdReturnTypeProperty]
val PolySymbol.gdCompletionTailText: String? get() = this[GdCompletionTailTextProperty]
val PolySymbol.gdCompletionTypeText: String? get() = this[GdCompletionTypeTextProperty]
val PolySymbol.gdDeclaringClassId: String? get() = this[GdDeclaringClassIdProperty]

/**
 * Real-or-synthetic PsiElement, for consumers OK navigating into a generated SDK doc file
 * (line-marker/goto-style navigation). Null only if the symbol has no backing element at all.
 */
object GdNavigationElementProperty : PolySymbolProperty<PsiElement>("gd-navigation-element", PsiElement::class.java)
val PolySymbol.gdNavigationElement: PsiElement? get() = this[GdNavigationElementProperty]

/**
 * REAL PSI only, null for SDK symbols - exact behavioral mirror of the `as? GdPsiPolySymbol)
 * ?.sourceElement` casts this replaces.
 */
object GdPsiSourceElementProperty : PolySymbolProperty<PsiElement>("gd-psi-source-element", PsiElement::class.java)
val PolySymbol.gdPsiSourceElement: PsiElement? get() = this[GdPsiSourceElementProperty]

/** Is this an SDK/engine symbol, as opposed to project-source? Orthogonal to [PolySymbol.kind]. */
object GdIsEngineSymbolProperty : PolySymbolProperty<Boolean>("gd-is-engine-symbol", Boolean::class.java)
val PolySymbol.gdIsEngineSymbol: Boolean get() = this[GdIsEngineSymbolProperty] == true

/** Does this CLASS symbol have an explicit constructor? SDK-only - see [gdscript.polySymbols.sdk.GdSdkClassSymbol]. */
object GdHasConstructorProperty : PolySymbolProperty<Boolean>("gd-has-constructor", Boolean::class.java)
val PolySymbol.gdHasConstructor: Boolean get() = this[GdHasConstructorProperty] == true

/** Structured parameter info for a METHOD/CONSTRUCTOR symbol - see [GdSignatureProperty]. */
data class GdParameterInfo(val name: String, val type: String, val hasDefault: Boolean = false)

/** Structured signature for a METHOD/CONSTRUCTOR symbol, uniform across PSI and SDK backing. */
data class GdSignature(val parameters: List<GdParameterInfo>, val isVariadic: Boolean)
object GdSignatureProperty : PolySymbolProperty<GdSignature>("gd-signature", GdSignature::class.java)
val PolySymbol.gdSignature: GdSignature? get() = this[GdSignatureProperty]
