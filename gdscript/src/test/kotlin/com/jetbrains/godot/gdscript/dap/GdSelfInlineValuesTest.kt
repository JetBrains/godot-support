package com.jetbrains.godot.gdscript.dap

import com.intellij.platform.dap.DapEvaluationResult
import com.intellij.platform.dap.DefaultDapInlineValueLocator
import com.intellij.platform.dap.xdebugger.DefaultDapXDebuggerPresentationFactory
import com.intellij.testFramework.LightVirtualFile
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.xdebugger.frame.XNamedValue
import gdscript.dap.remote.GdDapEvaluator
import gdscript.dap.remote.GdDapStackFrame
import gdscript.dap.remote.GdFrameSourceSelf
import gdscript.dap.remote.GdSelfCheckedFrame
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@TestApplication
class GdSelfInlineValuesTest {
    companion object {
        private val FACTORY = DefaultDapXDebuggerPresentationFactory(null)
    }

    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    fun testSelfPropertiesUseLastSourceMatches(nodeSelf: Boolean): Unit = withScenario(nodeSelf) {
        val categories = expand(tree.values.last()).values.filter { it.name in categoryNames }
        val properties = categories.flatMap { category ->
            val rows = expand(category)
            assertTrue(rows.errors.isEmpty())
            rows.values
        }
        assertEquals(listOf(
            "velocity", "base.gd/base_member", "metadata/target", "position", "base.gd/hello",
            "base.gd/nested/child", "MAX", "theme_override_colors/accent", "exported",
        ), properties.map { it.name })
        assertEquals(code.indexOf("self.velocity"), positionOf(properties[0]))
        assertEquals(code.indexOf("self.base_member"), positionOf(properties[1]))
        assertEquals(null, positionOf(properties[2]))
        assertEquals(code.lastIndexOf("position = 4"), positionOf(properties[3]))
        assertEquals(code.indexOf("hello = 10"), positionOf(properties[4]))
        assertEquals(null, positionOf(properties[5]))
        assertEquals(code.indexOf("MAX = 7"), positionOf(properties[6]))
        assertEquals(null, positionOf(properties[7]))
        assertEquals(code.indexOf("self.exported"), positionOf(properties[8]))
    }

    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    fun testOtherTreeRowsHaveNoSelfInlinePositions(nodeSelf: Boolean): Unit = withScenario(nodeSelf) {
        assertTrue(tree.errors.isEmpty())
        assertEquals(listOf("root", "self"), tree.values.map { it.name })
        val root = tree.values.first()
        assertEquals(null, positionOf(root))
        val rootChildren = expand(root)
        assertTrue(rootChildren.errors.isEmpty())
        val rootProperties = rootChildren.batches.flatMap { it.first.bottomGroups }.single { it.name == "Properties" }
        val rootRows = RecordingNode()
        rootProperties.computeChildren(rootRows)
        executor.last.await()
        val rootCategory = rootRows.values.single { it.name == "Members" }
        assertEquals(null, positionOf(rootCategory))
        assertEquals(null, positionOf(expand(rootCategory).values.single { it.name == "velocity" }))
        val self = tree.values.last()
        assertEquals(null, positionOf(self))
        val selfChildren = expand(self)
        val selfCategories = selfChildren.values.filter { it.name in categoryNames }
        assertEquals(categoryNames, selfCategories.map { it.name })
        selfCategories.forEach { assertEquals(null, positionOf(it)) }
        if (nodeSelf) {
            assertEquals(listOf("Parent"), selfChildren.values.filter { it.name == "Parent" }.map { it.name })
            assertEquals(null, positionOf(selfChildren.values.single { it.name == "Parent" }))
            val childrenGroup = selfChildren.batches.flatMap { it.first.topGroups }.single { it.name == "Children" }
            val childRows = RecordingNode()
            childrenGroup.computeChildren(childRows)
            executor.last.await()
            assertTrue(childRows.errors.isEmpty())
            val other = childRows.values.single { it.name == "Other" }
            assertEquals(null, positionOf(other))
            val otherCategory = expand(other).values.single { it.name == "Members" }
            assertEquals(null, positionOf(otherCategory))
            assertEquals(null, positionOf(expand(otherCategory).values.single { it.name == "velocity" }))
            val parent = selfChildren.values.single { it.name == "Parent" }
            val parentCategory = expand(parent).values.single { it.name == "Members" }
            assertEquals(null, positionOf(parentCategory))
            assertEquals(null, positionOf(expand(parentCategory).values.single { it.name == "velocity" }))
        }
        else {
            assertEquals(null, positionOf(selfChildren.values.single { it.name == "direct" }))
        }
    }

    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    fun testNestedAndLocalRowsKeepTheirOwnPositions(nodeSelf: Boolean): Unit = withScenario(nodeSelf) {
        val members = expand(tree.values.last()).values.single { it.name == "Members" }
        val velocity = expand(members).values.single { it.name == "velocity" }
        val nested = expand(velocity)
        assertEquals(listOf("field"), nested.values.map { it.name })
        assertEquals(null, positionOf(nested.values.single()))

        val locals = tree.batches.flatMap { it.first.topGroups }.single { it.name == "Locals" }
        val localRows = RecordingNode()
        locals.computeChildren(localRows)
        executor.last.await()
        val localFields = RecordingNode()
        assertEquals(code.indexOf("velocity = 1"), positionOf(localRows.values.single { it.name == "velocity" }))
        localRows.values.single { it.name == "items" }.computeChildren(localFields)
        executor.last.await()
        assertEquals(listOf("velocity"), localFields.values.map { it.name })
        assertEquals(null, positionOf(localFields.values.single()))
    }

