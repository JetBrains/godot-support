package com.jetbrains.godot.gdscript.dap

import com.intellij.platform.dap.DapEvaluationResult
import com.intellij.platform.dap.DapStructuredVariable
import com.intellij.platform.dap.DapVariable
import com.intellij.platform.dap.xdebugger.DefaultDapXDebuggerPresentationFactory
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.xdebugger.frame.XFullValueEvaluator
import com.intellij.xdebugger.frame.XNamedValue
import com.intellij.xdebugger.frame.XValueNode
import com.intellij.xdebugger.frame.XValuePlace
import com.intellij.xdebugger.frame.presentation.XValuePresentation
import gdscript.dap.remote.GdDapEvaluator
import gdscript.dap.remote.GdDapStackFrame
import gdscript.dap.remote.GdDapXScope
import gdscript.dap.remote.GdDapXValue
import gdscript.dap.remote.GdFrameSourceSelf
import gdscript.dap.remote.GdOwnerExpression
import gdscript.dap.remote.GdSelfCheckedFrame
import gdscript.dap.remote.GdWatchContext
import gdscript.dap.remote.GdWatchPath
import gdscript.dap.remote.GdValueKind
import org.jetbrains.concurrency.Promise
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.MethodSource
import org.junit.jupiter.params.provider.ValueSource
import java.util.concurrent.TimeUnit
import java.lang.reflect.Proxy
import javax.swing.Icon

@TestApplication
class GdWatchValueFlowTest {
    @ParameterizedTest
    @EnumSource(GdFrameSourceSelf::class)
    internal fun testEveryScopeCarriesWatchPathsWithoutEvaluation(source: GdFrameSourceSelf): Unit = withExecutor { executor ->
        val frame = TestFrame { error("Watch construction must not evaluate") }.apply {
            scopeNames = listOf("Locals", "Members", "Globals")
            localVariables = listOf(variable("hidden", "int"))
            memberVariables = listOf(variable("player", "Object"))
        }
        val checked = GdSelfCheckedFrame(frame, { source })
        val recoveryFrame = when (source) {
            GdFrameSourceSelf.Present -> checked
            GdFrameSourceSelf.Absent, GdFrameSourceSelf.Unavailable -> null
        }
        val scopes = executor.postAsync { frame.run { scopes() } }.await()
        for (scope in scopes) {
            val rows = RecordingNode()
            GdDapXScope(FACTORY, executor, scope, 0,
                        frame = recoveryFrame.takeIf { scope.name == "Locals" }).computeChildren(rows)
            executor.last.await()
            val row = rows.values.single()
            assertEquals(if (scope.name == "Members") "self.player" else if (scope.name == "Locals") "hidden" else "player", watch(row))
            assertEquals(1, rows.terminalCount)
        }
        executor.stopped = true
        val row = GdDapXValue(FACTORY, executor, variable("player", "Object"), owner = null, frame = null,
                             watchContext = GdWatchContext(GdWatchPath.scope("Globals", listOf(variable("player", "Object"))).single(),
                                                           frame, executor))
        assertEquals("player", watch(row))
    }

    @ParameterizedTest
    @EnumSource(value = GdFrameSourceSelf::class, names = ["Absent", "Unavailable"])
    internal fun testFrameScopeFallbackCarriesWatchPaths(source: GdFrameSourceSelf): Unit = withExecutor { executor ->
        val frame = TestFrame { error("The self check must block evaluation") }.apply {
            scopeNames = listOf("Locals", "Members", "Globals")
            localVariables = listOf(variable("local", "int"))
            memberVariables = listOf(variable("player", "Object"))
        }
        val checked = GdSelfCheckedFrame(frame, { source })
        val tree = RecordingNode()
        val factory = gdscript.dap.remote.GdDapPresentationFactory(PROJECT.get(), null)
        GdDapStackFrame(factory, executor, frame.thread, checked).computeChildren(tree)
        executor.last.await()
        val groups = tree.batches.flatMap { it.first.topGroups }
        assertEquals(listOf("Locals", "Members", "Globals"), groups.map { it.name })
        for (group in groups) {
            val rows = RecordingNode()
            group.computeChildren(rows)
            executor.last.await()
            assertEquals(if (group.name == "Members") "self.player" else if (group.name == "Locals") "local" else "player",
                         watch(rows.values.single()))
        }
    }

