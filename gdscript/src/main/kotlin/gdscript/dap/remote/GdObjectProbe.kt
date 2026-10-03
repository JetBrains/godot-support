package gdscript.dap.remote

import com.intellij.diagnostic.rethrowControlFlowException
import com.intellij.openapi.diagnostic.logger
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapStructuredVariable
import com.intellij.platform.dap.DapVariable
import com.jetbrains.dap.protocol.RequestCancelledByPeerException

private val LOG = logger<GdObjectProbe>()

internal object GdObjectProbe {
    /** Fields in each object probe: text, validity, script, global name, built-in class. */
    const val WIDTH: Int = 5

    /**
     * Selects structured Objects with a safe owner expression.
     * Watch results can lack a DAP type, so only watches permit type inference. Locals and children require a declared type.
     */
    fun isProbeTarget(variable: DapVariable, owner: GdOwnerExpression?, allowInferredType: Boolean = false): Boolean =
        owner != null && variable is DapStructuredVariable && GdValueKind.type(variable) == "Object" &&
            (allowInferredType || variable.type != null)

    /** A malformed field invalidates the whole batch, not just the row at that position. */
    fun read(rows: List<DapVariable>, count: Int, owner: GdOwnerExpression? = null, width: Int = WIDTH): List<GdObjectPresentation?>? {
        val guards = owner?.dictionaries?.size ?: 0
        val length = count * width + guards
        val reader = GdArrayReader(rows)
        if (rows.size != length + 1 || reader.declaredSize != length || reader.actualCount != length ||
            (0 until length).any { reader.getOrNull(it) == null } ||
            owner?.sizesMatch((0 until guards).map { reader.getOrNull(count * width + it)!!.value }) == false) return null
        return (0 until count).map { index ->
            val base = index * width + (width - WIDTH)
            val text = reader.getOrNull(base)!!
            val valid = reader.getOrNull(base + 1)!!
            val script = reader.getOrNull(base + 2)!!
            val global = reader.getOrNull(base + 3)!!
            val builtin = reader.getOrNull(base + 4)!!
            if (text.type != "String" || valid.type != "bool" || valid.value !in setOf("true", "false") ||
                script.type != "String" || global.type !in setOf("StringName", "String") ||
                builtin.type !in setOf("StringName", "String")) return null
            if (valid.value == "false") null else {
                val display = text.value
                val source = script.value
                val globalName = global.value
                val builtIn = builtin.value.takeIf { it.isNotEmpty() } ?: return null
                val path = if (source != "<null>") Regex("\\((.*)\\):<[^<>]*Script#").find(source)?.groupValues?.get(1) else null
                // Embedded scripts use the containing resource's file name before the :: suffix.
                val file = path?.substringBefore("::")?.substringAfterLast('/')?.takeIf { it.isNotEmpty() }
                GdObjectPresentation(if (source != "<null>" && globalName.isNotEmpty()) globalName else file ?: builtIn, display)
            }
        }
    }

    suspend fun DapSessionContext.load(
        evaluator: GdSelfCheckedFrame, values: List<String>, owner: GdOwnerExpression? = null
    ): List<GdObjectPresentation?>? {
        if (values.isEmpty()) return emptyList()
        try {
            val outcome = evaluateStructuredOutcome(evaluator, GdDapExpressions.probe(values, owner))
            if (outcome !is GdEvalOutcome.Structured) {
                if (outcome !is GdEvalOutcome.Failed || outcome.kind != GdEvalOutcome.Failed.Kind.RESPONSE_MISMATCH)
                    LOG.warn("Object text evaluation failed: $outcome")
                return null
            }
            val rows = when (val load = loadVariablesBounded(outcome.variable)) {
                is GdVariablesLoad.Success -> load.variables
                is GdVariablesLoad.Failed -> {
                    LOG.warn("Object text evaluation failed: ${load.message}")
                    return null
                }
            }
            val presentations = read(rows, values.size, owner)
            if (presentations == null) LOG.warn("Object text or dictionary size changed")
            return presentations
        }
        catch (e: Exception) {
            if (e !is RequestCancelledByPeerException) rethrowControlFlowException(e)
            LOG.warn("Object text evaluation failed", e)
            return null
        }
    }
}
