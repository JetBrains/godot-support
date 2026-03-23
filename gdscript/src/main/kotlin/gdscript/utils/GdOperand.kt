package gdscript.utils

import com.intellij.openapi.project.Project
import gdscript.GdKeywords
import gdscript.polySymbols.index.GdPolySymbolQueriesUtil

object GdOperand {

    fun getReturnType(left: String, right: String, operator: String, project: Project): String {
        if (operator == "[]" && left.startsWith("Array[")) return left.removePrefix("Array[").removeSuffix("]")
        if (operator == "[]" && left.startsWith("Dictionary[")) return left.removePrefix("Dictionary[").substringBefore(",")

        return getOperand(left, operator, right, project) ?: ""
    }

    fun isAllowed(left: String, right: String, operator: String, project: Project): Boolean {
        return getOperand(left, operator, right, project) != null
    }

    private fun getOperand(left: String, operand: String, right: String, project: Project): String? {
        val operator = GdPolySymbolQueriesUtil.getOperatorSymbol(project, left, operand, right)
        if (operator != null)
            return operator.returnType

        return GdPolySymbolQueriesUtil.getOperatorSymbol(project, left, operand, GdKeywords.VARIANT)?.returnType
    }

}