    @Test
    fun testGlobalsRejectHiddenAndReservedRoots(): Unit = withExecutor { executor ->
        val frame = TestFrame { error("Unexpected evaluation") }.apply {
            scopeNames = listOf("Locals", "Globals")
            localVariables = listOf(variable("player", "int"))
            memberVariables = listOf(variable("player", "Object"), variable("hash", "Object"), variable("visible", "Object"))
        }
        val globals = executor.postAsync { frame.run { scopes() } }.await().last()
        val rows = RecordingNode()
        GdDapXScope(FACTORY, executor, globals, 1).computeChildren(rows)
        executor.last.await()
        assertEquals(listOf(null, null, "visible"), rows.values.map(::watch))
    }

    @Test
    fun testUnavailableLocalNamesKeepGlobalsVisibleWithoutAWatch(): Unit = withExecutor { executor ->
        val frame = TestFrame(onScopes = { throw IllegalStateException("scope read unavailable") }) { error("Unexpected evaluation") }
        val globals = testScope("Globals", frame) { listOf(variable("player", "Object")) }
        val rows = RecordingNode()
        GdDapXScope(FACTORY, executor, globals, 1).computeChildren(rows)
        executor.last.await()
        assertEquals(listOf("player"), rows.values.map { it.name })
        assertNull(watch(rows.values.single()))
        assertTrue(rows.errors.isEmpty())
    }

    @ParameterizedTest
    @ValueSource(strings = ["player + other", "get_node_or_null(\"Player\")"])
    fun testEvaluationSeedsTheEvaluatedText(expression: String): Unit = withExecutor { executor ->
        val result = structuredVariable(children = listOf(variable("hp", "String")))
        val frame = TestFrame { requested ->
            if (requested == expression) DapEvaluationResult.Success(result) else DapEvaluationResult.Error("probe unavailable")
        }
        val checked = GdSelfCheckedFrame(frame, { GdFrameSourceSelf.Present })
        val callback = RecordingCallback()
        GdDapEvaluator(executor, FACTORY, checked).evaluate(expression, callback, null)
        executor.last.await()
        val row = callback.values.single() as XNamedValue
        assertEquals(expression, watch(row))
        assertEquals("($expression).hp", watch(expand(row, executor).values.single()))
    }

    @Test
    fun testCategoryRecoveryKeepsTheReadablePath(): Unit = withExecutor { executor ->
        var recover = true
        val nested = structuredVariable("Members", "Category", listOf(variable("hp", "String")))
        val frame = TestFrame { expression ->
            assertTrue(recover)
            recover = false
            numberedAnswer(expression, listOf(structuredVariable("0", children = listOf(nested))) + listOf(
                variable("1", "String", "Player:<Node#42>"), variable("2", "bool", "true"),
                variable("3", "String", "<null>"), variable("4", "StringName", ""), variable("5", "StringName", "Node"),
            ))
        }
        val checked = GdSelfCheckedFrame(frame, { GdFrameSourceSelf.Present })
        val category = GdDapXValue(FACTORY, executor,
            structuredVariable("Members", "Category", listOf(variable("base.gd/player", "int", "42"))),
            owner = GdOwnerExpression("self"), frame = checked,
            watchContext = GdWatchContext(GdWatchPath.nodeProperties("self").category(), checked, executor))
        assertNull(watch(category))
        val row = expand(category, executor).values.single()
        assertEquals("self.player", watch(row))
        val childCategory = expand(row, executor).values.single()
        assertNull(watch(childCategory))
        assertEquals("self.player.hp", watch(expand(childCategory, executor).values.single()))
    }

    @Test
    fun testArrayChildrenKeepTheParentPath(): Unit = withExecutor { executor ->
        val array = structuredVariable("items", "Array", listOf(
            variable("size", "int", "1"), structuredVariable("0", children = listOf(variable("hp", "String"))),
        ))
        val frame = TestFrame { error("A known path must not evaluate") }
        val value = GdDapXValue(FACTORY, executor, array, owner = null, frame = null,
                              watchContext = GdWatchContext(GdWatchPath.scope("Locals", listOf(array)).single(), frame, executor))
        val rows = expand(value, executor)
        assertEquals(listOf("items.size()", "items[0]"), rows.values.map(::watch))
        assertEquals("items[0].hp", watch(expand(rows.values.last(), executor).values.single()))
    }

