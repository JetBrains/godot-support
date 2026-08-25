package gdscript.polySymbols.scope

import com.intellij.model.Pointer
import com.intellij.openapi.project.Project
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.utils.PolySymbolScopeWithCache
import com.intellij.psi.util.PsiModificationTracker
import gdscript.index.impl.GdClassNamingIndex
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.psi.GdPsiClassSymbolFactory

/**
 * Project-level scope providing PSI-backed class symbols for user-defined GDScript classes, backed
 * by [GdClassNamingIndex] (`class_name` declarations, visible project-wide regardless of nesting).
 *
 * Caches the full symbol set via [PolySymbolScopeWithCache] so repeat name-match/list/completion
 * queries on the scope reuse it instead of re-walking the index every time. [partialMatchingSupport]
 * additionally lets a single name-match query - the common case, resolving one reference - go
 * straight to the underlying stub-index point lookup ([GdClassNamingIndex.getGlobally]) instead of
 * forcing (or waiting on) a full project-wide walk first: unlike a per-file scope where building the
 * full cache can be cheap enough to just always do eagerly (see `CssTagClassesScope`/
 * `CssStylesheetClassesScope`'s size-gated `partialMatchingSupport`, which only kicks in over ~500
 * stubbed symbols in one file), a full walk here is proportional to every `class_name` in the whole
 * project, so the point-lookup fast path is worth providing unconditionally.
 */
class GdPsiClassesPolySymbolScope(project: Project) : PolySymbolScopeWithCache<Project, Unit>(project, project, Unit) {

    override fun provides(kind: PolySymbolKind): Boolean = kind == GdPolySymbolKind.CLASS

    override fun initialize(consumer: (PolySymbol) -> Unit, cacheDependencies: MutableSet<Any>) {
        cacheDependencies.add(PsiModificationTracker.MODIFICATION_COUNT)
        GdClassNamingIndex.INSTANCE.getAllValues(project).forEach { GdPsiClassSymbolFactory.create(it)?.let(consumer) }
    }

    override val partialMatchingSupport: PartialMatchingSupport =
        object : PartialMatchingSupport {
            override val cacheDependencies: Collection<Any> = listOf(PsiModificationTracker.MODIFICATION_COUNT)

            override fun getMatchingSymbols(kind: PolySymbolKind, nameVariant: String): List<PolySymbol> =
                GdClassNamingIndex.INSTANCE.getGlobally(nameVariant, project)
                    .mapNotNull { GdPsiClassSymbolFactory.create(it) }
        }

    override fun createPointer(): Pointer<GdPsiClassesPolySymbolScope> {
        val project = project
        return Pointer { GdPsiClassesPolySymbolScope(project) }
    }
}
