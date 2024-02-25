package gdscript.inspection.validator

import gdscript.psi.GdStmt

data class GdMethodValidationResult(
        val returnTypes : Set<String>,
        val invalidReturns : List<Pair<GdStmt, String>>,
        val unreachableStatements : List<GdStmt>,
        val alwaysReturns : Boolean
) {

    fun noReturn() : Boolean {
        return returnTypes.isEmpty()
    }

    fun hasReturn() : Boolean {
        return returnTypes.isNotEmpty()
    }

}