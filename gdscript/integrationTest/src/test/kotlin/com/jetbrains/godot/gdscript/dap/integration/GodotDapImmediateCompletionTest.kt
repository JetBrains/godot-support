package com.jetbrains.godot.gdscript.dap.integration

import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.integration.GodotEditorLsp
import com.jetbrains.godot.gdscript.integration.dapRunArgs
import com.jetbrains.godot.gdscript.integration.defaultDapTestTimeout
import com.jetbrains.godot.gdscript.integration.godotDebug
import com.jetbrains.godot.gdscript.integration.godotEditorFixture
import com.jetbrains.godot.test.project.godotProject
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@TestApplication
class GodotDapImmediateCompletionTest {
    private val editorFixture = godotEditorFixture(GodotEditorLsp.ENABLED, ::dapProject)

    @Test
    fun testEvaluateWindowOffersTheMembersOfTheSuspendedFrame() = timeoutRunBlocking(timeout = defaultDapTestTimeout) {
        val lookups = godotDebug(editorFixture.get()) {
            breakpoint("print")
            launch()
            waitForPause(at = "print")
            immediate { complete() }
        }

        assertTrue(lookups.contains("health"), "The class member of the frame is missing, expected health to be in $lookups")
        assertTrue(lookups.contains("above"), "The local of the frame is missing, expected above to be in $lookups")
    }

    @Test
    fun testEvaluateWindowFiltersOnWhatTheUserTyped() = timeoutRunBlocking(timeout = defaultDapTestTimeout) {
        val lookups = godotDebug(editorFixture.get()) {
            breakpoint("print")
            launch()
            waitForPause(at = "print")
            immediate { complete("hea") }
        }

        assertTrue(lookups.contains("health"), "The typed prefix offers no match, expected health to be in $lookups")
        assertFalse(lookups.contains("above"), "The typed prefix offers an unrelated local, expected above to not be in $lookups")
    }
}

internal fun dapProject() = godotProject("DapCompletion") {
    script(
        "main.gd",
        """
        |extends Node
        |
        |var health := 100
        |
        |func _ready():
        |	var above := 7
        |	print(above, health) # <bp:print>
        """.trimMargin()
    )
    // A DAP launch request plays the project's main scene.
    scene("main.tscn", main = true) {
        this.root("Main", "Node", script = "main.gd")
    }
    dapRunArgs()
}
