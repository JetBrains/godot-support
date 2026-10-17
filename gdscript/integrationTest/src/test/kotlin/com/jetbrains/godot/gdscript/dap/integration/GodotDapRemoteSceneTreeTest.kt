package com.jetbrains.godot.gdscript.dap.integration

import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.xdebugger.frame.XNamedValue
import com.jetbrains.godot.gdscript.integration.GodotChildren
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

@TestApplication
class GodotDapRemoteSceneTreeTest {
    private val editorFixture = godotEditorFixture(GodotEditorLsp.DISABLED, ::remoteSceneTreeProject)

    @Test
    fun testRemoteSceneTreeKeepsChildOrderAndShowsNodeProperties() = timeoutRunBlocking(timeout = defaultDapTestTimeout) {
        godotDebug(editorFixture.get()) {
            breakpoint("stop")
            launch()
            waitForPause(at = "stop")

            val root = frameChildren().value("root")
            val rootChildren = collectChildren(root)
            assertEquals(listOf("Main"), rootChildren.values.map { (it as XNamedValue).name })

            val mainChildren = collectChildren(rootChildren.value("Main"))
            assertEquals(listOf("Zebra", "Alpha"), mainChildren.values.map { (it as XNamedValue).name })
            assertTrue(mainChildren.alreadySorted)

            val zebraChildren = collectChildren(mainChildren.value("Zebra"))
            assertEquals(listOf("Leaf"), zebraChildren.values.map { (it as XNamedValue).name })

            val leafChildren = collectChildren(zebraChildren.value("Leaf"))
            val properties = leafChildren.groups.single { it.name == "Properties" }
            val members = collectChildren(properties).value("Members")
            val score = collectChildren(members).value("score")
            val presentation = presentationOf(score)
            assertEquals("int", presentation.myType)
            assertEquals("42", presentation.myValue)
        }
    }
}

private fun GodotChildren.value(name: String) = values.single { (it as XNamedValue).name == name }

private fun remoteSceneTreeProject() = godotProject("DapRemoteSceneTree") {
    script(
        "main.gd",
        """
        |extends Node
        |
        |func _ready():
        |	print("ready") # <bp:stop>
        """.trimMargin()
    )
    script(
        "leaf.gd",
        """
        |extends Node
        |
        |var score := 42
        """.trimMargin()
    )
    scene("main.tscn", main = true) {
        this.root("Main", "Node", script = "main.gd") {
            node("Zebra", "Node") {
                node("Leaf", "Node", script = "leaf.gd")
            }
            node("Alpha", "Node")
        }
    }
    dapRunArgs()
}