    // Loaded rows give paths to Members, object properties, and evaluated results without extra evaluation.
    // Projection children have no watch path. Dictionary key reads have separate flow tests.
    @ParameterizedTest(name = "{0}")
    @MethodSource("untypedMemberCases")
    fun testUntypedMembersAndResultsUseLoadedRows(
        type: String,
        children: List<DapVariable>,
        suffixes: List<String?>,
    ): Unit = withExecutor { executor ->
        val text = mathValueText(type)
        val frame = TestFrame { error("Type resolution must not evaluate") }
        for (declaredType in listOf(null, "Nil")) {
            val parent = untypedStructured("value", declaredType, text, children)
            val members = testScope("Members", frame) { listOf(parent) }
            val rows = RecordingNode()
            GdDapXScope(FACTORY, executor, members, 0).computeChildren(rows)
            executor.last.await()
            val memberParts = expand(rows.values.single(), executor)
            assertEquals(suffixes.map { it?.let { "self.value$it" } }, memberParts.values.map(::watch))

            val category = structuredVariable("Members", "Category", listOf(parent))
            val objectRow = GdDapXValue(FACTORY, executor, structuredVariable("self", "Object", listOf(category)),
                owner = null, frame = null, watchContext = GdWatchContext(GdWatchPath.nodeProperties("self"), frame, executor))
            val categoryRow = expand(objectRow, executor).values.single()
            val property = expand(categoryRow, executor).values.single()
            val propertyParts = expand(property, executor)
            assertEquals(memberParts.values.map(::watch), propertyParts.values.map(::watch))

            val expression = if (type == "Transform3D") "global_transform" else if (type == "Array") "[1, 2]" else "value + other"
            var requests = 0
            val evaluatedFrame = TestFrame { requested ->
                assertEquals(expression, requested)
                requests++
                DapEvaluationResult.Success(parent)
            }
            val checked = GdSelfCheckedFrame(evaluatedFrame, { GdFrameSourceSelf.Present })
            val callback = RecordingCallback()
            GdDapEvaluator(executor, FACTORY, checked).evaluate(expression, callback, null)
            executor.last.await()
            val evaluated = callback.values.single() as XNamedValue
            val presentationNode = CollectionValueNode()
            evaluated.computePresentation(presentationNode, XValuePlace.TREE)
            if (type != "Array") {
                assertEquals(text, presentationNode.text())
                assertNull(presentationNode.evaluator)
            }
            val resultParts = expand(evaluated, executor)
            if (type != "Array") {
                assertEquals(text, presentationNode.text())
                assertNull(presentationNode.evaluator)
                assertTrue(resultParts.batches.flatMap { it.first.topGroups }.isEmpty())
            }
            assertEquals(suffixes.map { it?.let { "($expression)$it" } }, resultParts.values.map(::watch))
            assertEquals(1, requests)
            assertEquals(1, memberParts.terminalCount)
            assertEquals(1, propertyParts.terminalCount)
            assertEquals(1, resultParts.terminalCount)
        }
    }

    @Test
    fun testNestedMathPathsAndUnsupportedParents(): Unit = withExecutor { executor ->
        val vector = listOf("x", "y", "z").map { variable(it, "float") }
        val basis = structuredVariable("basis", "Basis", listOf("x", "y", "z").map { structuredVariable(it, "Vector3", vector) })
        val transform = untypedStructured("0", null, mathValueText("Transform3D"), listOf(basis, structuredVariable("origin", "Vector3", vector)))
        val array = untypedStructured("value", "Nil", "[${transform.value}]", listOf(variable("size", "int", "1"), transform))
        val frame = TestFrame { error("Math parts must not evaluate") }
        val root = GdDapXValue(FACTORY, executor, array, owner = null, frame = null,
            watchContext = GdWatchContext(GdWatchPath.evaluated("items + other"), frame, executor))
        val item = expand(root, executor).values.last()
        val basisRow = expand(item, executor).values.first()
        assertEquals("(items + other)[0].basis", watch(basisRow))
        val basisX = expand(basisRow, executor).values.first()
        assertEquals("(items + other)[0].basis.transposed().x", watch(basisX))
        assertEquals("(items + other)[0].basis.transposed().x.y", watch(expand(basisX, executor).values[1]))

        val aabb = untypedStructured("bounds", "Nil", mathValueText("AABB"), listOf(
            structuredVariable("position", "Vector3", vector), structuredVariable("size", "Vector3", vector),
        ))
        val bounds = GdDapXValue(FACTORY, executor, aabb, owner = null, frame = null,
            watchContext = GdWatchContext(GdWatchPath.evaluated("bounds"), frame, executor))
        val size = expand(bounds, executor).values.last()
        assertEquals("(bounds).size", watch(size))
        assertEquals("(bounds).size.z", watch(expand(size, executor).values.last()))

        for (text in listOf("{0: transform}", "[unknown]")) {
            val unsupportedParent = untypedStructured("result", null, text, listOf(transform))
            val row = GdDapXValue(FACTORY, executor, unsupportedParent, owner = null, frame = null,
                watchContext = GdWatchContext(GdWatchPath.evaluated("result"), frame, executor))
            val child = expand(row, executor).values.single()
            assertNull(watch(child))
            val parts = expand(child, executor).values
            assertTrue(parts.all { watch(it) == null })
            assertTrue(expand(parts.first(), executor).values.all { watch(it) == null })
        }
    }

