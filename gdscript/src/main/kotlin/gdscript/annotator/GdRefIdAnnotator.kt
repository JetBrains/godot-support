package gdscript.annotator

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.psi.util.nextLeaf
import com.intellij.polySymbols.PolySymbol
import gdscript.GdKeywords
import gdscript.GdScriptBundle
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.gdHasConstructor
import gdscript.polySymbols.resolve.GdSymbolResolverUtil.resolveSymbolReference
import gdscript.psi.GdEnumDeclTl
import gdscript.psi.GdNodePath
import gdscript.psi.GdRefIdRef
import gdscript.psi.GdTypes
import gdscript.psi.utils.GdClassMemberUtil
import gdscript.settings.GdProjectSettingsState
import gdscript.settings.GdProjectState
import gdscript.utils.PsiElementUtil.getCallExpr

/**
 * Reports unresolved references and the SDK builtin-type-assignability error.
 * Coloring is handled by [gdscript.polySymbols.highlighting.GdPolySymbolHighlightingCustomizer].
 */
class GdRefIdAnnotator : Annotator {

    private val objectContinuation = setOf(GdTypes.LRBR, GdTypes.LSBR, GdTypes.DOT)
    private val unresolvedTolerantTypes = setOf(GdKeywords.VARIANT, "Node", "Resource", "null")

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        val state = GdProjectSettingsState.getInstance(element).state.annotators
        if (element !is GdRefIdRef) return
        val txt = element.text

        if (txt == GdKeywords.SELF || txt == GdKeywords.SUPER) return
        if (GdKeywords.MATH_CONSTANTS.contains(txt)) return

        val symbol = element.resolveSymbolReference()
        if (symbol != null) {
            checkBuiltinTypeAssignability(element, symbol, holder)
            return
        }

        // Nothing was found in Poly Symbols
        if (txt == "new" || GdClassMemberUtil.calledUpon(element)?.returnType == "Dictionary") {
            return
        }

        val calledUponExpr = GdClassMemberUtil.calledUpon(element)
        // For undefined types do not mark it as error
        if (calledUponExpr != null) {
            // If qualifier is a node path, skip error
            if (PsiTreeUtil.findChildOfType(calledUponExpr, GdNodePath::class.java) != null) return

            // If qualifier resolves to a named enum, allow enum member access only for existing enum values
            run {
                val decl = GdClassMemberUtil.findDeclaration(calledUponExpr)
                if (decl is GdEnumDeclTl) {
                    val isMember = decl.enumValueList.any { it.enumValueNmi.name == txt }
                    if (isMember) return
                    // otherwise, fall through to unresolved reference error
                }
            }

            val callType = calledUponExpr.returnType
            if (callType in unresolvedTolerantTypes) return
        }

        if (element.getCallExpr() != null && GdClassMemberUtil.hasMethodCheck(element)) return

        holder
            .newAnnotationGd(
                GdProjectState.selectedLevel(state),
                GdScriptBundle.message("annotator.message.reference.not.found", element.text)
            )
            .range(element.textRange)
            .create()
    }

    /**
     * SDK builtin value types with an explicit constructor (e.g. `Vector2`) can only be used via
     * `.new()`/call syntax, never assigned bare to a variable. This is an assignability check, not
     * a resolution problem, so it only runs once resolution to a class symbol succeeds.
     */
    private fun checkBuiltinTypeAssignability(element: GdRefIdRef, symbol: PolySymbol, holder: AnnotationHolder) {
        if (symbol.kind != GdPolySymbolKind.CLASS) return
        if (!symbol.gdHasConstructor) return

        val nextLeaf = element.nextLeaf(true)
        if (objectContinuation.contains(nextLeaf.elementType)) return

        holder
            .newAnnotationGd(
                HighlightSeverity.ERROR,
                GdScriptBundle.message("annotator.builtin.type.cannot.be.assigned.to.a.variable", element.text)
            )
            .range(element.textRange)
            .create()
    }
}
