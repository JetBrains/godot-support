package com.jetbrains.godot.gdscript.lsp.integration

import com.intellij.testFramework.common.timeoutRunBlocking
import org.junit.jupiter.api.Test

class GodotLspCompletionTest : GodotLspBaseTest() {
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
        runCompletionAndCheckItems(listOf("call_deferred", "call_deferred_thread_group", "call_thread_safe"))
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
        runCompletionAndCheckAgainst(
            """
            extends Node
            func _ready():
              Input.is_action_pressed("ui_accept")
        """.trimIndent()
        )
    }
}