    @Test
    fun testCollectionRowsShowSizeAfterExpansionAndKeepFullText(): Unit = withExecutor { executor ->
        val cases = GdValueKind.arrayTypes.map { type ->
            Triple(type, "[first, second]", listOf(variable("size", "int", "2"), variable("0", "int"), variable("1", "int"),
                variable("metadata", "Category")))
        } + listOf(
            Triple("Dictionary", "{size: 1, key: 2}", listOf(variable("size", "int"), variable("key", "int"),
                variable("metadata", "Category"))),
            Triple(null, "[1, 2]", listOf(variable("size", "int", "2"), variable("0", "int"), variable("1", "int"))),
            Triple("Nil", "[1, 2]", listOf(variable("size", "int", "2"), variable("0", "int"), variable("1", "int"))),
            Triple("Nil", "{size: 1, key: 2}", listOf(variable("size", "int"), variable("key", "int"))),
            Triple("Nil", "{  }", emptyList()),
        )
        for ((type, text, children) in cases) {
            val expectedType = type?.takeUnless { it == "Nil" } ?: if (text.startsWith("{")) "Dictionary" else "Array"
            val raw = untypedStructured("items", type, text, children)
            val row = GdDapXValue(FACTORY, executor, raw, owner = null, frame = null)
            val node = CollectionValueNode()
            row.computePresentation(node, XValuePlace.TREE)
            assertEquals(expectedType, node.presentation?.type)
            assertTrue(node.hasChildren)
            assertEquals("", node.text())
            assertEquals("", node.presentation?.separator)
            val evaluator = node.evaluator!!
            var fullText: String? = null
            evaluator.startEvaluation(object : XFullValueEvaluator.XFullValueEvaluationCallback {
                override fun evaluated(fullValue: String) { fullText = fullValue }
                override fun errorOccurred(errorMessage: String) { error(errorMessage) }
                override fun isObsolete() = false
            })
            assertEquals(text, fullText)
            assertEquals(text, raw.value)
            expand(row, executor)
            assertEquals("size = ${if (children.isEmpty()) 0 else 2}", node.text())
            assertEquals(expectedType, node.presentation?.type)
            assertTrue(node.hasChildren)
            val staleRow = GdDapXValue(FACTORY, executor, raw, owner = null, frame = null)
            val staleNode = CollectionValueNode()
            staleRow.computePresentation(staleNode, XValuePlace.TREE)
            staleNode.obsolete = true
            expand(staleRow, executor)
            assertEquals("", staleNode.text())
        }
        for (text in listOf("[hello]", "{hello}", "[P: hello]")) {
            for (type in listOf(null, "Nil")) {
                val raw = object : DapVariable by variable("text", "String", text) { override val type = type }
                val row = GdDapXValue(FACTORY, executor, raw, owner = null, frame = null)
                val node = CollectionValueNode()
                row.computePresentation(node, XValuePlace.TREE)
                assertEquals(text, node.text())
                assertEquals(type, node.presentation?.type)
                assertTrue(!node.hasChildren)
                assertNull(node.evaluator)
            }
        }
        for (type in listOf("Object", "Category")) {
            val row = GdDapXValue(FACTORY, executor, structuredVariable("row", type, emptyList()), owner = null, frame = null)
            val node = CollectionValueNode()
            row.computePresentation(node, XValuePlace.TREE)
            assertEquals("value", node.text())
            assertNull(node.evaluator)
        }
    }

    @Test
    fun testEmptyPackedArrayShowsZeroAfterExpansion(): Unit = withExecutor { executor ->
        val raw = untypedStructured("items", "PackedByteArray", "[]", listOf(variable("size", "int", "0")))
        val row = GdDapXValue(FACTORY, executor, raw, owner = null, frame = null)
        val node = CollectionValueNode()
        row.computePresentation(node, XValuePlace.TREE)
        assertEquals("PackedByteArray", node.presentation?.type)
        assertEquals("", node.text())
        expand(row, executor)
        assertEquals("size = 0", node.text())
    }

