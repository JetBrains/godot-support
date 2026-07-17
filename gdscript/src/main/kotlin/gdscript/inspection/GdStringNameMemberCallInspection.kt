package gdscript.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.polySymbols.PolySymbol
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import gdscript.GdScriptBundle
import gdscript.inspection.fixes.GdStringNameMemberCallFix
import gdscript.inspection.util.ProblemsHolderExtension.registerWeakWarning
import gdscript.polySymbols.GdPolySymbolModifier
import gdscript.polySymbols.gdSignature
import gdscript.polySymbols.resolve.GdSymbolResolverUtil
import gdscript.polySymbols.scope.hasModifier
import gdscript.psi.GdAttributeEx
import gdscript.psi.GdCallEx
import gdscript.psi.GdLiteralEx
import gdscript.psi.GdTypes
import gdscript.psi.GdVisitor
import gdscript.psi.utils.GdClassMemberUtil

enum class QfCandidate {
    Signal,
    Method
}

class GdStringNameMemberCallInspection : LocalInspectionTool() {
    // false positives from the candidate filtering
    private val blackList: HashSet<String> = hashSetOf<String>(
        "call_deferred_thread_group",
        "call_thread_safe",
        "propagate_call",
        "rpc_config",
        "get_method_argument_count",
        "has_method",
        "has_signal",
        "has_user_signal",
        "remove_user_signal",
    )

    // sadly there are cases, where the replacement is named differently from the string variant
    private val replacementName: HashMap<String, String> = hashMapOf(
        "get_signal_connection_list" to "get_connections",
        "emit_signal" to "emit",
    )

    // Only used for testing if another potential quick fix candidate was added.
    fun getQfCandidateCount(element: PsiElement): Int {
        return fetchNodeMethods(element).count { isQfCandidate(it) != null }
    }

    private fun isQfCandidate(method: PolySymbol): QfCandidate? {
        val firstParam = method.gdSignature?.parameters?.firstOrNull() ?: return null
        if (method.name in blackList) return null
        if (firstParam.type != "StringName") return null
        if (firstParam.name == "signal") return QfCandidate.Signal
        if (firstParam.name == "method") return QfCandidate.Method
        return null
    }

    private fun fetchNodeMethods(element: PsiElement): List<PolySymbol> {
        val nodeClass = GdSymbolResolverUtil.resolveCanonicalClassSymbol(element.project, "Node", element)
        return GdSymbolResolverUtil.listMethodSymbols(nodeClass)
            .filterNot { it.hasModifier(GdPolySymbolModifier.STATIC) }
    }

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : GdVisitor() {
            override fun visitCallEx(element: GdCallEx) {
                val expr = element.expr
                val attrExpr = expr as? GdAttributeEx
                val base = attrExpr?.expr
                val baseMethodName = attrExpr?.refId?.text ?: expr.text

                val methods = fetchNodeMethods(element)
                val candidate = methods
                    .asSequence()
                    .filter { it.name == baseMethodName }
                    .firstNotNullOfOrNull { isQfCandidate(it) }
                    ?: return
                val argList = element.argList ?: return
                val args = argList.argExprList
                if (args.isEmpty()) return

                val firstArg = args[0].expr
                if (firstArg is GdLiteralEx) {
                    val firstArgName = firstArg.stringVal?.text?.trim('"', '\'')
                        ?: if (firstArg.node.findChildByType(GdTypes.STRING_NAME) != null) {
                            firstArg.text.trim('"', '\'', '&')
                        } else {
                            null
                        }

                    if (firstArgName == null) return

                    val searchContext = attrExpr?.refId ?: expr
                    if (GdClassMemberUtil.listDeclarations(searchContext, firstArgName).isEmpty()) return
                    val description = when (candidate) {
                        QfCandidate.Signal -> GdScriptBundle.message("inspection.string.name.signal.description")
                        QfCandidate.Method -> GdScriptBundle.message("inspection.string.name.callable.description")
                    }

                    val remainingArgs = args.drop(1).joinToString(", ") { it.text }
                    holder.registerWeakWarning(
                        element, description,
                        GdStringNameMemberCallFix(
                            element, base?.text, firstArgName,
                            replacementName[baseMethodName] ?: baseMethodName,
                            baseMethodName,
                            remainingArgs,
                            candidate
                        )
                    )
                }
            }
        }
    }
}
