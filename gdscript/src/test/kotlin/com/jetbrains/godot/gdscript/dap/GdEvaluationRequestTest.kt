package com.jetbrains.godot.gdscript.dap

import com.intellij.platform.dap.DapEvaluationResult
import com.intellij.platform.dap.DapVariable
import com.intellij.testFramework.common.timeoutRunBlocking
import gdscript.dap.remote.GdEvalOutcome
import gdscript.dap.remote.GdEvaluationRequest
import gdscript.dap.remote.GdNumberedAnswer
import gdscript.dap.remote.evaluateStructuredOutcome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class GdEvaluationRequestTest {
    @Test
    fun testValidAnswersKeepStructuredAndScalarValues() {
        for (inner in listOf(structuredVariable("0", "Array", emptyList()), variable("0", "Nil", "<null>"))) {
            val rows = listOf(variable("size", "int", "2"), inner, variable("1", "int", "7"))
            assertEquals(GdNumberedAnswer.Value(inner), GdEvaluationRequest.answer(rows, 7))
        }
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidAnswers")
    internal fun testInvalidNumberedLayout(
        name: String, rows: List<DapVariable>, failure: GdNumberedAnswer.Failure,
    ): Unit = timeoutRunBlocking {
        assertEquals(failure, GdEvaluationRequest.answer(rows, 7), name)
        val frame = evaluationFrame { expression ->
            val number = expression.substringAfterLast(", ").removeSuffix("]")
            val answer = rows.map { if (it.value == "7") variable(it.name, it.type!!, number) else it }
            DapEvaluationResult.Success(structuredVariable("result", "Array", answer))
        }
        val outcome = with(unusedSessionContext()) { evaluateStructuredOutcome(frame, "items") }
        assertEquals(GdEvalOutcome.Failed.Kind.RESPONSE_MISMATCH, (outcome as GdEvalOutcome.Failed).kind)
    }

    companion object {
        @JvmStatic
        fun invalidAnswers(): List<Arguments> {
            val inner = structuredVariable("0", "Array", emptyList())
            val size = variable("size", "int", "2")
            val number = variable("1", "int", "7")
            return listOf(
                Arguments.of("size type", listOf(variable("size", "String", "2"), inner, number), GdNumberedAnswer.Failure.COUNT),
                Arguments.of("size value", listOf(variable("size", "int", "02"), inner, number), GdNumberedAnswer.Failure.COUNT),
                Arguments.of("duplicate size", listOf(size, inner, number, size), GdNumberedAnswer.Failure.COUNT),
                Arguments.of("missing size", listOf(inner, number), GdNumberedAnswer.Failure.COUNT),
                Arguments.of("number type", listOf(size, inner, variable("1", "String", "7")), GdNumberedAnswer.Failure.NUMBER),
                Arguments.of("number value", listOf(size, inner, variable("1", "int", "-1")), GdNumberedAnswer.Failure.NUMBER),
                Arguments.of("duplicate index", listOf(size, inner, number, number), GdNumberedAnswer.Failure.INDICES),
                Arguments.of("missing index", listOf(size, inner), GdNumberedAnswer.Failure.INDICES),
                Arguments.of("noncanonical index", listOf(size, inner, variable("01", "int", "7")), GdNumberedAnswer.Failure.INDICES),
                Arguments.of("reordered indices", listOf(size, number, inner), GdNumberedAnswer.Failure.INDICES),
                Arguments.of("extra row", listOf(size, inner, number, variable("extra", "int", "0")), GdNumberedAnswer.Failure.INDICES),
            )
        }
    }
}
