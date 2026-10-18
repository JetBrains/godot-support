package com.jetbrains.godot.gdscript.lsp.integration

import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.integration.GodotEditorLsp
import com.jetbrains.godot.gdscript.integration.godotEditorFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@TestApplication
class GodotLspHighlightTest {
  private val editor = godotEditorFixture(GodotEditorLsp.ENABLED, ::lspProject)
  private val myFixture get() = editor.get().codeInsight

  @Test
  @ChecklistItems(["LSP functionality/Highlighting"])
  fun testHighlight() = timeoutRunBlocking(timeout = defaultLspTestTimeout) {
    myFixture.configureByText(
      "main.gd",
      """
        func _ready():
            <error>bad</error><caret>
      """.trimIndent()
    )
    editor.get().runHighlightingCheck()
  }
}
