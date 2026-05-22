package gdscript.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import gdscript.GdScriptBundle
import gdscript.inspection.fixes.GdCallableCallDeferredFix
import gdscript.inspection.util.ProblemsHolderExtension.registerWeakWarning
import gdscript.psi.GdAttributeEx
import gdscript.psi.GdCallEx
import gdscript.psi.GdLiteralEx
import gdscript.psi.GdTypes
import gdscript.psi.GdVisitor
import gdscript.psi.utils.GdClassMemberUtil

class GdCallableCallDeferredInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : GdVisitor() {
            override fun visitCallEx(element: GdCallEx) {
                val expr = element.expr
                val base = if (expr is GdAttributeEx) expr.expr else null
                val baseMethodName = if (expr is GdAttributeEx) {
                    expr.refId?.text
                } else {
                    expr.text
                }

                if (baseMethodName != "call_deferred") return

                val argList = element.argList ?: return
                val args = argList.argExprList
                if (args.isEmpty()) return

                val firstArg = args[0].expr
                if (firstArg is GdLiteralEx) {
                    val methodName = firstArg.stringVal?.text?.trim('"', '\'')
                        ?: if (firstArg.node.findChildByType(GdTypes.STRING_NAME) != null) {
                            firstArg.text.trim('"', '\'', '&')
                        } else {
                            null
                        }

                    if (methodName == null) return

                    val searchContext = (element.expr as? GdAttributeEx)?.refId ?: element.expr
                    if (GdClassMemberUtil.listDeclarations(searchContext, methodName).isEmpty()) return

                    val remainingArgs = args.drop(1).joinToString(", ") { it.text }

                    holder.registerWeakWarning(
                        element,
                        GdScriptBundle.message("inspection.callable.call.deferred.description"),
                        GdCallableCallDeferredFix(element, base?.text, methodName, remainingArgs)
                    )
                }
            }
        }
    }
}
