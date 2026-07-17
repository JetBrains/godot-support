package gdscript.psi.impl

import com.intellij.lang.ASTNode
import com.intellij.model.psi.PsiSymbolReference
import com.intellij.openapi.util.TextRange
import com.intellij.polySymbols.query.PolySymbolQueryExecutorFactory
import com.intellij.polySymbols.references.polySymbolOwnReferences
import com.intellij.polySymbols.utils.unwrapMatchedSymbols
import com.intellij.psi.PsiElementVisitor
import gdscript.GdKeywords
import gdscript.polySymbols.GdClassSymbol
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.GdPolySymbolKind.QUALIFIABLE_SYMBOLS
import gdscript.polySymbols.GdPolySymbolModifier.STATIC
import gdscript.polySymbols.psi.GdAliasedNameSymbol
import gdscript.polySymbols.psi.GdPsiPolySymbolUtil.isStatic
import gdscript.polySymbols.psi.GdPsiPolySymbolUtil.resolveConstructorSymbols
import gdscript.polySymbols.psi.GdPsiPolySymbolUtil.resolveEarlierEnumValueSymbol
import gdscript.polySymbols.resolve.GdSymbolResolverUtil
import gdscript.polySymbols.scope.hasModifier
import gdscript.polySymbols.scope.hasStaticInstanceDistinction
import gdscript.psi.GdRefIdRef
import gdscript.psi.GdVisitor
import gdscript.utils.PsiElementUtil.getCallExpr
import org.jetbrains.annotations.Unmodifiable

class GdRefIdRefImpl(node: ASTNode) : GdRefElementImpl(node), GdRefIdRef {
    fun accept(visitor: GdVisitor) {
        visitor.visitRefIdNm(this)
    }

    override fun accept(visitor: PsiElementVisitor) {
        if (visitor is GdVisitor) accept(visitor)
        else super.accept(visitor)
    }

    override fun getOwnReferences(): @Unmodifiable Collection<PsiSymbolReference> {
        val text = text
        if (text == GdKeywords.SELF || text == GdKeywords.SUPER || GdKeywords.MATH_CONSTANTS.contains(text)) {
            return emptyList()
        }
        return polySymbolOwnReferences(this) {
            reference(TextRange(0, text.length), QUALIFIABLE_SYMBOLS) {
                if (text == "new") {
                    resolveConstructorSymbols(this@GdRefIdRefImpl)
                } else {
                    resolveEarlierEnumValueSymbol(this@GdRefIdRefImpl)?.let {
                        return@reference listOf(it)
                    }
                    val requireStatic = isStatic(this@GdRefIdRefImpl)
                    val resolved = PolySymbolQueryExecutorFactory.create(this@GdRefIdRefImpl, true)
                        .nameMatchQuery(QUALIFIABLE_SYMBOLS, text)
                        .run()
                        .filter { symbol -> !requireStatic || !symbol.hasStaticInstanceDistinction() || symbol.hasModifier(STATIC) }

                    // Bare constructor call (`ClassName(...)`, no `.new()`): the resolved reference
                    // is CLASS-only today (constructors are never name-matched by their class's own
                    // name - only SDK constructors happen to share it, PSI ones are always "_init").
                    // `resolved`'s items are still PolySymbolMatchBase-style composite matches here
                    // (the platform only unwraps after this lambda returns, in
                    // PolySymbolOwnReferencesBuilderImpl's `resolvedSymbols`) - must unwrap before
                    // trusting `.kind`, exactly like hasStaticInstanceDistinction()/hasModifier() do.
                    val classSymbol = resolved.flatMap { it.unwrapMatchedSymbols() }
                        .firstOrNull { it.kind == GdPolySymbolKind.CLASS } as? GdClassSymbol
                    if (classSymbol != null && this@GdRefIdRefImpl.getCallExpr() != null) {
                        resolved + GdSymbolResolverUtil.listConstructorSymbols(classSymbol).map { GdAliasedNameSymbol(it, text) }
                    } else {
                        resolved
                    }
                }
            }
        }
    }
}