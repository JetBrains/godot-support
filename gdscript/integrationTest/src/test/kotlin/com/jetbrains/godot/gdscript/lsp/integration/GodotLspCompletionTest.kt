package com.jetbrains.godot.gdscript.lsp.integration

import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.integration.GodotEditorLsp
import com.jetbrains.godot.gdscript.integration.godotEditorFixture
import com.jetbrains.godot.test.project.godotProject
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@TestApplication
class GodotLspCompletionTest {
  private val editor = godotEditorFixture(GodotEditorLsp.ENABLED, ::completionProject)
  private val myFixture get() = editor.get().codeInsight

  @Test
  @ChecklistItems(["LSP functionality/Completion"])
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
  @ChecklistItems(["LSP functionality/Completion"])
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
          Input.is_action_pressed("ui_accept"<caret>)
      """.trimIndent()
    )
  }

    @Test
    @ChecklistItems(["LSP functionality/Completion"])
    fun testResourceCompletionMovesCaretAfterDoubleQuote() = checkResourceCompletion("\"")

    @Test
    @ChecklistItems(["LSP functionality/Completion"])
    fun testResourceCompletionMovesCaretAfterSingleQuote() = checkResourceCompletion("'")

    @Test
    @ChecklistItems(["LSP functionality/Completion"])
    fun testBareDollarCompletionMovesCaretAfterNodeName() = checkNodeCompletion("$<caret>", $$"$Plain<caret>", $$"$Plain")

    @Test
    @ChecklistItems(["LSP functionality/Completion"])
    fun testUniqueNodeCompletionMovesCaretAfterClosingQuote() = checkNodeCompletion("%\"Tar<caret>\"", "%\"Target\"<caret>")

    @Test
    @ChecklistItems(["LSP functionality/Completion"])
    fun testStringNameCompletionMovesCaretAfterDoubleQuote() = checkStringNameCompletion("\"")

    @Test
    @ChecklistItems(["LSP functionality/Completion"])
    fun testStringNameCompletionMovesCaretAfterSingleQuote() = checkStringNameCompletion("'")

    private fun checkResourceCompletion(quote: String) = timeoutRunBlocking(timeout = defaultLspTestTimeout) {
        myFixture.configureByText(
            "main.gd",
            """
                extends Node
                func _ready():
                  preload(${quote}res://main.ts<caret>$quote)
            """.trimIndent()
        )
        editor.get().runCompletionAndCheckAgainst(
            """
                extends Node
                func _ready():
                  preload(${quote}res://main.tscn$quote<caret>)
            """.trimIndent()
        )
    }

    private fun checkNodeCompletion(
        expression: String,
        expected: String,
        lookupString: String? = null,
    ) = timeoutRunBlocking(timeout = defaultLspTestTimeout) {
        val script = myFixture.addFileToProject(
            "main.gd",
            """
                extends Node
                func _ready():
                  print($expression)
            """.trimIndent()
        )
        myFixture.configureFromExistingVirtualFile(script.virtualFile)
        editor.get().runCompletionAndCheckAgainst(
            """
                extends Node
                func _ready():
                  print($expected)
            """.trimIndent(),
            lookupString = lookupString,
        )
    }

    private fun checkStringNameCompletion(quote: String) = timeoutRunBlocking(timeout = defaultLspTestTimeout) {
        myFixture.configureByText(
            "main.gd",
            """
                extends Node
                func _ready():
                  Input.is_action_pressed(&${quote}ui_a<caret>$quote)
            """.trimIndent()
        )
        editor.get().runCompletionAndCheckAgainst(
            """
                extends Node
                func _ready():
                  Input.is_action_pressed(&${quote}ui_accept$quote<caret>)
            """.trimIndent()
        )
    }
}

private fun completionProject() = godotProject("LspTest") {
    script("main.gd", "extends Node\n\nfunc _ready():\n\tpass\n")
    scene("main.tscn", main = true) {
        root("Main", "Node", script = "main.gd") {
            node("Target", "Node") {
                uniqueNameInOwner = true
            }
            node("Plain", "Node")
        }
    }
}
