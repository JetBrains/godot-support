package gdscript.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor

/**
 * Never actually runs (registered with `enabledByDefault="false"` and never enabled). It exists
 * only as a `polySymbols.inspectionToolMapping` target for the `qualifiable-symbol` kind's
 * `UnknownSymbol` problem: without a mapping, the platform's own generic
 * `PolySymbolHighlightingAnnotator` reports unresolved `GdRefIdRef` own-references with a
 * hardcoded WARNING that bypasses [gdscript.annotator.GdRefIdAnnotator]'s tolerance heuristics and
 * severity settings entirely. Mapping to a disabled tool suppresses that generic diagnostic so
 * `GdRefIdAnnotator` stays the sole real "reference not found" diagnostic, while own-references
 * can still register an unresolved match (needed for Find Usages/rename reachability).
 */
class GdUnrecognizedIdentifierInspection : LocalInspectionTool() {
    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        PsiElementVisitor.EMPTY_VISITOR
}
