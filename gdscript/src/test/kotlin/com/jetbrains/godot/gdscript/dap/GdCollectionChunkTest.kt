package com.jetbrains.godot.gdscript.dap

import com.intellij.platform.dap.DapSessionContext
import com.intellij.platform.dap.DapStructuredVariable
import com.intellij.platform.dap.DapVariable
import com.intellij.platform.dap.VariableBatch
import com.intellij.platform.dap.VariableFilter
import com.intellij.platform.dap.xdebugger.DefaultDapXDebuggerPresentationFactory
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.xdebugger.frame.XNamedValue
import com.intellij.xdebugger.frame.presentation.XValuePresentation
import gdscript.dap.remote.GdDapXValue
import gdscript.dap.remote.GdFrameSourceSelf
import gdscript.dap.remote.GdOwnerExpression
import gdscript.dap.remote.GdSelfCheckedFrame
import gdscript.dap.remote.GdWatchContext
import gdscript.dap.remote.GdWatchPath
import gdscript.dap.remote.GdValueKind
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import java.lang.reflect.Proxy
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import java.util.concurrent.TimeUnit

@TestApplication
class GdCollectionChunkTest {
    @ParameterizedTest
    @ValueSource(ints = [0, 100, 101])
    fun testDirectRowsAndAllPackedTypes(count: Int): Unit = withExecutor { executor ->
        for (type in GdValueKind.arrayTypes) {
            val rows = listOf(variable("size", "int", count.toString())) + (0 until count).map { variable("$it", "int") }
            var loads = 0
            val parent = counted(type, rows) { loads++ }
            val frame = TestFrame { error("A known array path must not evaluate") }
            val root = GdDapXValue(FACTORY, executor, parent, owner = GdOwnerExpression("items"), frame = null,
                watchContext = GdWatchContext(GdWatchPath.evaluated("items"), frame, executor))
            val node = expand(root, executor)
            assertEquals(1, loads)
            assertEquals(listOf("size"), node.values.map { it.name }.filter { it == "size" })
            if (count <= 100) {
                assertTrue(node.batches.flatMap { it.first.topGroups }.isEmpty())
                assertEquals(count + 1, node.values.size)
            }
            else {
                assertEquals(listOf("[0..99]", "[100..100]"), groups(node).map { it.name })
                val last = expandGroup(node, 1, executor)
                assertEquals(listOf("100"), last.values.map { it.name })
                assertEquals("(items)[100]", watch(last.values.single()))
                assertEquals(1, loads)
            }
            assertEquals(1, node.terminalCount)
        }
    }

    @Test
    fun testUntypedArrayUsesFullResponseForTypeInference(): Unit = withExecutor { executor ->
        val rows = listOf(variable("size", "int", "101")) + (0..100).map { variable("$it", "int") }
        val parent = object : DapStructuredVariable by counted("Array", rows, {}) {
            override val type: String? = null
            override val value = "[${(0..100).joinToString { "0" }}]"
        }
        val frame = TestFrame { error("A known path must not evaluate") }
        val root = GdDapXValue(FACTORY, executor, parent, owner = null, frame = null,
            watchContext = GdWatchContext(GdWatchPath.evaluated("items"), frame, executor))
        val top = expand(root, executor)
        assertEquals(listOf("[0..99]", "[100..100]"), groups(top).map { it.name })
        assertEquals("(items)[100]", watch(expandGroup(top, 1, executor).values.single()))
    }

