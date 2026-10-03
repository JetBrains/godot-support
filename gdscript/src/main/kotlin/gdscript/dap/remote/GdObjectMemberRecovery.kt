package gdscript.dap.remote

import com.intellij.diagnostic.rethrowControlFlowException
import com.intellij.openapi.diagnostic.logger
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapStructuredVariable
import com.intellij.platform.dap.DapVariable
import com.jetbrains.dap.protocol.RequestCancelledByPeerException

private val LOG = logger<GdObjectMemberRecovery>()
private const val RECOVERY_WIDTH: Int = 1 + GdObjectProbe.WIDTH

/**
 * Godot reports Object members in property categories as integer ObjectIDs.
 * Recover only properties with unambiguous readable names. Evaluate their values in one batch through the checked frame.
 * Replace a row only when its matching result is a structured Object.
 * If evaluation fails or the result shape changes, keep the original rows and log the failure.
 * Caller cancellation propagates. An integer alone does not establish object identity.
 */
internal object GdObjectMemberRecovery {
    /** An inherited debugger label is not a property name. Reject shadowed or synthetic labels. */
    fun readablePropertyNames(category: String, rows: List<DapVariable>): List<String?> {
        val names = rows.map { row ->
            if (skipRecovery(category, row.name)) return@map null
            when (category) {
                "Members" -> {
                    val slash = row.name.indexOf('/')
                    if (slash >= 0 && row.name.substring(0, slash).endsWith(".gd")) row.name.substring(slash + 1)
                    else row.name
                }
                else -> row.name
            }
        }
        val ambiguous = names.filterNotNull().groupingBy { it }.eachCount().filterValues { it > 1 }.keys
        return names.map { it?.takeUnless { name -> name.isEmpty() || name in ambiguous } }
    }

    /**
     * Constants can have the same name as a member. `GDScriptInstance::get` selects the member first.
     * The Node category also contains the synthetic `multiplayer_authority` row.
     * Keep these rows without a warning.
     * Warn about ambiguous names only when no recovery candidate remains and an ordinary integer row contains a numeric value.
     */
    private fun skipRecovery(category: String, name: String): Boolean =
        category == "Constants" || category == "Node" && name == "multiplayer_authority"

    /** Only real integer properties can be lost ObjectIDs. */
    fun recoveryCandidates(category: String, rows: List<DapVariable>): List<Pair<Int, String>> {
        val names = readablePropertyNames(category, rows)
        return rows.mapIndexedNotNull { index, row ->
            val name = names[index]
            if (row.type == "int" && row !is DapStructuredVariable && row.value.toLongOrNull() != null && name != null)
                index to name else null
        }
    }

    /** Exact size and index order matter: never pair a result by the current UI row position. */
    fun replaceConfirmedObjects(
        rows: List<DapVariable>, candidates: List<Pair<Int, String>>, result: List<DapVariable>,
        presentations: List<GdObjectPresentation?>, dictionaryCount: Int = 0
    ): List<DapVariable>? {
        val length = candidates.size * RECOVERY_WIDTH + dictionaryCount
        if (presentations.size != candidates.size || result.size != length + 1 ||
            result.firstOrNull()?.name != "size" || result.first().value.toIntOrNull() != length ||
            result.drop(1).indices.any { result[it + 1].name != it.toString() }) return null
        val replaced = rows.toMutableList()
        candidates.forEachIndexed { index, (rowIndex, _) ->
            val original = rows[rowIndex]
            val recovered = result[index * RECOVERY_WIDTH + 1]
            val presentation = presentations[index]
            // A reference alone can also denote an Array or Dictionary.
            if (presentation != null && recovered.type == "Object" && recovered is DapStructuredVariable) {
                replaced[rowIndex] = object : DapStructuredVariable by recovered, GdPresentedVariable {
                    override val name: String = original.name
                    override val evaluateName: String? = original.evaluateName
                    override val objectPresentation: GdObjectPresentation = presentation
                }
            }
        }
        return replaced
    }

    suspend fun DapSessionContext.recoverCategory(
        evaluator: GdSelfCheckedFrame, owner: GdOwnerExpression, category: String, rows: List<DapVariable>
    ): List<DapVariable> {
        val candidates = recoveryCandidates(category, rows)
        if (candidates.isEmpty()) {
            if (rows.any { it.type == "int" && it.value.toLongOrNull() != null && !skipRecovery(category, it.name) })
                LOG.warn("Object member recovery skipped in $category: no unambiguous property names")
            return rows
        }
        try {
            val outcome = evaluateStructuredOutcome(evaluator, GdDapExpressions.recovery(owner, candidates))
            if (outcome !is GdEvalOutcome.Structured) {
                if (outcome !is GdEvalOutcome.Failed || outcome.kind != GdEvalOutcome.Failed.Kind.RESPONSE_MISMATCH)
                    LOG.warn("Object member recovery failed in $category: $outcome")
                return rows
            }
            val result = when (val load = loadVariablesBounded(outcome.variable)) {
                is GdVariablesLoad.Success -> load.variables
                is GdVariablesLoad.Failed -> {
                    LOG.warn("Object member recovery failed in $category: ${load.message}")
                    return rows
                }
            }
            val presentations = GdObjectProbe.read(result, candidates.size, owner, width = RECOVERY_WIDTH)
            if (presentations == null) {
                LOG.warn("Object member recovery dictionary size or result shape changed in $category")
                return rows
            }
            return replaceConfirmedObjects(rows, candidates, result, presentations, owner.dictionaries.size) ?: rows.also {
                LOG.warn("Object member recovery result shape changed in $category")
            }
        }
        catch (e: Exception) {
            if (e !is RequestCancelledByPeerException) rethrowControlFlowException(e)
            LOG.warn("Object member recovery failed in $category", e)
            return rows
        }
    }
}
