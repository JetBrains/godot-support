package gdscript.polySymbols.scope

import com.intellij.model.Pointer
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.query.PolySymbolCompoundScope
import com.intellij.polySymbols.query.PolySymbolQueryExecutor
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.query.polySymbolScope
import com.intellij.psi.PsiElement
import com.intellij.psi.createSmartPointer
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.resolve.GdSymbolResolverUtil
import gdscript.polySymbols.resolve.GdSymbolResolverUtil.resolveSymbolReferences
import gdscript.psi.GdTypeHintRef
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.psi.utils.GdCommonUtil

internal class GdQualifiedTypeHintResolveScope(private val qualifier: GdTypeHintRef) : PolySymbolCompoundScope() {
    override fun build(
        queryExecutor: PolySymbolQueryExecutor,
        consumer: (PolySymbolScope) -> Unit
    ) {
        resolveQualifierSymbol()
            ?.queryScope
            ?.forEach(consumer)

        consumer(polySymbolScope {
            provides(GdPolySymbolKind.TYPE_HINTS)
            initialize {
                addSymbol(GdPolySymbolKind.TYPE_HINTS, "GDScript Type Hints") {
                    pattern {
                        group {
                            symbols {
                                from(GdPolySymbolKind.CLASS)
                                from(GdPolySymbolKind.LOADED_CLASS_ALIAS)
                                from(GdPolySymbolKind.ENUM)
                            }
                            symbolReference()
                        }
                    }
                }
            }
        })
    }

    /**
     * Resolve the qualifier [GdTypeHintRef] to a single [PolySymbol] whose
     * [PolySymbol.queryScope] provides the members visible after the dot.
     */
    private fun resolveQualifierSymbol(): PolySymbol? {
        val referenced = qualifier
            .resolveSymbolReferences()
            .firstOrNull { it.queryScope.isNotEmpty() }
        if (referenced != null) return referenced

        val qualifierType = getQualifierType()
        if (qualifierType.isEmpty()) return null
        return GdSymbolResolverUtil.resolveCanonicalClassSymbol(
            qualifier.project,
            qualifierType,
            qualifier,
        )
    }

    private fun getQualifierType(): String {
        val declaration = GdClassMemberUtil.findDeclaration(qualifier)
        val resolved = when (declaration) {
            is PsiElement -> GdCommonUtil.returnType(declaration)
            else -> ""
        }
        if (resolved.isNotEmpty()) {
            return resolved
        }

        return qualifier.text
    }


    override fun equals(other: Any?): Boolean =
        other === this || other is GdQualifiedTypeHintResolveScope && qualifier == other.qualifier

    override fun hashCode(): Int =
        qualifier.hashCode()

    override fun createPointer(): Pointer<out PolySymbolCompoundScope> {
        val qualifierPtr = qualifier.createSmartPointer()
        return Pointer { GdQualifiedTypeHintResolveScope(qualifierPtr.element ?: return@Pointer null) }
    }
}