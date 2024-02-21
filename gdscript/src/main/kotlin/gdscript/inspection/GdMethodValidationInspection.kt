package gdscript.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemHighlightType.GENERIC_ERROR
import com.intellij.codeInspection.ProblemHighlightType.WEAK_WARNING
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.childrenOfType
import gdscript.GdKeywords
import gdscript.inspection.quickFix.GdAddReturnTypeHintFix
import gdscript.inspection.quickFix.GdChangeReturnTypeFix
import gdscript.psi.*
import gdscript.psi.impl.GdMatchBlockImpl
import gdscript.psi.utils.GdExprUtil
import gdscript.psi.utils.GdInheritanceUtil
import gdscript.psi.utils.GdMethodUtil
import gdscript.psi.utils.GdStmtUtil

class GdInvalidMethodInspection : LocalInspectionTool() {

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
                val returnTypeResult = ReturnTypesResult()
                stmtOrSuite?.let { traverseSuite(returnTypeResult, stmtOrSuite, hint, returnType) }

                // no return statement when required
                if (returnType.isNotEmpty() && returnType != "void" && returnTypeResult.returnTypes.isEmpty()) {
                    holder.registerProblem(methodId, "Function's require a return value", GENERIC_ERROR)
                // no return type specified
                } else if(returnType.isEmpty() && returnTypeResult.returnTypes.isNotEmpty())  {
                    val commonType = determineCommonType(returnTypeResult.returnTypes)
                    if (commonType == null) {
                        holder.registerProblem(methodId, "Function's return type is not specified", WEAK_WARNING)
                    } else {
                        holder.registerProblem(
                                methodId,
                                "Function's return type can be specified as $commonType",
                                WEAK_WARNING,
                                GdAddReturnTypeHintFix(methodId, commonType)
                        )
                    }
                }

                if (methodId is GdMethodIdNmi) validateParentType(methodId, hint, returnType)
            }

            private fun validateConstructor(methodId: GdMethodIdNmi, method: GdMethodDeclTl) {
                // If parent has parameters - child must call super(args)
                val parent = GdInheritanceUtil.getExtendedElement(method, holder.project) ?: return;
                val parentConstructor = GdMethodUtil.findMethod(parent, GdKeywords.INIT_METHOD) ?: return;
                if (parentConstructor.parameters.isEmpty()) return;

                val stmts = PsiTreeUtil.findChildrenOfType(method, GdCallEx::class.java)
                        .filter{ it.expr.text == GdKeywords.SUPER };
                if (stmts.isEmpty()) {
                    holder.registerProblem(methodId, "Initializing super() constructor is required", GENERIC_ERROR)
                }
            }

            private fun traverseSuite(result: ReturnTypesResult, stmtOrSuite: GdStmtOrSuite, hint : GdReturnHintVal?, returnType: String) {
                // single statement
                if (stmtOrSuite.stmt != null) {
                    return traverseStatement(result, listOf(stmtOrSuite.stmt!!), hint, returnType)
                }

                // traverse suites
                for (suite in stmtOrSuite.suiteList) {
                    traverseStatement(result, suite.childrenOfType<GdStmt>(), hint, returnType)
                }
            }

            private fun traverseStatement(result: ReturnTypesResult, statements: List<GdStmt>, hint : GdReturnHintVal?, returnType: String) {
                var seenReturn = false

                for (statement in statements) {
                    // if it always returns, this code is unreachable
                    if (seenReturn) {
                        holder.registerProblem(statement, "Code is unreachable", GENERIC_ERROR)
                    } else if (statement is GdFlowSt && statement.type == GdKeywords.FLOW_RETURN) {
                        result.addReturnType(validateReturnStatement(statement, hint, returnType))
                        seenReturn = true
                    } else if (GdStmtUtil.isNestedStatement(statement)) {
                        traverseNestedStatement(result, statement, hint, returnType)
                    }
                }
            }

            private fun traverseNestedStatement(result: ReturnTypesResult, statement: GdStmt, hint : GdReturnHintVal?, returnType: String) {
                if (statement is GdMatchSt) {
                    for (matchBlock in statement.childrenOfType<GdMatchBlockImpl>()) {
                        for (nestedElems in matchBlock.childrenOfType<GdStmtOrSuite>()) {
                            traverseSuite(result, nestedElems, hint, returnType)
                        }
                    }
                } else {
                    for (nestedElems in statement.childrenOfType<GdStmtOrSuite>()) {
                        traverseSuite(result, nestedElems, hint, returnType)
                    }
                }
            }

            private fun validateReturnStatement(statement: GdFlowSt, hint : GdReturnHintVal?, returnType: String) : String {
                val stmtType = statement.expr?.returnType ?: GdKeywords.VOID;
                if (!GdExprUtil.typeAccepts(stmtType, returnType, holder.project)) {
                    holder.registerProblem(
                            statement,
                            "Returns a type [$stmtType] which do not match function's [$returnType]",
                            GENERIC_ERROR,
                            hint?.let { GdChangeReturnTypeFix(hint, stmtType) }
                    )
                }
                return stmtType
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
                    holder.registerProblem(
                            methodId,
                            "Return type [$returnType] does not match parent's [${parent.returnType}]",
                            WEAK_WARNING,
                            hint?.let { GdChangeReturnTypeFix(hint, parent.returnType) }
                    )
                }
            }
        }
    }

}

private fun ProblemsHolder.registerProblem(element: PsiElement, description: String, highlightType: ProblemHighlightType, quickFix: LocalQuickFix?) {
    if (quickFix == null) {
        this.registerProblem(element, description, highlightType)
    } else {
        this.registerProblem(element, description, highlightType, quickFix)
    }
}

class ReturnTypesResult() {

    val returnTypes = mutableSetOf<String>()

    fun addReturnType(type: String) {
        returnTypes.add(type)
    }

}
