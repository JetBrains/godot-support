package com.jetbrains.godot.gdscript.dap.integration

import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.integration.GodotEditorLsp
import com.jetbrains.godot.gdscript.integration.dapRunArgs
import com.jetbrains.godot.gdscript.integration.defaultDapTestTimeout
import com.jetbrains.godot.gdscript.integration.godotDebug
import com.jetbrains.godot.gdscript.integration.godotEditorFixture
import com.jetbrains.godot.test.project.godotProject
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

                stepOver()
                dumpFrame()

                stepInto()
                dumpFrame()
                dumpVariables()

                resume()
                waitForPause(at = "end")
                dumpFrame()
                dumpVariables()
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