    @Test
    fun testUntypedArrayProbesObjectValuesInRowOrder(): Unit = withExecutor { executor ->
        for (type in listOf(null, "Nil")) {
            val rows = listOf(variable("size", "int", "3"), variable("0", "int"),
                structuredVariable("1", "Object", emptyList()), structuredVariable("2", "Object", emptyList()))
            val requests = mutableListOf<String>()
            val frame = GdSelfCheckedFrame(TestFrame { expression ->
                requests.add(expression)
                assertTrue("(items)[1]" in expression)
                assertTrue(expression.indexOf("(items)[1]") < expression.indexOf("(items)[2]"))
                assertTrue("(items)[0]" !in expression)
                numberedAnswer(expression, probeFields(listOf("First", "Second")))
            }, { GdFrameSourceSelf.Present })
            val root = GdDapXValue(FACTORY, executor, untyped(type, "[0, <EncodedObjectAsID#1>, <EncodedObjectAsID#2>]", rows),
                owner = GdOwnerExpression("items"), frame = frame)
            val node = expand(root, executor)
            assertEquals(listOf("size", "0", "1", "2"), node.values.map { it.name })
            executor.runLast()
            assertEquals(1, requests.size)
            assertEquals(listOf("First", "Second"), node.values.drop(2).map {
                val row = it as GdDapXValue
                presentationText(row.createValuePresentation(row.variable, true, false), "renderComment").trim()
            })
        }
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 100, 101])
    fun testDictionarySizeKeyAndDirectRows(count: Int): Unit = withExecutor { executor ->
        val rows = (0 until count).map { variable(if (it == 0) "size" else "key$it", "int") }
        val root = GdDapXValue(FACTORY, executor, counted("Dictionary", rows) {}, owner = null, frame = null)
        val node = expand(root, executor)
        assertEquals(if (count == 101) listOf("[0..99]", "[100..100]") else emptyList(), groups(node).map { it.name })
        if (count <= 100) assertEquals(rows.map { it.name }, node.values.map { it.name })
        else assertEquals("key100", expandGroup(node, 1, executor).values.single().name)
        assertEquals("size = $count", presentationText(root.createValuePresentation(root.variable, true, false)))
    }

    @Test
    fun testPackedOwnersRemainUnsupported() {
        for (type in GdValueKind.arrayTypes - "Array") {
            assertNull(GdOwnerExpression("items").child(type, "100", 100))
        }
        assertEquals("(items)[100]", GdOwnerExpression("items").child("Array", "100", 101)?.text)
    }

    @Test
    fun testNestedRangesKeepAbsoluteArrayIndices(): Unit = withExecutor { executor ->
        val parent = counted("Array", listOf(variable("size", "int", "10001")) +
            (0..10_000).map { variable("$it", "int") }) {}
        val root = GdDapXValue(FACTORY, executor, parent, owner = GdOwnerExpression("items"), frame = null,
            watchContext = GdWatchContext(GdWatchPath.evaluated("items"), TestFrame { error("Unexpected evaluation") }, executor))
        val top = expand(root, executor)
        assertEquals(listOf("[0..9999]", "[10000..10000]"), groups(top).map { it.name })
        val first = expandGroup(top, 0, executor)
        assertEquals(100, groups(first).size)
        val boundary = expandGroup(first, 1, executor)
        assertEquals(listOf("100", "199"), listOf(boundary.values.first().name, boundary.values.last().name))
        assertEquals(listOf("(items)[100]", "(items)[199]"),
            listOf(watch(boundary.values.first()), watch(boundary.values.last())))
        val tail = expandGroup(top, 1, executor)
        assertEquals("10000", tail.values.single().name)
        assertEquals("(items)[10000]", watch(tail.values.single()))
    }

    @Test
    fun testDictionaryBoundaryKeepsPositionsLabelsAndProbeOrder(): Unit = withExecutor { executor ->
        val labels = (0..101).map { if (it == 0 || it == 100) "same" else "key$it" }
        val entries = labels.map { structuredVariable(it, "Object", emptyList()) }
        var loads = 0
        val parent = counted("Dictionary", entries) { loads++ }
        val requests = mutableListOf<String>()
        val frame = GdSelfCheckedFrame(TestFrame { expression ->
            requests.add(expression)
            if (".keys()" in expression) {
                val keyRows = labels.mapIndexed { index, key -> variable("$index", "String", key) }
                val position = Regex("\\.keys\\(\\)\\[(\\d+)]").find(expression)!!.groupValues[1].toInt()
                val fields = buildList {
                    addAll(keyRows)
                    add(variable("102", "int", "4"))
                    add(variable("103", "String", "00000000"))
                }
                numberedAnswer(expression, fields).also { assertEquals(100, position) }
            }
            else {
                assertTrue("(items).values()[100]" in expression)
                assertTrue("(items).values()[101]" in expression)
                assertTrue("(items).size()" in expression)
                numberedAnswer(expression, (0..1).flatMap { index ->
                    listOf(variable("${index * 5}", "String", "Object$index"), variable("${index * 5 + 1}", "bool", "true"),
                        variable("${index * 5 + 2}", "String", "<null>"), variable("${index * 5 + 3}", "StringName", ""),
                        variable("${index * 5 + 4}", "StringName", "Node"))
                } + variable("10", "int", "102"))
            }
        }, { GdFrameSourceSelf.Present })
        val root = GdDapXValue(FACTORY, executor, parent, owner = GdOwnerExpression("items"), frame = frame,
            watchContext = GdWatchContext(GdWatchPath.evaluated("items"), frame, executor))
        val top = expand(root, executor)
        assertEquals(1, loads)
        assertEquals(listOf("[0..99]", "[100..101]"), groups(top).map { it.name })
        assertTrue(requests.isEmpty())
        val leaf = expandGroup(top, 1, executor)
        assertEquals(listOf("same", "key101"), leaf.values.map { it.name })
        assertTrue(requests.isEmpty())
        executor.runLast()
        assertEquals(1, requests.size)
        assertEquals(1, loads)
        assertEquals("size = 102", presentationText(root.createValuePresentation(parent, true, false)))
        assertEquals(listOf("Node", "Node"), leaf.values.map {
            presentationText((it as GdDapXValue).createValuePresentation(it.variable, true, false))
        })
        assertEquals(listOf("Object0", "Object1"), leaf.values.map {
            presentationText((it as GdDapXValue).createValuePresentation(it.variable, true, false), "renderComment").trim()
        })
        val expression = leaf.values.first().calculateEvaluationExpression()
        executor.runLast()
        assertEquals("(items)[\"same\"]", expression.blockingGet(1, TimeUnit.SECONDS)?.expression)
        assertEquals(2, requests.size)
        assertTrue(".keys()[100]" in requests.last())
        assertTrue(leaf.errors.isEmpty())
    }

    @Test
    fun testUntypedArrayPassesOwnerToNestedProbeAndMemberRecovery(): Unit = withExecutor { executor ->
        val members = structuredVariable("Members", "Category", listOf(variable("child", "int", "42")))
        val rows = listOf(variable("size", "int", "1"), structuredVariable("0", "Object", listOf(members)))
        val requests = mutableListOf<String>()
        val frame = GdSelfCheckedFrame(TestFrame { expression ->
            requests.add(expression)
            if (".get(\"child\")" in expression) {
                assertTrue("((items)[0]).get(\"child\")" in expression)
                numberedAnswer(expression, listOf(structuredVariable("0", "Object", emptyList()),
                    variable("1", "String", "Recovered"), variable("2", "bool", "true"),
                    variable("3", "String", "<null>"), variable("4", "StringName", ""),
                    variable("5", "StringName", "Node")))
            }
            else {
                assertTrue("(items)[0]" in expression)
                numberedAnswer(expression, probeFields(listOf("Outer")))
            }
        }, { GdFrameSourceSelf.Present })
        val root = GdDapXValue(FACTORY, executor, untyped("Nil", "[<EncodedObjectAsID#1>]", rows),
            owner = GdOwnerExpression("items"), frame = frame)
        val objectRow = expand(root, executor).values.last() as GdDapXValue
        executor.runLast()
        assertEquals("Outer", presentationText(objectRow.createValuePresentation(objectRow.variable, true, false), "renderComment").trim())
        val category = expand(objectRow, executor).values.single()
        val member = expand(category, executor).values.single() as GdDapXValue
        assertEquals(listOf(false, true), requests.map { ".get(\"child\")" in it })
        assertEquals("child", member.variable.name)
        assertTrue(member.variable is DapStructuredVariable)
        assertEquals("Recovered", presentationText(member.createValuePresentation(member.variable, true, false), "renderComment").trim())
    }

    private fun probeFields(texts: List<String>): List<DapVariable> = texts.flatMapIndexed { index, text ->
        val valid = if (text == "not an object") "false" else "true"
        listOf(variable("${index * 5}", "String", text), variable("${index * 5 + 1}", "bool", valid),
            variable("${index * 5 + 2}", "String", "<null>"), variable("${index * 5 + 3}", "StringName", ""),
            variable("${index * 5 + 4}", "StringName", "Node"))
    }

    @Test
    fun testCategoryStaysOutsideGroups(): Unit = withExecutor { executor ->
        val parent = counted("Array", listOf(variable("size", "int", "101"),
            structuredVariable("metadata", "Category", emptyList())) + (0..100).map { variable("$it", "int") }) {}
        val top = expand(GdDapXValue(FACTORY, executor, parent, owner = null, frame = null), executor)
        assertEquals(listOf("size", "metadata"), top.values.map { it.name })
        assertEquals(listOf("[0..99]", "[100..100]"), groups(top).map { it.name })
    }

    private fun untyped(type: String?, text: String, rows: List<DapVariable>): DapStructuredVariable =
        object : DapStructuredVariable by counted("Nil", rows, {}) {
            override val type: String? = type
            override val value = text
        }

    private fun counted(type: String, rows: List<DapVariable>, onLoad: () -> Unit): DapStructuredVariable =
        object : DapStructuredVariable by structuredVariable("items", type, rows) {
            override fun DapSessionContext.loadVariables(filter: VariableFilter?, batchSize: Int): Flow<VariableBatch> {
                onLoad()
                assertNull(filter)
                return flowOf(VariableBatch(rows, 0))
            }
        }

    private suspend fun expand(value: XNamedValue, executor: ControlledExecutor): RecordingNode = RecordingNode().also {
        value.computeChildren(it)
        executor.jobs[executor.jobs.lastIndex].await()
    }

    private suspend fun expandGroup(node: RecordingNode, index: Int, executor: ControlledExecutor): RecordingNode = RecordingNode().also {
        groups(node)[index].computeChildren(it)
        executor.jobs[executor.jobs.lastIndex].await()
    }

    private fun groups(node: RecordingNode) = node.batches.flatMap { it.first.topGroups }

    private fun watch(value: XNamedValue): String? = value.calculateEvaluationExpression().blockingGet(1, TimeUnit.SECONDS)?.expression

    private fun presentationText(presentation: XValuePresentation, renderMethod: String = "renderValue"): String {
        val text = StringBuilder()
        val renderer = Proxy.newProxyInstance(javaClass.classLoader, arrayOf(XValuePresentation.XValueTextRenderer::class.java)) {
            _, method, args -> if (method.name == renderMethod) text.append(args?.firstOrNull()) else null
        } as XValuePresentation.XValueTextRenderer
        presentation.renderValue(renderer)
        return text.toString()
    }

    companion object {
        private val FACTORY = DefaultDapXDebuggerPresentationFactory(null)
    }
}
