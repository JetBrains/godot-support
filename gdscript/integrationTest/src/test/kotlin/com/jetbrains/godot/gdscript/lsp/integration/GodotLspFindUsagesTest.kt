package com.jetbrains.godot.gdscript.lsp.integration

import com.intellij.platform.lsp.api.customization.LspFindReferencesDisabled
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.integration.GodotEditorLsp
import com.jetbrains.godot.gdscript.integration.godotEditorFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@TestApplication
class GodotLspFindUsagesTest {
    private val editor = godotEditorFixture(GodotEditorLsp.ENABLED, ::lspProject)
    private val myFixture get() = editor.get().codeInsight

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
        editor.get().runFindUsagesByMarker(2)
    }

    @Test
    fun testLspFindReferencesIsDisabled() = timeoutRunBlocking(timeout = defaultLspTestTimeout) {
        myFixture.configureByText(
            "main.gd",
            """
              extends Node
              func foo():
                pass
          """.trimIndent()
        )
        // RIDER-142711: an enabled LSP find-references customizer duplicates GDScript's own
        // PolySymbols-backed "Find Usages" target for every declaration.
        assertEquals(LspFindReferencesDisabled, editor.get().godotFindReferencesCustomizer())
    }
}
