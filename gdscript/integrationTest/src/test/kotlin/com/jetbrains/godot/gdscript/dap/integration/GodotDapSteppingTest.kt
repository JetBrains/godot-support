package com.jetbrains.godot.gdscript.dap.integration

import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.xdebugger.frame.XNamedValue
import com.jetbrains.godot.gdscript.integration.GodotDebugSession
import com.jetbrains.godot.gdscript.integration.GodotEditorLsp
import com.jetbrains.godot.gdscript.integration.collectChildren
import com.jetbrains.godot.gdscript.integration.dapRunArgs
import com.jetbrains.godot.gdscript.integration.defaultDapTestTimeout
import com.jetbrains.godot.gdscript.integration.godotDebug
import com.jetbrains.godot.gdscript.integration.godotEditorFixture
import com.jetbrains.godot.gdscript.integration.presentationOf
import com.jetbrains.godot.test.project.godotProject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * The gold file holds the trace: the position of every pause and the variables that the frame offers there.
 */
@TestApplication
class GodotDapSteppingTest {
    private val editorFixture = godotEditorFixture(GodotEditorLsp.ENABLED, ::steppingProject)

    @Test
    fun testStepsThroughTheReadyFunctionAndResumesToTheNextBreakpoint() =
        timeoutRunBlocking(timeout = defaultDapTestTimeout) {
            godotDebug(editorFixture.get()) {
                breakpoint("start")
                breakpoint("end")
                launch()
                waitForPause(at = "start")
                dumpFrame()
                dumpVariables()
                assertSceneRows()
                val snapshots = List(2) {
                    val children = frameChildren()
                    children.groups.map { "[${it.name}]" } + children.values.map { (it as XNamedValue).name }
                }
                assertTrue(snapshots.all { it.containsAll(listOf("[Locals]", "root", "self")) })
                assertEquals(snapshots.first(), snapshots.last())

                stepOver()
                dumpFrame()

                stepInto()
                dumpFrame()
                dumpVariables()
                assertSceneRows()

                resume()
                waitForPause(at = "end")
                dumpFrame()
                dumpVariables()
                assertSceneRows(expandRoot = true)
            }
        }

    private suspend fun GodotDebugSession.assertSceneRows(expandRoot: Boolean = false) {
        val values = frameChildren().values
        assertEquals(listOf("root", "self"), values.map { (it as XNamedValue).name })
        for ((index, value) in values.withIndex()) {
            val presentation = presentationOf(value)
            assertTrue(presentation.myHasChildren, "The scene row must expand")
            assertTrue(presentation.myValue.startsWith(if (index == 0) "Window" else "Main"),
                       "The scene row must show the node class or name")
        }
        val members = collectChildren(values[1]).values.single { (it as XNamedValue).name == "Members" }
        val health = collectChildren(members).values.single { (it as XNamedValue).name == "health" }
        val presentation = presentationOf(health)
        assertEquals("int", presentation.myType)
        assertEquals("100", presentation.myValue)
        if (expandRoot) {
            val children = collectChildren(values[0])
            assertEquals(listOf("Main"), children.values.map { (it as XNamedValue).name })
            assertEquals(listOf("Properties"), children.groups.map { it.name })
        }
    }
}

/** A project with a second function, so that a step can enter one. */
private fun steppingProject() = godotProject("DapStepping") {
    script(
        "main.gd",
        """
        |extends Node
        |
        |var health := 100
        |
        |func double(value: int) -> int:
        |	var doubled := value * 2
        |	return doubled
        |
        |func _ready():
        |	var above := 7 # <bp:start>
        |	var total := double(above)
        |	print(total, health) # <bp:end>
        """.trimMargin()
    )
    // A DAP launch request plays the project's main scene.
    scene("main.tscn", main = true) {
        this.root("Main", "Node", script = "main.gd")
    }
    dapRunArgs()
}
