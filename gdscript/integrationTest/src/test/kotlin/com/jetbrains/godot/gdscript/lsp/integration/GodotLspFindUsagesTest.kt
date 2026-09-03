package com.jetbrains.godot.gdscript.lsp.integration

import com.intellij.testFramework.common.timeoutRunBlocking
import org.junit.jupiter.api.Test

class GodotLspFindUsagesTest : GodotLspBaseTest() {
    @Test
    fun testFindUsages() = timeoutRunBlocking(timeout = defaultLspTestTimeout) {
        myFixture.configureByText(
            "main.gd",
            """
              extends Node
              func foo():
                pass
              func bar():
                foo()
              func _ready():
                f<caret>oo()
          """.trimIndent()
        )
        runFindUsagesByMarker(2)
    }
}
