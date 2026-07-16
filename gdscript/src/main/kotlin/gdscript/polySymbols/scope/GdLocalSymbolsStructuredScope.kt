package gdscript.polySymbols.scope

import com.intellij.model.Pointer
import com.intellij.polySymbols.PolySymbolKind
import com.intellij.polySymbols.utils.PolySymbolStructuredScope
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.PsiRecursiveElementVisitor
import com.intellij.psi.createSmartPointer
import com.intellij.psi.util.PsiTreeUtil
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.psi.GdPsiBindingPatternSymbol
import gdscript.polySymbols.psi.GdPsiForVariableSymbol
import gdscript.polySymbols.psi.GdPsiLocalConstantSymbol
import gdscript.polySymbols.psi.GdPsiLocalVariableSymbol
import gdscript.polySymbols.psi.GdPsiParameterSymbol
import gdscript.psi.GdConstDeclSt
import gdscript.psi.GdElifSt
import gdscript.psi.GdElseSt
import gdscript.psi.GdFile
import gdscript.psi.GdForSt
import gdscript.psi.GdFuncDeclEx
import gdscript.psi.GdIfSt
import gdscript.psi.GdMatchBlock
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdPattern
import gdscript.psi.GdVarDeclSt
import gdscript.psi.GdVarNmi
import gdscript.psi.GdWhileSt

/**
 * Resolves local variables, constants, parameters, for-loop variables, and match-binding patterns
 * that are visible at an unqualified [GdRefIdRef][gdscript.psi.GdRefIdRef] reference position.
 *
 * Builds one scope tree per file, mirroring PSI nesting (class/method/lambda bodies, for/match/if/
 * while blocks). Locals within the same flat suite are treated as mutually visible regardless of
 * textual order (an accepted simplification — [PolySymbolStructuredScope] scopes on PSI
 * containment, not statement order; see the migration plan for the "used before declared" case
 * this intentionally leaves imprecise).
 */
class GdLocalSymbolsStructuredScope(location: PsiElement) : PolySymbolStructuredScope<PsiElement, GdFile>(location) {

    override val rootPsiElement: GdFile?
        get() = location.containingFile as? GdFile

    override val providedSymbolKinds: Set<PolySymbolKind> = setOf(
        GdPolySymbolKind.LOCAL_VARIABLE,
        GdPolySymbolKind.LOCAL_CONSTANT,
        GdPolySymbolKind.PARAMETER,
        GdPolySymbolKind.FOR_VARIABLE,
        GdPolySymbolKind.BINDING_PATTERN,
    )

    override val scopesBuilderProvider: (GdFile, PolySymbolPsiScopesHolder) -> PsiElementVisitor? =
        { _, holder -> LocalScopesVisitor(holder) }

    override fun createPointer(): Pointer<out PolySymbolStructuredScope<PsiElement, GdFile>> {
        val locationPtr = location.createSmartPointer()
        return Pointer { locationPtr.element?.let { GdLocalSymbolsStructuredScope(it) } }
    }

    /**
     * Must be genuinely, synchronously recursive (not [com.intellij.psi.PsiRecursiveElementWalkingVisitor],
     * which defers child visitation via its own internal walking state) — the push/pop scope
     * pairing below relies on all of an element's descendants being visited before control returns
     * past its own `pushScope`/`popScope` pair.
     */
    private class LocalScopesVisitor(private val holder: PolySymbolPsiScopesHolder) : PsiRecursiveElementVisitor() {
        override fun visitElement(element: PsiElement) {
            val pushedScope = when (element) {
                is GdMethodDeclTl -> {
                    holder.pushScope(element) {
                        element.paramList?.paramList?.forEach { addSymbol(GdPsiParameterSymbol(it.varNmi)) }
                    }
                    true
                }
                is GdFuncDeclEx -> {
                    holder.pushScope(element) {
                        element.paramList?.paramList?.forEach { addSymbol(GdPsiParameterSymbol(it.varNmi)) }
                    }
                    true
                }
                is GdForSt -> {
                    holder.pushScope(element) {
                        element.varNmi?.let { addSymbol(GdPsiForVariableSymbol(it)) }
                    }
                    true
                }
                is GdMatchBlock, is GdIfSt, is GdElifSt, is GdElseSt, is GdWhileSt -> {
                    holder.pushScope(element)
                    true
                }
                is GdVarDeclSt -> {
                    element.varNmi?.let { holder.currentScope { addSymbol(GdPsiLocalVariableSymbol(it)) } }
                    false
                }
                is GdConstDeclSt -> {
                    element.varNmi?.let { holder.currentScope { addSymbol(GdPsiLocalConstantSymbol(it)) } }
                    false
                }
                is GdPattern -> {
                    // A `var x` match-pattern parses as a bare GdVarNmi directly under GdPattern -
                    // there is no separate GdBindingPattern node for this case (array/dict
                    // sub-patterns and plain expressions are the other, mutually exclusive shapes
                    // GdPattern can wrap, and none of them have a GdVarNmi as a direct child).
                    PsiTreeUtil.getChildOfType(element, GdVarNmi::class.java)?.let {
                        holder.currentScope { addSymbol(GdPsiBindingPatternSymbol(it)) }
                    }
                    false
                }
                else -> false
            }
            super.visitElement(element)
            if (pushedScope) holder.popScope()
        }
    }
}
