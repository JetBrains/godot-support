package com.jetbrains.godot.gdscript.lsp.integration

import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.integration.GodotEditorLsp
import com.jetbrains.godot.gdscript.integration.godotEditorFixture
import org.junit.jupiter.api.Test

@TestApplication
class GodotLspCompletionTest {
  private val editor = godotEditorFixture(GodotEditorLsp.ENABLED, ::lspProject)
  private val myFixture get() = editor.get().codeInsight

  @Test
  fun testCompletion() = timeoutRunBlocking(timeout = defaultLspTestTimeout) {
    myFixture.configureByText(
      "main.gd",
      """
        extends Node
        func _ready():
          call_<caret>
      """.trimIndent()
    )
    editor.get().runCompletionAndCheckItems(listOf("call_deferred", "call_deferred_thread_group", "call_thread_safe"))
  }

  // RIDER-132708 Literal autocompletion will add extra " requiring manual deletion
  @Test
  fun testCompletionForDoubleQuote() = timeoutRunBlocking(timeout = defaultLspTestTimeout) {
    myFixture.configureByText(
      "main.gd",
      """
        extends Node
        func _ready():
          Input.is_action_pressed("ui_a<caret>")
      """.trimIndent()
    )
    editor.get().runCompletionAndCheckAgainst(
      """
        extends Node
        func _ready():
          Input.is_action_pressed("ui_accept")
      """.trimIndent()
    )
  }
}
