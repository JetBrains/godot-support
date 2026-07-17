package gdscript.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdScriptBundle
import gdscript.action.quickFix.GdAddMatchBranchesFix
import gdscript.inspection.util.ProblemsHolderExtension.registerWeakWarning
import gdscript.polySymbols.GdPolySymbolKind
import gdscript.polySymbols.gdDeclaringClassId
import gdscript.polySymbols.gdDeclaringClassName
import gdscript.polySymbols.gdEnumValues
import gdscript.polySymbols.gdIsEngineSymbol
import gdscript.polySymbols.gdNavigationElement
import gdscript.polySymbols.resolve.GdSymbolResolverUtil.resolveSymbolReference
import gdscript.psi.GdMatchSt
import gdscript.psi.GdRefIdRef
import gdscript.psi.GdTypeHintRef
import gdscript.psi.GdVisitor
import gdscript.psi.utils.GdClassUtil
import gdscript.psi.utils.GdInheritanceUtil

class GdMissingMatchBranchesInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : GdVisitor() {
            override fun visitMatchSt(match: GdMatchSt) {
                val expr = match.expr ?: return

                val id = PsiTreeUtil.getDeepestLast(expr).parent
                if (id !is GdRefIdRef) return

                if (match.matchBlockList.any { it.stmtOrSuite == null || it.stmtOrSuite?.text?.trim() == "" }) return

                val symbol = id.resolveSymbolReference() ?: return
                val rootDecl = symbol.gdNavigationElement?.parent ?: return
                val typeHint = PsiTreeUtil.findChildrenOfType(rootDecl, GdTypeHintRef::class.java).lastOrNull() ?: return
                val enumSymbol = typeHint.resolveSymbolReference()?.takeIf { it.kind == GdPolySymbolKind.ENUM } ?: return

                val usedKeys = match.matchBlockList.flatMap { block ->
                    block.patternList.patternList.map {
                        if (it.text == "_") return
                        it.text
                    }
                }

                val allKeys = (enumSymbol.gdEnumValues ?: return).toMutableSet()

                var prefix = ""
                val owningClassId = enumSymbol.gdDeclaringClassName ?: return
                val fullOwnerClassId = enumSymbol.gdDeclaringClassId ?: return

                if (!GdInheritanceUtil.isExtending(expr, owningClassId)) {
                    val myId = GdClassUtil.getFullClassId(expr)
                    prefix = if ("$myId.$owningClassId" == fullOwnerClassId) "$owningClassId." else "$fullOwnerClassId."
                }

                // SDK "enums" (grouped from doc-XML <constant enum="..."> entries) are documentation
                // metadata only - their values are real GDScript code are always accessed as flat
                // class constants (Input.MOUSE_MODE_VISIBLE), never qualified by the enum's own group
                // name (Input.MouseMode.MOUSE_MODE_VISIBLE, which isn't valid GDScript). User-declared
                // named enums (enum State { ... }) are the opposite: State.IDLE always requires the
                // enum name. Confirmed empirically: resolving Input.MOUSE_MODE_VISIBLE yields a plain
                // GdSdkConstantSymbol, not a nested enum-value access.
                if (!enumSymbol.gdIsEngineSymbol) {
                    prefix += "${enumSymbol.name}."
                }

                usedKeys.forEach {
                    if (it.startsWith(prefix)) {
                        allKeys.remove(it.removePrefix(prefix))
                    }
                }

                if (allKeys.isEmpty()) return

                holder.registerWeakWarning(
                    expr,
                    GdScriptBundle.message("inspection.missing.enum.options"),
                    GdAddMatchBranchesFix(match, allKeys.map { "$prefix$it" }.toTypedArray()),
                )
            }
        }
    }
}
