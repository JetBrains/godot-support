package gdscript.polySymbols.scope

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.PolySymbolNameSegment
import com.intellij.polySymbols.query.PolySymbolMatch
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.query.polySymbolScopeCached
import com.intellij.polySymbols.utils.withName
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.stubChildOfType
import gdscript.index.impl.GdFileResIndex
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.psi.GdPsiClassSymbolFactory
import gdscript.psi.GdClassNaming
import gdscript.psi.GdFile
import gdscript.utils.VirtualFileUtil.getPsiFile

/**
 * Project-level scope providing PSI-backed class symbols for user-defined anonymous GDScript classes.
 *
 * Backed by [GdFileResIndex] - not a real stub index (see its own doc comment), but a direct
 * VFS/content-root file lookup keyed by `res://...` resource path. The real, underlying class symbol
 * ([GdPsiClassSymbolFactory]) always reports kind `CLASS` - a resource path can resolve to a *named*
 * class too, e.g. `extends "res://base.gd"` where `base.gd` declares `class_name Base` - but this
 * scope answers queries for [GdPolySymbolKind.RESOURCE_CLASS]. A scope must never surface a symbol
 * whose own kind disagrees with the query kind it answers, so every symbol this scope produces is
 * wrapped via [referencingResourceClassSymbol] first - see its own doc comment for why that's a
 * [PolySymbolMatch], not a kind-overriding [com.intellij.polySymbols.utils.PolySymbolDelegate].
 *
 * Caches the full symbol set via [polySymbolScopeCached] so repeat name-match/list/completion
 * queries reuse it instead of re-walking `GdFileResIndex.getNonEmptyKeys` (a full
 * [com.intellij.openapi.roots.ProjectFileIndex.iterateContent] walk of the whole project) every time.
 * `partialMatchingSupport` keeps single-name resolution (the common case, e.g. `extends "res://foo.gd"`)
 * on the cheap direct-lookup path ([GdFileResIndex.getFiles]) without forcing that full walk first.
 * The decision to offer it is unconditional (always active, same reasoning as
 * [gdPsiClassesPolySymbolScope]), so the no-argument `partialMatchingSupport { }` form is used -
 * no `CachedValue` needed for a decision that never actually changes.
 */
fun gdPsiResourceClassesPolySymbolScope(project: Project): PolySymbolScope =
    polySymbolScopeCached(project) {
        provides(GdPolySymbolKind.RESOURCE_CLASS)
        partialMatchingSupport {
            provideMatchingSymbols(
                PsiModificationTracker.MODIFICATION_COUNT, VirtualFileManager.VFS_STRUCTURE_MODIFICATIONS,
            ) { _, nameVariant ->
                GdFileResIndex.getFiles(nameVariant.trim('"', '\''), project)
                    .asSequence()
                    .mapNotNull { it.getPsiFile(project) as? GdFile }
                    .mapNotNull { GdPsiClassSymbolFactory.create(it) }
                    .map { referencingResourceClassSymbol(it, nameVariant) }
                    .toList()
            }
        }
        initialize {
            cacheDependencies(PsiModificationTracker.MODIFICATION_COUNT, VirtualFileManager.VFS_STRUCTURE_MODIFICATIONS)
            GdFileResIndex.getNonEmptyKeys(project)
                .asSequence()
                .flatMap { GdFileResIndex.getFiles(it, project) }
                .mapNotNull { it.getPsiFile(project) as? GdFile }
                .filter { it.stubChildOfType<GdClassNaming>() == null }
                .mapNotNull { GdPsiClassSymbolFactory.create(it) }
                .map { referencingResourceClassSymbol(it, it.name) }
                .forEach(::add)
        }
    }

/**
 * Wraps [target] (a real, `CLASS`-kind class symbol) into a one-segment [PolySymbolMatch] reporting
 * kind [GdPolySymbolKind.RESOURCE_CLASS] and name [resourcePath] - a "referencing symbol," in the
 * same spirit as the platform's own `com.intellij.polySymbols.utils.ReferencingPolySymbol`, that lets
 * a scope answer a query of one kind with a real symbol of a genuinely different kind, without
 * lying about that real symbol's own kind anywhere else.
 *
 * Can't reuse `ReferencingPolySymbol.create()` itself: it builds a declarative symbol whose pattern
 * *re-queries* other scopes for a symbol of the given kind(s) under the exact same name - correct
 * when the referencing and the referenced name coincide (its own worked example, Angular Forms'
 * `formControlName="x"` mapping to a `FormGroup`'s `"x"` key), but here the queried resource path
 * (`"res://base.gd"`) and the real target's own name (`"Base"`) usually differ, and the target is
 * already known - found by a direct file lookup, not a fresh by-name search - so a hand-built
 * `PolySymbolMatch` around the already-resolved [target] is the right tool instead.
 *
 * [target] is [withName]-aliased to [resourcePath] before being placed in the segment: a
 * [PolySymbolMatch]'s own reported name is computed purely from the segment's range over
 * [resourcePath], so the *outer* wrapper already reports the right name regardless - but
 * [com.intellij.polySymbols.utils.unwrapMatchedSymbols] (used by own references' final name check in
 * `PolySymbolOwnReferencesBuilderImpl`) unwraps *through* the `PolySymbolMatch` layer down to
 * [target] itself, so its own name must equal [resourcePath] too, or that final check would drop the
 * match for every *named* resource class.
 */
private fun referencingResourceClassSymbol(target: PolySymbol, resourcePath: String): PolySymbol =
    PolySymbolMatch.create(
        resourcePath,
        GdPolySymbolKind.RESOURCE_CLASS,
        PolySymbolNameSegment.create(0, resourcePath.length, target.withName(resourcePath)),
    )