    @ParameterizedTest
    @ValueSource(booleans = [false, true])
    fun testEvaluationResultHasNoSelfInlinePosition(nodeSelf: Boolean): Unit = withScenario(nodeSelf) {
        val watch = RecordingCallback()
        GdDapEvaluator(executor, FACTORY, checked).evaluate("velocity", watch, null)
        executor.last.await()
        assertTrue(watch.errors.isEmpty())
        assertEquals(1, watch.answerCount)
        assertEquals(null, positionOf(watch.values.single() as XNamedValue))
    }

    private val categoryNames = listOf("Members", "Transform", "Constants", "player.gd")

    private class Scenario(
        val executor: ControlledExecutor,
        val code: String,
        val checked: GdSelfCheckedFrame,
        val tree: RecordingNode,
    ) {
        suspend fun expand(value: XNamedValue): RecordingNode {
            val rows = RecordingNode()
            value.computeChildren(rows)
            executor.last.await()
            return rows
        }
    }

    private fun withScenario(nodeSelf: Boolean, body: suspend Scenario.() -> Unit): Unit = withExecutor { executor ->
        val code = """
            velocity = 1
            self.velocity = 2
            self.position = 3
            position = 4
            base_member = 5
            self.base_member = 6
            MAX = 7
            field = 8
            self.exported = 9
            hello = 10
            target = 11
            child = 12
            accent = 13
            Members = 14
            Transform = 15
            Constants = 16
            player.gd = 17
            direct = 18
            Parent = 19
            Children = 20
            root = 21
            Other = 22
        """.trimIndent()
        val categories = listOf(
            structuredVariable("Members", "Category", listOf(
                structuredVariable("velocity", "Dictionary", listOf(variable("field", "String", "8"))),
                variable("base.gd/base_member", "String", "6"),
                variable("metadata/target", "String", "11"),
            )),
            structuredVariable("Transform", "Category", listOf(
                variable("position", "String", "4"), variable("base.gd/hello", "String", "10"),
                variable("base.gd/nested/child", "String", "12"),
            )),
            structuredVariable("Constants", "Category", listOf(
                variable("MAX", "String", "7"), variable("theme_override_colors/accent", "String", "13"),
            )),
            structuredVariable("player.gd", "Category", listOf(variable("exported", "String", "9"))),
        )
        val frame = TestFrame { expression ->
            when {
                expression == "velocity" -> DapEvaluationResult.Success(variable("velocity", "String", "2"))
                expression.contains("get_child_count()") -> numberedAnswer(expression, listOf(variable("0", "int", "1")))
                expression.contains("get_child(0)") -> numberedAnswer(expression, listOf(
                    variable("0", "int", "74"), variable("1", "StringName", "Other"), variable("2", "StringName", "Node"),
                ))
                expression.contains("Engine.get_main_loop().root.get_instance_id()") -> numberedAnswer(expression, listOf(
                    variable("0", "int", "75"), variable("1", "StringName", "Root"), variable("2", "StringName", "Node"),
                ))
                expression.contains("get_parent().get_instance_id()") -> numberedAnswer(expression, listOf(
                    variable("0", "int", "76"), variable("1", "StringName", "Parent"), variable("2", "StringName", "Node"),
                ))
                expression.contains("instance_from_id(74)") || expression.contains("instance_from_id(75)") ||
                    expression.contains("instance_from_id(76)") ->
                    numberedSuccessAnswer(expression, structuredVariable("0", children = categories))
                nodeSelf -> numberedAnswer(expression, listOf(
                    variable("0", "bool", "true"), variable("1", "int", "73"),
                    variable("2", "StringName", "Node2D"), variable("3", "StringName", "Player"),
                ))
                else -> numberedAnswer(expression, listOf(variable("0", "bool", "false")))
            }
        }.apply {
            source = LightVirtualFile("frame.gd", code)
            stoppedLine = code.lines().size
            scopeNames = listOf("Members", "Locals")
            memberVariables = listOf(structuredVariable("self", children = categories + variable("direct", "String")))
            localVariables = listOf(
                structuredVariable("items", "Dictionary", listOf(variable("velocity", "String", "2"))),
                variable("velocity", "String", "2"),
            )
        }
        val checked = GdSelfCheckedFrame(frame, { GdFrameSourceSelf.Present })
        val tree = RecordingNode()
        GdDapStackFrame(FACTORY, executor, frame.thread, checked, DefaultDapInlineValueLocator()).computeChildren(tree)
        executor.last.await()
        Scenario(executor, code, checked, tree).body()
    }
}
