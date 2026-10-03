package com.jetbrains.godot.gdscript.dap

import com.intellij.platform.dap.DapEvaluationResult
import com.intellij.platform.dap.DapExpressionEvaluator
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapStructuredVariable
import com.intellij.platform.dap.DapVariable
import com.intellij.platform.dap.ValueKind
import com.intellij.platform.dap.VariableBatch
import com.intellij.platform.dap.VariableFilter
import com.intellij.testFramework.common.timeoutRunBlocking
import com.jetbrains.dap.protocol.VariableAttribute
import gdscript.dap.remote.GdEvalOutcome
import gdscript.dap.remote.GdObjectPresentation
import gdscript.dap.remote.GdObjectProbe
import gdscript.dap.remote.GdLocalExpressions
import gdscript.dap.remote.GdOwnerExpression
import gdscript.dap.remote.GdDapExpressions
import gdscript.dap.remote.evaluateStructuredOutcome
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertNotNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Test

class GdObjectProbeTest {
    @Test
    fun testUtilityNamesCannotBeUsedAsLocalExpressions() {
        assertAll(
            { assertNull(GdLocalExpressions.name("str")) },
            { assertNull(GdLocalExpressions.name("is_instance_valid")) },
            { assertNull(GdLocalExpressions.name("bad-name")) },
            { assertNotNull(GdLocalExpressions.name("player")) },
        )
    }

    @Test
    fun testRequestBuildersAndDictionaryGuards() {
        val local = GdDapExpressions.probe(listOf("player"))
        val owner = GdOwnerExpression("player", listOf("inventory" to 2))
        assertAll(
            { assertEquals("[str(player), is_instance_valid(player), " +
                "str([self, player][int(is_instance_valid(player))].get_script()), " +
                "[self.get_script(), [self, player][int(is_instance_valid(player))].get_script()]" +
                "[int([self, player][int(is_instance_valid(player))].get_script() != null)].get_global_name(), " +
                "[self, player][int(is_instance_valid(player))].get_class()]", local) },
            { assertEquals(local.dropLast(1) + ", (inventory).size()]", GdDapExpressions.probe(listOf("player"), owner)) },
            { assertEquals("[[1, 2], 91]", GdDapExpressions.numbered("[1, 2]", 91)) },
        )
    }

    @Test
    fun testNumberedOutcomeHandlesNullAndChecksStructuredAnswers(): Unit = timeoutRunBlocking {
        val inner = structured("0", "Array", emptyList())
        val scope = unusedSessionContext()
        val evaluator = object : DapExpressionEvaluator {
            var answer: (Long) -> DapEvaluationResult = { DapEvaluationResult.Success(v("answer", "Nil", "<null>")) }
            override suspend fun DapSessionContext.evaluate(expression: String): DapEvaluationResult {
                return answer(expression.substringAfterLast(", ").removeSuffix("]").toLong())
            }
        }
        suspend fun evaluate(): GdEvalOutcome = with(scope) {
            evaluateStructuredOutcome(evaluationFrame { evaluator.run { evaluate(it) } }, "[1, 2]")
        }

        assertEquals(GdEvalOutcome.Missing, evaluate())
        evaluator.answer = { number -> DapEvaluationResult.Success(structured("outer", "Array",
            listOf(v("size", "int", "2"), inner, v("1", "int", number.toString())))) }
        assertEquals(inner, (evaluate() as GdEvalOutcome.Structured).variable)
        var earlierNumber = 0L
        evaluator.answer = { number ->
            earlierNumber = number
            DapEvaluationResult.Success(structured("outer", "Array",
                listOf(v("size", "int", "2"), v("0", "Nil", "<null>"), v("1", "int", number.toString()))))
        }
        assertEquals(GdEvalOutcome.Missing, evaluate())
        for (shape in listOf("stale", "size", "index")) {
            evaluator.answer = { number -> DapEvaluationResult.Success(structured("outer", "Array",
                listOf(v("size", "int", if (shape == "size") "1" else "2"), inner,
                       v(if (shape == "index") "2" else "1", "int", if (shape == "stale") earlierNumber.toString() else number.toString())))) }
            assertEquals(GdEvalOutcome.Failed.Kind.RESPONSE_MISMATCH, (evaluate() as GdEvalOutcome.Failed).kind)
        }
        evaluator.answer = { DapEvaluationResult.Error("transport error") }
        assertEquals(GdEvalOutcome.Failed("transport error"), evaluate())
    }

    @Test
    fun testLoadCollectsSplitBatchesAndPreservesProbeOrder(): Unit = timeoutRunBlocking {
        val owner = GdOwnerExpression("items", listOf("items" to 2))
        val valid = fields("Player:<Node#42>", "<null>", "", "Node")
        val freed = fields("<null>", "<null>", "", "Object").toMutableList()
        freed[1] = v("1", "bool", "false")
        val rows = result((valid + freed).mapIndexed { index, field -> v(index.toString(), field.type!!, field.value) } +
            v("10", "int", "2"))
        val evaluator = object : DapExpressionEvaluator {
            override suspend fun DapSessionContext.evaluate(expression: String): DapEvaluationResult {
                val inner = structured("0", "Array", rows, splitAt = 4)
                return numberedSuccessAnswer(expression, inner, splitAt = 2)
            }
        }
        val context = unusedSessionContext()
        val answer = with(GdObjectProbe) {
            with(context) {
                load(evaluationFrame { evaluator.run { evaluate(it) } }, listOf("(items).values()[0]", "(items).values()[1]"), owner)
            }
        }
        assertEquals(listOf(GdObjectPresentation("Node", "Player:<Node#42>"), null), answer)
        val unusedEvaluator = object : DapExpressionEvaluator {
            override suspend fun DapSessionContext.evaluate(expression: String): DapEvaluationResult = error("The empty probe must not evaluate")
        }
        assertEquals(emptyList<GdObjectPresentation>(), with(GdObjectProbe) {
            with(context) { load(evaluationFrame { unusedEvaluator.run { evaluate(it) } }, emptyList()) }
        })
    }

