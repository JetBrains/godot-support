package com.jetbrains.godot.gdscript.lsp.integration

import com.intellij.openapi.util.registry.Registry
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.integration.GodotEditorLsp
import com.jetbrains.godot.gdscript.integration.godotEditorFixture
import gdscript.completion.GdCompletionDeduplicatingContributor
import gdscript.completion.GdCompletionSource
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test

@TestApplication
class GodotLspCompletionDeduplicationTest {
  private val editor = godotEditorFixture(GodotEditorLsp.ENABLED, ::lspProject)
  private val myFixture get() = editor.get().codeInsight

  private val script = """
    extends Node
    func _ready():
      A<caret>

    class A1:
      func ppa1():
        pass
  """.trimIndent()

  @Test
  fun testDuplicateClassNameIsDeduplicated() = timeoutRunBlocking(timeout = defaultLspTestTimeout) {
    myFixture.configureByText("main.gd", script)

    val items = editor.get().runCompletionAndGetElements().filter { it.lookupString == "A1" }

    assertEquals(1, items.size) { "Expected exactly one 'A1' completion item, got: $items" }
    assertNotEquals(
      GdCompletionSource.LSP,
      items.single().getUserData(GdCompletionDeduplicatingContributor.SERVICE_COMPLETION_KEY),
    )
  }

  // The Godot LSP offers keywords, such as `pass`, that GdKeywordContributor also offers.
  @Test
  fun testKeywordIsDeduplicated() = timeoutRunBlocking(timeout = defaultLspTestTimeout) {
    myFixture.configureByText("main.gd", """
      extends Node
      func _ready():
        pas<caret>
    """.trimIndent())

    val items = editor.get().runCompletionAndGetElements().filter { it.lookupString == "pass" }

    assertEquals(1, items.size) { "Expected exactly one 'pass' completion item, got: $items" }
    assertNotEquals(
      GdCompletionSource.LSP,
      items.single().getUserData(GdCompletionDeduplicatingContributor.SERVICE_COMPLETION_KEY),
    )
  }

  // Regression guard: proves the scenario above is a genuine LSP/PolySymbols collision - with the
  // deduplication switched off, both sources' "A1" items reach the lookup.
  @Test
  fun testDuplicateClassNameReappearsWhenDeduplicationIsDisabled() = timeoutRunBlocking(timeout = defaultLspTestTimeout) {
    val registryValue = Registry.get("gdscript.completion.deduplicateLspItems")
    val previousValue = registryValue.asBoolean()
    registryValue.setValue(false)
    try {
      myFixture.configureByText("main.gd", script)

      val items = editor.get().runCompletionAndGetElements().filter { it.lookupString == "A1" }

      assertEquals(2, items.size) { "Expected the LSP and PolySymbols 'A1' items both to be present, got: $items" }
    } finally {
      registryValue.setValue(previousValue)
    }
  }
}
