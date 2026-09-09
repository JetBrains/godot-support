package gdscript.polySymbols.scope

import com.intellij.openapi.project.Project
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.query.polySymbolScopeCached
import com.intellij.psi.util.PsiModificationTracker
import gdscript.index.impl.GdClassNamingIndex
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.psi.GdPsiClassSymbolFactory

/**
 * Project-level scope providing PSI-backed class symbols for user-defined GDScript classes, backed
 * by [GdClassNamingIndex] (`class_name` declarations, visible project-wide regardless of nesting).
 *
 * Caches the full symbol set via [polySymbolScopeCached] so repeat name-match/list/completion
 * queries on the scope reuse it instead of re-walking the index every time. `partialMatchingSupport`
 * additionally lets a single name-match query - the common case, resolving one reference - go
 * straight to the underlying stub-index point lookup ([GdClassNamingIndex.getGlobally]) instead of
 * forcing (or waiting on) a full project-wide walk first. The decision to offer that fast path is
 * unconditional here (always active - a project-wide index point lookup is always cheap relative to
 * a full project-wide walk, regardless of project size), so the no-argument `partialMatchingSupport { }`
 * form is used - it costs nothing beyond a one-time lazy computation, unlike the `CachedValue`-backed
 * overload CSS's `CssStylesheetClassesScope` needs for its own, genuinely conditional, size-gated
 * decision.
 */
fun gdPsiClassesPolySymbolScope(project: Project): PolySymbolScope =
    polySymbolScopeCached(project) {
        provides(GdPolySymbolKind.CLASS)
        partialMatchingSupport {
            provideMatchingSymbols(PsiModificationTracker.MODIFICATION_COUNT) { _, nameVariant ->
                GdClassNamingIndex.INSTANCE.getGlobally(nameVariant, project)
                    .mapNotNull { GdPsiClassSymbolFactory.create(it) }
            }
        }
        initialize {
            cacheDependencies(PsiModificationTracker.MODIFICATION_COUNT)
            GdClassNamingIndex.INSTANCE.getAllValues(project).forEach { GdPsiClassSymbolFactory.create(it)?.let(::add) }
        }
    }
