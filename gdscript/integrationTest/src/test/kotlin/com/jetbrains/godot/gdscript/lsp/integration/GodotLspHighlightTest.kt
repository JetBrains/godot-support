package com.jetbrains.godot.gdscript.lsp.integration

import com.intellij.testFramework.common.timeoutRunBlocking
import org.junit.jupiter.api.Test

class GodotLspHighlightTest : GodotLspBaseTest() {
    @Test
    fun testHighlight() = timeoutRunBlocking(timeout = defaultLspTestTimeout) {
        myFixture.configureByText(
            "main.gd",
            """
              func _ready():
                  <error>bad</error><caret>
          """.trimIndent()
        )
        runHighlightingCheck()
    }
}
