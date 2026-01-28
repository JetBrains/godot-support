package gdscript.utils

import com.intellij.openapi.project.Project
import config.index.impl.GdConfigOperatorDataIndex
import gdscript.GdKeywords

object GdOperand {

    fun getReturnType(left: String, right: String, operator: String, project: Project): String {
        if (operator == "[]" && left.startsWith("Array[")) return left.removePrefix("Array[").removeSuffix("]")
        if (operator == "[]" && left.startsWith("Dictionary[")) return left.removePrefix("Dictionary[").substringBefore(",")
        val typed = getOperand(left, operator, right, project)
        if (typed == null && operator == "[]") {
            val result = GdConfigOperatorDataIndex.INSTANCE.getGlobally(left, project).firstOrNull()
                ?.operationList?.firstOrNull()?.rightTyped ?: ""

            if (result.isEmpty())
                return if (left == "Array" || left == "Dictionary") GdKeywords.VARIANT else result
            return result
        }

        return typed ?: ""
    }

    fun isAllowed(left: String, right: String, operator: String, project: Project): Boolean {
        return getOperand(left, operator, right, project) != null
    }

    private fun parseOperator(operator: String): String {
        for (prefix in TO_TRIM) {
            if (operator == "$prefix=") {
                return prefix
            }
        }

        return operator
    }

    val TO_TRIM = arrayOf("+", "-", "*", "/", "**", "%", "&", "|", "^", "<<", ">>")

    private fun getOperand(left: String, operand: String, right: String, project: Project): String? {
        val op = parseOperator(operand)
        val operator = GdConfigOperatorDataIndex.INSTANCE.getGlobally(left, project).firstOrNull() ?: return null

        return operator.operationList.find { it.operand == op && it.leftTyped == right }?.rightTyped
            ?: operator.operationList.find { it.operand == op && it.leftTyped == GdKeywords.VARIANT }?.rightTyped
    }

}
