package gdscript.polySymbols.scope

import com.intellij.model.Pointer
import com.intellij.polySymbols.query.PolySymbolCompoundScope
import com.intellij.polySymbols.query.PolySymbolQueryExecutor
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.query.polySymbolScope
import com.intellij.psi.PsiElement
import com.intellij.psi.createSmartPointer
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.resolve.GdSymbolResolverUtil.resolveSymbolReferences

/**
 * Resolves a nested-class inheritance chain segment (`extends Outer.Inner`, where [qualifier] is
 * `Outer`'s own `GdInheritanceIdRef`/`GdInheritanceSubIdRef`) by resolving the qualifier to a
 * single [com.intellij.polySymbols.PolySymbol] and re-exposing its [com.intellij.polySymbols.PolySymbol.queryScope]
 * - mirroring [GdQualifiedTypeHintResolveScope]/[GdQualifiedRefIdResolveScope]. No return-type
 * fallback is needed here, unlike those two: an inheritance qualifier is always itself an
 * inheritance-id element, which always resolves directly via its own own-references.
 */
internal class GdQualifiedInheritanceResolveScope(private val qualifier: PsiElement) : PolySymbolCompoundScope() {

    override fun build(
        queryExecutor: PolySymbolQueryExecutor,
        consumer: (PolySymbolScope) -> Unit
    ) {
        qualifier.resolveSymbolReferences()
            .firstOrNull { it.queryScope.isNotEmpty() }
            ?.queryScope
            ?.forEach(consumer)

        consumer(polySymbolScope {
            provides(GdPolySymbolKind.INHERITANCE_SYMBOLS)
            initialize {
                addSymbol(GdPolySymbolKind.INHERITANCE_SYMBOLS, "GDScript Inheritance Symbols") {
                    pattern {
                        group {
                            symbols {
                                from(GdPolySymbolKind.CLASS)
                            }
                            symbolReference()
                        }
                    }
                }
            }
        })
    }

    override fun equals(other: Any?): Boolean =
        other === this || other is GdQualifiedInheritanceResolveScope && qualifier == other.qualifier

    override fun hashCode(): Int =
        qualifier.hashCode()

    override fun createPointer(): Pointer<out PolySymbolCompoundScope> {
        val qualifierPtr = qualifier.createSmartPointer()
        return Pointer { qualifierPtr.element?.let { GdQualifiedInheritanceResolveScope(it) } }
    }
}
