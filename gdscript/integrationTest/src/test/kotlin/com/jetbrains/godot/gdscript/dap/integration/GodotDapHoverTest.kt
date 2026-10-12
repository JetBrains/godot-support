package com.jetbrains.godot.gdscript.dap.integration

import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.integration.GodotEditorLsp
import com.jetbrains.godot.gdscript.integration.dapRunArgs
import com.jetbrains.godot.gdscript.integration.defaultDapTestTimeout
import com.jetbrains.godot.gdscript.integration.godotDebug
import com.jetbrains.godot.gdscript.integration.godotEditorFixture
import com.jetbrains.godot.test.project.godotProject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@TestApplication
class GodotDapHoverTest {
    private val editorFixture = godotEditorFixture(GodotEditorLsp.ENABLED, ::hoverProject)

    @Test
    fun testSelectedFrameHover() = timeoutRunBlocking(timeout = defaultDapTestTimeout) {
        godotDebug(editorFixture.get()) {
            breakpoint("stop")
            launch()
            waitForPause(at = "stop")
            hoverHasNoRange("main.gd", "extends")
            val local = hover("main.gd", "above,", "above", "above")
            assertEquals("7", local.value)
            val node = hover("main.gd", "\$Player)", "\$Player", "get_node_or_null(\"Player\")")
            assertTrue(node.value.matches(Regex("<EncodedObjectAsID#-?\\d+>|Node {2}Player:<Node#-?\\d+>")), "The node path gave $node")
            val nodeName = hover("main.gd", "name)", $$"$Player.name", "get_node_or_null(\"Player\").name")
            assertEquals("Player", nodeName.value)
            hoverHasNoRange("main.gd", "outside)")
        }
    }
}

private fun hoverProject() = godotProject("DapHover") {
    script("main.gd", $$"""
        |extends Node
        |
        |func _ready():
        |\tvar above := 7
        |\tprint($Player.name)
        |\tprint(above, $Player) # <bp:stop>
        |
        |func other():
        |\tvar outside := 9
        |\tprint(outside)
        """.trimMargin().replace("\\t", "\t"))
    scene("main.tscn", main = true) {
        root("Main", "Node", script = "main.gd") {
            node("Player", "Node")
        }
    }
    dapRunArgs()
}
