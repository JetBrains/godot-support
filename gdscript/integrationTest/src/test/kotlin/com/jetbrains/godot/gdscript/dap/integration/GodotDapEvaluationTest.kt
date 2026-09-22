package com.jetbrains.godot.gdscript.dap.integration

import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.integration.GodotEditorLsp
import com.jetbrains.godot.gdscript.integration.GodotValue
import com.jetbrains.godot.gdscript.integration.dapRunArgs
import com.jetbrains.godot.gdscript.integration.defaultDapTestTimeout
import com.jetbrains.godot.gdscript.integration.godotDebug
import com.jetbrains.godot.gdscript.integration.godotEditorFixture
import com.jetbrains.godot.test.project.godotProject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@TestApplication
class GodotDapEvaluationTest {
    private val editorFixture = godotEditorFixture(GodotEditorLsp.ENABLED, ::evaluationProject)

    @Test
    fun testEvaluationAddsALocalAndAClassMember() = timeoutRunBlocking(timeout = defaultDapTestTimeout) {
        val sum = godotDebug(editorFixture.get()) {
            breakpoint("stop")
            launch()
            waitForPause(at = "stop")
            evaluate("above + health")
        }

        assertEquals(GodotValue(type = null, value = "107"), sum, "The game evaluated the expression to $sum")
    }
}

private fun evaluationProject() = godotProject("DapEvaluation") {
    script(
        "main.gd",
        """
        |extends Node
        |
        |var health := 100
        |
        |func _ready():
        |	var above := 7
        |	print(above, health) # <bp:stop>
        """.trimMargin()
    )
    // A DAP launch request plays the project's main scene.
    scene("main.tscn", main = true) {
        this.root("Main", "Node", script = "main.gd")
    }
    dapRunArgs()
}
