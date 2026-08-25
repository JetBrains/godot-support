package gdscript.polySymbols.scope

import com.intellij.polySymbols.PolySymbol
import com.intellij.polySymbols.query.PolySymbolScope
import com.intellij.polySymbols.query.polySymbolScopeCached
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiModificationTracker
import com.intellij.psi.util.PsiTreeUtil
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.psi.GdPsiClassSymbolFactory
import gdscript.psi.GdClassDeclTl
import gdscript.psi.GdFile

/**
 * File-level scope providing PSI-backed class symbols for unnamed nested GDScript classes
 * (`class Inner:` with no `class_name`), resolvable by their simple name anywhere in the declaring
 * file - not just via qualified `Outer.Inner` access (already covered by
 * [gdscript.polySymbols.psi.gdPsiClassMemberScope]).
 *
 * Walks the file's own stub tree once per modification (cached by [polySymbolScopeCached]) rather
 * than hitting a project-wide index: a single already-open file is cheap to walk fully, and caching
 * means every subsequent name-match/list/completion query on the file reuses the same walk - see
 * `CssTagClassesScope`/`CssStylesheetClassesScope` for the platform's worked example of this same
 * "cache a stub walk, only fall back to an index for genuinely large inputs" trade-off (which this
 * scope doesn't need at all, given its bounded, single-file scope).
 */
fun gdPsiInnerClassesPolySymbolScope(file: GdFile): PolySymbolScope =
    polySymbolScopeCached(file) {
        provides(GdPolySymbolKind.CLASS)
        initialize {
            cacheDependencies(PsiModificationTracker.MODIFICATION_COUNT)
            collectClassDeclsRecursively(element, ::add)
        }
    }

private fun collectClassDeclsRecursively(root: PsiElement, consumer: (PolySymbol) -> Unit) {
    val worklist = ArrayDeque<PsiElement>()
    worklist.add(root)
    while (worklist.isNotEmpty()) {
        val children = PsiTreeUtil.getStubChildrenOfTypeAsList(worklist.removeFirst(), GdClassDeclTl::class.java)
        children.forEach {
            GdPsiClassSymbolFactory.create(it)?.let(consumer)
        }
        worklist.addAll(children)
    }
}
