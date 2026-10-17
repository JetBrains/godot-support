package gdscript.dap.remote

import com.intellij.openapi.diagnostic.logger
import com.intellij.platform.dap.DapEvaluationResult
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapStructuredVariable
import com.intellij.platform.dap.DapVariable
import gdscript.GdScriptBundle
import java.util.concurrent.atomic.AtomicLong

/**
 * Separates a structured answer, a missing value, and a failed request.
 * [evaluateStructuredOutcome] validates the response before it selects an outcome.
 */
internal sealed class GdEvalOutcome {
    /** A structured variable that can be expanded via `variablesReference`. */
    data class Structured(val variable: DapStructuredVariable) : GdEvalOutcome()

    /** An evaluation with no structured answer, including a failed Godot expression. */
    data object Missing : GdEvalOutcome()

    /** A DAP error or an already-logged numbered response mismatch. */
    data class Failed(val message: String, val kind: Kind = Kind.DAP_ERROR) : GdEvalOutcome() {
        enum class Kind { DAP_ERROR, RESPONSE_MISMATCH }
    }
}

private val LOG = logger<GdEvalOutcome>()

private fun responseMismatch(): GdEvalOutcome.Failed {
    LOG.warn("Evaluation request number or result shape changed")
    return GdEvalOutcome.Failed(
        GdScriptBundle.message("gdscript.debugger.error.unknown"),
        GdEvalOutcome.Failed.Kind.RESPONSE_MISMATCH
    )
}

/** The validated value or the reason that the numbered array is invalid. */
internal sealed interface GdNumberedAnswer {
    data class Value(val variable: DapVariable) : GdNumberedAnswer
    enum class Failure : GdNumberedAnswer { COUNT, INDICES, NUMBER }
}

/** Checks one integer size marker and all canonical indices in response order. */
internal fun canonicalArrayFailure(rows: List<DapVariable>, count: Int): GdNumberedAnswer.Failure? {
    val size = rows.singleOrNull { it.name == "size" }
    if (size?.type != "int" || size.value != count.toString()) return GdNumberedAnswer.Failure.COUNT
    val elements = rows.filter { it.name != "size" }
    if (elements.size != count || elements.withIndex().any { (index, row) -> row.name != index.toString() })
        return GdNumberedAnswer.Failure.INDICES
    return null
}

internal object GdEvaluationRequest {
    private val nextNumber = AtomicLong()

    fun answer(rows: List<DapVariable>, number: Long): GdNumberedAnswer {
        canonicalArrayFailure(rows, 2)?.let { return it }
        val elements = rows.filter { it.name != "size" }
        if (elements[1].type != "int" || elements[1].value != number.toString()) return GdNumberedAnswer.Failure.NUMBER
        return GdNumberedAnswer.Value(elements[0])
    }

    fun number(): Long = nextNumber.incrementAndGet()
}

/**
 * Evaluates `[value, number]` and accepts only the matching numbered answer.
 * Godot caches answers by expression text, not by DAP request ID.
 * A unique number makes each request text distinct and detects a stale answer.
 * See `debug_adapter_parser.cpp`, `DebugAdapterParser::req_evaluate`.
 *
 * A failed Godot Expression returns null instead of the outer array.
 * A valid null value stays inside the array. Neither result supplies a structured value to the caller.
 */
internal suspend fun DapSessionContext.evaluateStructuredOutcome(
    evaluator: GdSelfCheckedFrame,
    expression: String
): GdEvalOutcome {
    val number = GdEvaluationRequest.number()
    return when (val result = evaluator.run { evaluate(GdDapExpressions.numbered(expression, number)) }) {
        is DapEvaluationResult.Error -> GdEvalOutcome.Failed(result.errorMessage)
        is DapEvaluationResult.Success -> {
            val outer = result.variable as? DapStructuredVariable ?: return GdEvalOutcome.Missing
            val rows = when (val load = loadVariablesBounded(outer)) {
                is GdVariablesLoad.Success -> load.variables
                is GdVariablesLoad.Failed -> return GdEvalOutcome.Failed(load.message)
            }
            when (val answer = GdEvaluationRequest.answer(rows, number)) {
                is GdNumberedAnswer.Failure -> responseMismatch()
                is GdNumberedAnswer.Value -> {
                    val inner = answer.variable
                    if (inner is DapStructuredVariable) GdEvalOutcome.Structured(inner) else GdEvalOutcome.Missing
                }
            }
        }
    }
}
