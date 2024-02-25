package gdscript.inspection.validator

import com.intellij.openapi.project.Project
import com.intellij.psi.util.childrenOfType
import gdscript.GdKeywords
import gdscript.psi.*
import gdscript.psi.impl.GdMatchBlockImpl
import gdscript.psi.utils.GdExprUtil
import gdscript.psi.utils.GdStmtUtil

class GdMethodValidator(val project : Project, val returnType: String) {

    private val returnTypes = mutableSetOf<String>()
    private val invalidReturns = mutableListOf<Pair<GdStmt, String>>()
    private val unreachableStatements = mutableListOf<GdStmt>()
    private var alwaysReturns = false

    fun validate(stmtOrSuite: GdStmtOrSuite?): GdMethodValidationResult {
        if (stmtOrSuite != null) {
            // Traverse suite
            alwaysReturns = traverseSuite(stmtOrSuite)
        }

        return GdMethodValidationResult(returnTypes, invalidReturns, unreachableStatements, alwaysReturns)
    }

    private fun traverseSuite(stmtOrSuite: GdStmtOrSuite) : Boolean {
        // single statement
        if (stmtOrSuite.stmt != null) {
            return traverseStatements(listOf(stmtOrSuite.stmt!!))
        }

        // traverse suites
        var alwaysReturn = stmtOrSuite.suiteList.isNotEmpty()
        for (suite in stmtOrSuite.suiteList) {
            val pathReturns = traverseStatements(suite.childrenOfType<GdStmt>())
            alwaysReturn = alwaysReturn && pathReturns
        }
        return alwaysReturn
    }

    private fun traverseStatements(statements: List<GdStmt>) : Boolean {
        var alwaysReturn = false

        for (statement in statements) {
            // if it always returns, this code is unreachable
            if (alwaysReturn) {
                unreachableStatements.add(statement)
                return true
            } else if (statement is GdFlowSt && statement.type == GdKeywords.FLOW_RETURN) {
                checkReturnStatement(statement)
                alwaysReturn = true
            } else if (statement is GdMatchSt) {
                alwaysReturn = traverseMatchStatement(statement)
            } else if (statement is GdIfSt) {
                alwaysReturn = traverseIfStatement(statement)
            } else if (GdStmtUtil.isLoopStatement(statement)) {
                alwaysReturn = traverseLoopStatement(statement)
            }
        }

        return alwaysReturn
    }

    private fun traverseMatchStatement(statement: GdMatchSt) : Boolean {
        var alwaysReturn = true
        for (matchBlock in statement.childrenOfType<GdMatchBlockImpl>()) {
            for (nestedElems in matchBlock.childrenOfType<GdStmtOrSuite>()) {
                val suiteReturn = traverseSuite(nestedElems)
                alwaysReturn = alwaysReturn && suiteReturn
            }
        }
        return alwaysReturn
    }

    private fun traverseIfStatement(statement: GdIfSt) : Boolean {
        var hasElse = false
        var alwaysReturns = statement.children.isNotEmpty()

        // 'If' has a stmtOrSuite with optional elif* and else statements
        for (nestedElem in statement.children) {
            if (nestedElem is GdStmtOrSuite) {
                val suiteReturn = traverseSuite(nestedElem)
                alwaysReturns = alwaysReturns && suiteReturn
            } else if (nestedElem is GdElifSt) {
                if (nestedElem.stmtOrSuite != null) {
                    val suiteReturn = traverseSuite(nestedElem.stmtOrSuite!!)
                    alwaysReturns = alwaysReturns && suiteReturn
                }
            } else if (nestedElem is GdElseSt) {
                if (nestedElem.stmtOrSuite != null) {
                    val suiteReturn = traverseSuite(nestedElem.stmtOrSuite!!)
                    alwaysReturns = alwaysReturns && suiteReturn
                }
                hasElse = true
            }

        }
        // Only will always return when all statements have a return and there is an Else
        return alwaysReturns && hasElse
    }

    private fun traverseLoopStatement(statement: GdStmt) : Boolean {
        val suites = statement.childrenOfType<GdStmtOrSuite>()
        var alwaysReturns = suites.isNotEmpty()

        for (suite in suites) {
            val suiteReturn = traverseSuite(suite)
            alwaysReturns = alwaysReturns && suiteReturn
        }

        return alwaysReturns
    }

    private fun checkReturnStatement(statement: GdFlowSt) {
        val stmtType = statement.expr?.returnType ?: GdKeywords.VOID;
        if (!GdExprUtil.typeAccepts(stmtType, returnType, project)) {
            invalidReturns.add(Pair(statement, stmtType))
        }
        returnTypes.add(stmtType)
    }

}