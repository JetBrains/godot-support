package com.jetbrains.godot.gdscript.dap

import com.intellij.platform.dap.DapEvaluationResult
import com.intellij.platform.dap.DapExpressionEvaluator
import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapStructuredVariable
import com.intellij.platform.dap.DapVariable
import com.intellij.platform.dap.VariableBatch
import com.intellij.platform.dap.VariableFilter
import com.intellij.testFramework.common.timeoutRunBlocking
import com.jetbrains.dap.protocol.VariableAttribute
import gdscript.dap.remote.GdObjectMemberRecovery
import gdscript.dap.remote.GdObjectMemberRecovery.recoveryCandidates
import gdscript.dap.remote.GdObjectPresentation
import gdscript.dap.remote.GdObjectProbe
import gdscript.dap.remote.GdOwnerExpression
import gdscript.dap.remote.GdPresentedVariable
import gdscript.dap.remote.GdValueKind
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class GdObjectMemberRecoveryTest {
    @Test
    fun testMemberOwnerExpressions() {
        val owner = GdOwnerExpression("self")
        assertAll(
            { assertEquals("(self).get(\"a\\\"b\")", owner.member("a\"b").text) },
            { assertEquals("(self).get(\"a\\nb\")", owner.member("a\nb").text) },
        )
    }

    @Test
    fun testArrayOwnerExpressions() {
        val owner = GdOwnerExpression("self")
        assertAll(
            { assertEquals("(self)[2]", owner.child("Array", "2", 3)?.text) },
            { assertNull(owner.child("Array", "size", 0)) },
        )
    }

    @Test
    fun testCandidateMappingAndFiltering() {
        val rows = listOf(plain("base.gd/child", "int", "123"), plain("actual", "int", "4"),
                          plain("box", "Object", "obj"), plain("metadata/target", "int", "43"),
                          plain("base.gd/nested/child", "int", "44"))
        assertAll(
            { assertEquals(listOf(0 to "child", 1 to "actual", 3 to "metadata/target", 4 to "nested/child"),
                           recoveryCandidates("Members", rows)) },
            { assertEquals(listOf(0 to "base.gd/child", 1 to "actual", 3 to "metadata/target", 4 to "base.gd/nested/child"),
                           recoveryCandidates("Node", rows)) },
            { assertEquals(listOf(0 to "metadata/target", 1 to "theme_override_colors/accent"),
                           recoveryCandidates("Theme", listOf(plain("metadata/target", "int", "42"),
                                                             plain("theme_override_colors/accent", "int", "43")))) },
        )
    }

    @Test
    fun testConstantsAndAmbiguousMembersAreNotCandidates() {
        val rows = listOf(plain("base.gd/child", "int", "123"), plain("actual", "int", "4"))
        assertAll(
            { assertTrue(recoveryCandidates("Constants", rows).isEmpty()) },
            { assertTrue(recoveryCandidates("Members", listOf(plain("a.gd/x", "int", "1"), plain("x", "int", "2"))).isEmpty()) },
        )
    }

    @Test
    fun testNodeCategoryRetainsOrdinaryIntProperties() {
        val rows = listOf(plain("multiplayer_authority", "int", "1"), plain("custom_object", "int", "42"),
                          plain("path", "NodePath", "/root"))
        assertEquals(listOf(1 to "custom_object"), recoveryCandidates("Node", rows))
    }

    @Test
    fun testDictionaryEntriesHaveNoSyntheticSizeRow() {
        val owner = GdOwnerExpression("d")
        val elements = listOf("1", "1").mapIndexed { index, label -> owner.child("Dictionary", label, index, 2)!! }
        assertAll(
            { assertEquals(listOf("(d).values()[0]", "(d).values()[1]"), elements.map { it.text }) },
            { assertEquals("[(d).values()[0], (d).size()]", elements.first().withSizes(listOf(elements.first().text))) },
            { assertTrue(elements.all { it.sizesMatch(listOf("2")) }) },
            { assertTrue(elements.none { it.sizesMatch(listOf("1")) }) },
        )
    }

    @Test
    fun testTopLevelKindKeepsObjectContainersAndCollidingDictionaryKeysDistinct() {
        val array = structured("result", null, "[<EncodedObjectAsID#12>, <EncodedObjectAsID#13>]")
        val dictionary = structured("result", null, "{ 1: <EncodedObjectAsID#12>, \"1\": <EncodedObjectAsID#13> }")
        assertAll(
            { assertEquals("Array", GdValueKind.type(array)) },
            { assertEquals("Dictionary", GdValueKind.type(dictionary)) },
            { assertEquals("Object", GdValueKind.type(plain("result", null, "<EncodedObjectAsID#-12>"))) },
            { assertNull(GdValueKind.type(plain("result", null, "prefix <EncodedObjectAsID#12>"))) },
        )
    }

    @Test
    fun testProbeTargetRespectsDeclaredTypeAtLocalsAndChildren() {
        val owner = GdOwnerExpression("self.player")
        val declared = structured("player", "Object")
        val inferred = structured("player", null, "<EncodedObjectAsID#12>")
        assertAll(
            { assertTrue(GdObjectProbe.isProbeTarget(declared, owner)) },
            { assertTrue(!GdObjectProbe.isProbeTarget(inferred, owner)) },
            { assertTrue(GdObjectProbe.isProbeTarget(inferred, owner, allowInferredType = true)) },
            { assertTrue(!GdObjectProbe.isProbeTarget(declared, null)) },
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["Array", "Dictionary"])
    fun testSuccessfulCategoryRecoveryPreservesRowsAndChildData(containerType: String): Unit = timeoutRunBlocking {
        val objectRow = fake("base.gd/player", "int", "42", "self.player")
        val rows = listOf(plain("label", "String", "title"), objectRow, plain("health", "int", "100"), plain("container", "int", "43"))
        val child = plain("name", "String", "Player")
        val recovered = structured("0", "Object", children = listOf(child))
        val fields = recoveryRecord(recovered, "Player:<Node#42>", "true", "<null>", "", "Node") +
            recoveryRecord(plain("6", "int", "100"), "100", "false", "<null>", "", "int") +
            recoveryRecord(structured("12", containerType), "container", "true", "<null>", "", containerType) + plain("18", "int", "1")
        val evaluator = object : DapExpressionEvaluator {
            override suspend fun DapSessionContext.evaluate(expression: String): DapEvaluationResult =
                numberedSuccessAnswer(expression, structured("0", "Array", children = listOf(plain("size", "int", "19")) + fields))
        }
        val context = unusedSessionContext()
        val owner = GdOwnerExpression("(inventory).values()[0]", listOf("inventory" to 1))
        val result = with(GdObjectMemberRecovery) {
            with(context) { recoverCategory(evaluationFrame { evaluator.run { evaluate(it) } }, owner, "Members", rows) }
        }
        assertEquals(rows.map { it.name }, result.map { it.name })
        for (index in listOf(0, 2, 3)) assertSame(rows[index], result[index])
        assertEquals("self.player", result[1].evaluateName)
        assertEquals(GdObjectPresentation("Node", "Player:<Node#42>"), (result[1] as GdPresentedVariable).objectPresentation)
        val batches = with(context) { (result[1] as DapStructuredVariable).run { loadVariables(null, 0).toList() } }
        assertEquals(listOf(child), batches.flatMap { it.variables })
    }

    @Test
    fun testFailedEvaluateKeepsOriginalRows(): Unit = timeoutRunBlocking {
        val row = plain("member", "int", "42")
        val evaluator = object : DapExpressionEvaluator {
            override suspend fun DapSessionContext.evaluate(expression: String): DapEvaluationResult =
                DapEvaluationResult.Error("expression failed")
        }
        val result = with(GdObjectMemberRecovery) {
            with(unusedSessionContext()) {
                recoverCategory(evaluationFrame { evaluator.run { evaluate(it) } }, GdOwnerExpression("self"), "Members", listOf(row))
            }
        }
        assertSame(row, result.singleOrNull())
    }

    @Test
    fun testMalformedResultKeepsOriginalRows(): Unit = timeoutRunBlocking {
        val row = plain("member", "int", "42")
        val record = recoveryRecord(structured("0", "Object"), "Node", "true", "<null>", "", "Node")
        val answers = listOf(
            listOf(plain("size", "int", "0")),
            listOf(plain("size", "int", "0")) + record,
            listOf(plain("size", "int", "6")) + recoveryRecord(structured("1", "Object"), "Node", "true", "<null>", "", "Node"),
            listOf(plain("size", "int", "6")),
        )
        for (fields in answers) {
            val evaluator = object : DapExpressionEvaluator {
                override suspend fun DapSessionContext.evaluate(expression: String): DapEvaluationResult =
                    numberedSuccessAnswer(expression, structured("0", "Array", children = fields))
            }
            val result = with(GdObjectMemberRecovery) {
                with(unusedSessionContext()) {
                    recoverCategory(evaluationFrame { evaluator.run { evaluate(it) } }, GdOwnerExpression("self"), "Members", listOf(row))
                }
            }
            assertSame(row, result.singleOrNull())
        }
    }

    // Each record contains the recovered value, then text, valid, script, global name, built-in class.
    // Dictionary sizes follow all records in the full answer.
    private fun recoveryRecord(
        value: DapVariable, text: String, valid: String, script: String, globalName: String, builtInClass: String
    ): List<DapVariable> = listOf(value) + listOf(text, valid, script, globalName, builtInClass)
        .mapIndexed { index, field -> plain((value.name.toInt() + index + 1).toString(), if (index == 1) "bool" else "String", field) }

    private fun plain(name: String, type: String?, value: String): DapVariable = fake(name, type, value)
    private fun structured(
        name: String, type: String?, value: String = "value", children: List<DapVariable> = emptyList()
    ): DapStructuredVariable = object : DapStructuredVariable, DapVariable by fake(name, type, value) {
        override val namedVariables: Int = if (type == "Array") 0 else children.size
        override val indexedVariables: Int = if (type == "Array") children.size else 0
        override fun DapSessionContext.loadVariables(filter: VariableFilter?, batchSize: Int): Flow<VariableBatch> =
            if (children.isEmpty()) emptyFlow() else flowOf(VariableBatch(children, 0))
    }
    private fun fake(nameArg: String, typeArg: String?, vArg: String, expression: String? = null): DapVariable = object : DapVariable {
        override val name = nameArg
        override val type = typeArg
        override val value = vArg
        override val evaluateName: String? = expression
        override val kind: com.intellij.platform.dap.ValueKind? = null
        override val attributes: List<VariableAttribute>? = null
    }
}
