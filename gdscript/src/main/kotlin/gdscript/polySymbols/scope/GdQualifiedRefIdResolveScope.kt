package gdscript.polySymbols.scope

import com.intellij.model.Pointer
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.query.PolySymbolCompoundScope
import com.intellij.polySymbols.query.PolySymbolQueryExecutor
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.query.polySymbolScope
import com.intellij.polySymbols.utils.ReferencingPolySymbol
import com.intellij.psi.createSmartPointer
import com.intellij.psi.util.PsiTreeUtil
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.gdReturnType
import gdscript.polySymbols.resolve.GdSymbolResolverUtil
import gdscript.polySymbols.resolve.GdSymbolResolverUtil.resolveSymbolReferences
import gdscript.psi.GdExpr
import gdscript.psi.GdPsiUtils
import gdscript.psi.GdRefIdRef

internal class GdQualifiedRefIdResolveScope(private val qualifier: GdExpr) : PolySymbolCompoundScope() {

    override fun build(
        queryExecutor: PolySymbolQueryExecutor,
        consumer: (PolySymbolScope) -> Unit
    ) {
        resolveQualifierSymbol()
            ?.queryScope
            ?.forEach(consumer)

        consumer(polySymbolScope {
            provides(GdPolySymbolKind.QUALIFIABLE_SYMBOLS)
            initialize {
                add(
                    ReferencingPolySymbol.create(
                        GdPolySymbolKind.QUALIFIABLE_SYMBOLS,
                        "GDScript Class Member Symbols",
                        GdPolySymbolKind.CLASS,
                        GdPolySymbolKind.CONSTRUCTOR,
                        GdPolySymbolKind.METHOD,
                        GdPolySymbolKind.PROPERTY,
                        GdPolySymbolKind.CONSTANT,
                        GdPolySymbolKind.SIGNAL,
                        GdPolySymbolKind.ENUM,
                        GdPolySymbolKind.ENUM_VALUE,

                        // PSI-only
                        GdPolySymbolKind.LOADED_CLASS_ALIAS,
                        GdPolySymbolKind.AUTOLOAD,
                        GdPolySymbolKind.DICT_KEY,
                    )
                )
            }
        })
    }

    /**
     * Resolve the qualifier to a single [PolySymbol] whose [PolySymbol.queryScope]
     * provides the members visible after the dot.
     */
    private fun resolveQualifierSymbol(): PolySymbol? {
        val refElement = PsiTreeUtil.collectElementsOfType(qualifier, GdRefIdRef::class.java).lastOrNull()
        val references = refElement?.resolveSymbolReferences()
        if (references?.isNotEmpty() == true) {
            // If a symbol has a query scope, return it
            val referenceWithQueryScope = references
                .firstOrNull { it.queryScope.isNotEmpty() }
            if (referenceWithQueryScope != null) return referenceWithQueryScope

            // Otherwise, get the return type from the first reference and try to resolve it to a class symbol
            val classFromReturnType = references.firstOrNull()
                ?.gdReturnType
                ?.let { returnType ->
                    GdSymbolResolverUtil.resolveCanonicalClassSymbol(
                        qualifier.project,
                        returnType,
                        null
                    )
                }
            if (classFromReturnType != null) return classFromReturnType
        }

        val qualifierType = getQualifierType()
        if (qualifierType.isEmpty()) return null
        return GdSymbolResolverUtil.resolveCanonicalClassSymbol(
            qualifier.project,
            qualifierType,
            qualifier,
        )
    }

    private fun getQualifierType(): String {
        val qualifierType = GdPsiUtils.getReturnType(qualifier)

        // in the case of array or dictionary, we need to remove the [type] part
        return qualifierType.substringBefore("[")
    }


    override fun equals(other: Any?): Boolean =
        other === this || other is GdQualifiedRefIdResolveScope && qualifier == other.qualifier

    override fun hashCode(): Int =
        qualifier.hashCode()

    override fun createPointer(): Pointer<out PolySymbolCompoundScope> {
        val qualifierPtr = qualifier.createSmartPointer()
        return Pointer { GdQualifiedRefIdResolveScope(qualifierPtr.element ?: return@Pointer null) }
    }
}