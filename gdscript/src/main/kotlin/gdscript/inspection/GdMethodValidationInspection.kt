package gdscript.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import gdscript.GdKeywords
import gdscript.GdScriptBundle
import gdscript.inspection.fixes.GdAddReturnTypeHintFix
import gdscript.inspection.fixes.GdChangeReturnTypeFix
import gdscript.inspection.util.ProblemsHolderExtension.registerGenericError
import gdscript.inspection.util.ProblemsHolderExtension.registerWeakWarning
import gdscript.inspection.validator.GdMethodValidator
import gdscript.psi.GdCallEx
import gdscript.psi.GdFuncDeclEx
import gdscript.psi.GdMethodDeclTl
import gdscript.psi.GdMethodIdNmi
import gdscript.psi.GdNamedIdElement
import gdscript.psi.GdReturnHintVal
import gdscript.psi.GdStmtOrSuite
import gdscript.psi.GdVisitor
import gdscript.psi.utils.GdExprUtil
import gdscript.psi.utils.GdInheritanceUtil
import gdscript.psi.utils.GdMethodUtil

class GdMethodValidationInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor {
        return object : GdVisitor() {

            override fun visitFuncDeclEx(o: GdFuncDeclEx) {
                if (o.funcDeclIdNmi == null) return
                validateMethod(o.funcDeclIdNmi!!, o.returnHint?.returnHintVal, o.returnType, o.stmtOrSuite)
            }

            override fun visitMethodDeclTl(o: GdMethodDeclTl) {
                if (o.methodIdNmi == null) return
                validateMethod(o.methodIdNmi!!, o.returnHint?.returnHintVal, o.returnType, o.stmtOrSuite)
                if (o.name == GdKeywords.INIT_METHOD) validateConstructor(o.methodIdNmi!!, o)
            }

            private fun validateMethod(methodId: GdNamedIdElement, hint : GdReturnHintVal?, returnType: String, stmtOrSuite: GdStmtOrSuite?) {
                val validationResult = GdMethodValidator(holder.project, returnType).validate(stmtOrSuite)

                validationResult.unreachableStatements.forEach{
                    holder.registerWeakWarning(it, GdScriptBundle.message("inspection.method.validation.unreachable.code"))
                }

                validationResult.invalidReturns.forEach{
                    val stmtType = it.second
                    holder.registerGenericError(
                            it.first,
                        GdScriptBundle.message("inspection.method.validation.non.matching.return.type", stmtType,returnType),
                            hint?.let { GdChangeReturnTypeFix(hint, stmtType) }
                    )
                }

                // no return statement when required
                if (returnType.isNotEmpty() && validationResult.noReturn() && returnType != "void") {
                    holder.registerGenericError(methodId, GdScriptBundle.message("inspection.method.validation.unspecified.return.value"))
                } else if (!validationResult.voidOnlyReturn() && !validationResult.alwaysReturns) {
                    holder.registerGenericError(methodId,
                        GdScriptBundle.message("inspection.method.validation.not.all.code.paths.return.a.value")
                    )
                }
                // no return type specified
                if(returnType.isEmpty() && validationResult.hasReturn())  {
                    val commonType = determineCommonType(validationResult.returnTypes)
                    if (commonType == null) {
                        holder.registerWeakWarning(methodId, GdScriptBundle.message("inspection.method.validation.unspecified.return.type"))
                    } else {
                        holder.registerWeakWarning(
                                methodId,
                                GdScriptBundle.message("inspection.method.validation.return.type.can.be.specified.as", commonType),
                                GdAddReturnTypeHintFix(methodId, commonType)
                        )
                    }
                }

                if (methodId is GdMethodIdNmi) validateParentType(methodId, hint, returnType)
            }

            private fun validateConstructor(methodId: GdMethodIdNmi, method: GdMethodDeclTl) {
                // If parent has parameters - child must call super(args)
                val parent = GdInheritanceUtil.getExtendedElement(method, holder.project) ?: return
                val parentConstructor = GdMethodUtil.findMethod(parent, GdKeywords.INIT_METHOD) ?: return
                if (parentConstructor.parameters.isEmpty()) return

                val stmts = PsiTreeUtil.findChildrenOfType(method, GdCallEx::class.java)
                        .filter{ it.expr.text == GdKeywords.SUPER }
                if (stmts.isEmpty()) {
                    holder.registerGenericError(methodId, GdScriptBundle.message("inspection.method.validation.missing.super.call"))
                }
            }

            private fun determineCommonType(types: Set<String>) : String? {
                if (types.isEmpty()) return null
                if (types.size == 1) return types.first()

                for (type in types) {
                    if (types.all { GdExprUtil.typeAccepts(it, type, holder.project) }) return type
                }
                return null
            }

            private fun validateParentType(methodId: GdMethodIdNmi, hint : GdReturnHintVal?, returnType: String) {
                val parent = GdMethodUtil.findParentMethodRecursive(methodId, holder.project) ?: return

                if (!GdExprUtil.typeAccepts(returnType, parent.returnType, holder.project)) {
                    holder.registerWeakWarning(
                            methodId,
                        GdScriptBundle.message("inspection.method.validation.return.type.does.not.match.parent.type", returnType,parent.returnType),
                            hint?.let { GdChangeReturnTypeFix(hint, parent.returnType) }
                    )
                }
            }
        }
    }

}
