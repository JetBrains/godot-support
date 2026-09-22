package com.jetbrains.godot.gdscript.dap.integration

import com.intellij.openapi.application.EDT
import com.intellij.platform.lsp.api.LspClientManager
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.integration.GodotEditorLsp
import com.jetbrains.godot.gdscript.integration.dapRunArgs
import com.jetbrains.godot.gdscript.integration.defaultDapTestTimeout
import com.jetbrains.godot.gdscript.integration.godotDebug
import com.jetbrains.godot.gdscript.integration.godotEditorFixture
import com.jetbrains.godot.test.project.godotProject
import gdscript.lsp.GodotLspIntegrationProvider
import gdscript.lsp.GodotLspRunningStatusProvider
import gdscript.settings.GdLspConnectionMode
import gdscript.settings.GdLspSettingsFlowService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@TestApplication
class GodotDapWithoutLspTest {
    private val editorFixture = godotEditorFixture(GodotEditorLsp.DISABLED, ::withoutLspProject)

    @Test
    fun testDebugSessionWorksWithoutLsp() = timeoutRunBlocking(timeout = defaultDapTestTimeout) {
        val editor = editorFixture.get()
        val project = editor.project
        val lookups = godotDebug(editor) {
            breakpoint("stop")
            launch()
            waitForPause(at = "stop")

            // The session start is what calls ensureLspRunning, so an earlier assertion cannot fail.
            GodotLspRunningStatusProvider.ensureLspRunning(project)
            val mode = GdLspSettingsFlowService.getInstance(project).lspConnectionMode.value
            withContext(Dispatchers.EDT) {
                PlatformTestUtil.waitWithEventsDispatching(
                    { "The GDScript LSP did not reach a stable state" },
                    {
                        mode == GdLspConnectionMode.Never ||
                            LspClientManager.getInstance(project)
                                .getClients(GodotLspIntegrationProvider::class.java)
                                .isNotEmpty()
                    },
                    10,
                )
            }
            assertTrue(mode == GdLspConnectionMode.Never, "The fixture did not disable the GDScript LSP: $mode")
            assertTrue(
                LspClientManager.getInstance(project).getClients(GodotLspIntegrationProvider::class.java).isEmpty(),
                "The GDScript LSP client started while LSP was disabled"
            )
            assertFalse(GodotLspRunningStatusProvider.isLspRunning(project))
            assertFalse(GodotLspRunningStatusProvider.isLspStartingUp(project))

            immediate { complete("hea") }
        }

        assertTrue(lookups.contains("health"), "The DAP session did not evaluate without LSP, expected health to be in $lookups")
    }
}

private fun withoutLspProject() = godotProject("DapWithoutLsp") {
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
    // The main scene is required because a DAP launch request plays it.
    scene("main.tscn", main = true) {
        this.root("Main", "Node", script = "main.gd")
    }
    dapRunArgs()
}