    @Test
    fun testReaderUsesClassThenFileThenBuiltin() {
        val global = result(fields("<Object#1>", "(res://scripts/player.gd):<GDScript#1>", "Hero", "Node"))
        val file = result(fields("<Object#2>", "(res://scripts/player.gd):<GDScript#1>", "", "Node"))
        val builtin = result(fields("<Object#3>", "<null>", "Stale", "Node"))
        assertAll(
            { assertEquals("Hero", GdObjectProbe.read(global, 1)?.single()?.className) },
            { assertEquals("player.gd", GdObjectProbe.read(file, 1)?.single()?.className) },
            { assertEquals("Node", GdObjectProbe.read(builtin, 1)?.single()?.className) },
            { assertEquals("<Object#1>", GdObjectProbe.read(global, 1)?.single()?.text) },
        )
        val files = listOf(
            "(res://scripts/player.cs):<CSharpScript#12>" to "player.cs",
            "(res://scenes/hero.tscn::GDScript_1):<GDScript#12>" to "hero.tscn",
            "(res://player(alt).gd):<GDScript#12>" to "player(alt).gd",
            "(res://scenes/hero(alt).tscn::GDScript_1):<GDScript#12>" to "hero(alt).tscn",
        )
        for ((script, expected) in files) {
            assertEquals(expected, GdObjectProbe.read(result(fields("<Object#1>", script, "", "Node")), 1)?.single()?.className)
        }
    }

    @Test
    fun testLiteralQuotesAndEscapesDoNotInvalidateObjectProbeBatch() {
        val rows = (fields("\"unterminated", "<null>", "", "Node") +
            fields("line\\n\"", "<null>", "", "Node")).mapIndexed { index, field ->
            v(index.toString(), field.type!!, field.value)
        }
        val texts = GdObjectProbe.read(result(rows), 2)
        assertAll(
            { assertEquals(listOf("\"unterminated", "line\\n\""), texts?.map { it?.text }) },
            { assertEquals(listOf("Node", "Node"), texts?.map { it?.className }) },
        )
    }

    @Test
    fun testInvalidValueIgnoresFallbackFields() {
        val invalid = result(listOf(v("0", "String", "<null>"), v("1", "bool", "false"),
            v("2", "String", "(res://scripts/player.gd):<GDScript#1>"),
            v("3", "StringName", ""), v("4", "String", "Object")))
        assertEquals(listOf(null), GdObjectProbe.read(invalid, 1))
    }

    @Test
    fun testNestedDictionaryGuardsAndRecoveryStride() {
        val owner = GdOwnerExpression("(outer).values()[1]", listOf("outer" to 3, "inner" to 2))
        val fieldRows = fields("<Object#4>", "<null>", "", "Node")
        val contents = listOf(structured("0", "Object", emptyList())) + fieldRows.mapIndexed { i, field ->
            v((i + 1).toString(), field.type!!, field.value)
        } + listOf(v("6", "int", "3"), v("7", "int", "2"))
        assertAll(
            { assertEquals("Node", GdObjectProbe.read(result(contents), 1, owner, width = 6)?.single()?.className) },
            { assertNull(GdObjectProbe.read(result(contents.dropLast(1) + v("7", "int", "1")), 1, owner, width = 6)) },
        )
    }

    @Test
    fun testShapeAndDictionarySizeAreRequired() {
        val owner = GdOwnerExpression("(d).values()[0]", listOf("d" to 2))
        val data = fields("<Object#1>", "<null>", "", "Node")
        assertAll(
            { assertNull(GdObjectProbe.read(result(data + v("5", "int", "1")), 1, owner)) },
            { assertEquals("Node", GdObjectProbe.read(result(data + v("5", "int", "2")), 1, owner)?.single()?.className) },
            { assertNull(GdObjectProbe.read(result(data).dropLast(1), 1)) },
            { assertNull(GdObjectProbe.read(result(data).dropLast(1) + v("4", "int", "42"), 1)) },
        )
    }

    private fun fields(text: String, script: String, global: String, builtIn: String): List<DapVariable> =
        listOf(v("0", "String", text), v("1", "bool", "true"), v("2", "String", script),
            v("3", "StringName", global), v("4", "StringName", builtIn))

    private fun result(fields: List<DapVariable>): List<DapVariable> =
        listOf(v("size", "int", fields.size.toString())) + fields

    private fun structured(name: String, type: String, children: List<DapVariable>, splitAt: Int = children.size): DapStructuredVariable =
        object : DapStructuredVariable, DapVariable by v(name, type, "value") {
            override val namedVariables = 0
            override val indexedVariables = children.size
            override fun DapSessionContext.loadVariables(filter: VariableFilter?, batchSize: Int): Flow<VariableBatch> =
                if (splitAt == children.size) flowOf(VariableBatch(children, 0))
                else flowOf(VariableBatch(children.take(splitAt), children.size - splitAt), VariableBatch(children.drop(splitAt), 0))
        }

    private fun v(nameArg: String, typeArg: String, valueArg: String): DapVariable = object : DapVariable {
        override val name = nameArg
        override val type = typeArg
        override val value = valueArg
        override val evaluateName: String? = null
        override val kind: ValueKind? = null
        override val attributes: List<VariableAttribute>? = null
    }
}