    private class CollectionValueNode : XValueNode {
        var presentation: XValuePresentation? = null
        var evaluator: XFullValueEvaluator? = null
        var obsolete = false
        var hasChildren = false
        override fun setPresentation(icon: Icon?, type: String?, value: String, hasChildren: Boolean) = Unit
        override fun setPresentation(icon: Icon?, presentation: XValuePresentation, hasChildren: Boolean) {
            this.presentation = presentation
            this.hasChildren = hasChildren
        }
        override fun setFullValueEvaluator(fullValueEvaluator: XFullValueEvaluator) { evaluator = fullValueEvaluator }
        override fun isObsolete() = obsolete
        fun text(): String {
            val result = StringBuilder()
            val renderer = Proxy.newProxyInstance(javaClass.classLoader, arrayOf(XValuePresentation.XValueTextRenderer::class.java)) {
                _, method, args -> if (method.name == "renderValue") result.append(args?.firstOrNull()) else null
            } as XValuePresentation.XValueTextRenderer
            presentation!!.renderValue(renderer)
            return result.toString()
        }
    }

    private fun untypedStructured(name: String, type: String?, text: String, children: List<DapVariable>): DapStructuredVariable =
        object : DapStructuredVariable by structuredVariable(name, "Nil", children) {
            override val type = type
            override val value = text
        }

    private suspend fun expand(row: XNamedValue, executor: ControlledExecutor): RecordingNode {
        val rows = RecordingNode()
        row.computeChildren(rows)
        executor.last.await()
        return rows
    }

    private fun watch(row: XNamedValue): String? {
        var answers = 0
        val promise = row.calculateEvaluationExpression()
        promise.onSuccess { answers++ }
        promise.onError { error("A watch path must not reject") }
        assertEquals(Promise.State.SUCCEEDED, promise.state)
        assertEquals(1, answers)
        return promise.blockingGet(0, TimeUnit.SECONDS)?.expression
    }

    companion object {
        private val FACTORY = DefaultDapXDebuggerPresentationFactory(null)
        private val PROJECT = com.intellij.testFramework.junit5.fixture.projectFixture()

        @JvmStatic
        private fun untypedMemberCases(): List<Arguments> = listOf(
            Arguments.of("Vector2",
                listOf(variable("x", "float"), variable("y", "float")),
                listOf(".x", ".y")),
            Arguments.of("Vector2i",
                listOf(variable("x", "int"), variable("y", "int")),
                listOf(".x", ".y")),
            Arguments.of("Vector3",
                listOf(variable("x", "float"), variable("y", "float"), variable("z", "float")),
                listOf(".x", ".y", ".z")),
            Arguments.of("Vector3i",
                listOf(variable("x", "int"), variable("y", "int"), variable("z", "int")),
                listOf(".x", ".y", ".z")),
            Arguments.of("Rect2",
                listOf(variable("x", "float"), variable("y", "float"), variable("w", "float"), variable("h", "float")),
                listOf(".position.x", ".position.y", ".size.x", ".size.y")),
            Arguments.of("Rect2i",
                listOf(variable("x", "int"), variable("y", "int"), variable("w", "int"), variable("h", "int")),
                listOf(".position.x", ".position.y", ".size.x", ".size.y")),
            Arguments.of("Transform2D",
                listOf(variable("x", "Vector2"), variable("y", "Vector2"), variable("origin", "Vector2")),
                listOf(".x", ".y", ".origin")),
            Arguments.of("AABB",
                listOf(variable("position", "Vector3"), variable("size", "Vector3")),
                listOf(".position", ".size")),
            Arguments.of("Plane",
                listOf(variable("d", "float"), variable("normal", "Vector3")),
                listOf(".d", ".normal")),
            Arguments.of("Quaternion",
                listOf(variable("x", "float"), variable("y", "float"), variable("z", "float"), variable("w", "float")),
                listOf(".x", ".y", ".z", ".w")),
            Arguments.of("Color",
                listOf(variable("r", "float"), variable("g", "float"), variable("b", "float"), variable("a", "float")),
                listOf(".r", ".g", ".b", ".a")),
            Arguments.of("Basis",
                listOf(variable("x", "Vector3"), variable("y", "Vector3"), variable("z", "Vector3")),
                listOf(".transposed().x", ".transposed().y", ".transposed().z")),
            Arguments.of("Transform3D",
                listOf(variable("basis", "Basis"), variable("origin", "Vector3")),
                listOf(".basis", ".origin")),
            Arguments.of("Array",
                listOf(variable("size", "int", "2"), variable("0", "int"), variable("1", "int")),
                listOf(".size()", "[0]", "[1]")),
            Arguments.of("Projection",
                listOf(variable("x", "Vector4"), variable("y", "Vector4"), variable("z", "Vector4"), variable("w", "Vector4")),
                listOf(null, null, null, null)),
        )
    }
}
