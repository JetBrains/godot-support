package com.jetbrains.godot.gdscript.resolve

import org.junit.jupiter.api.Test

class GdMatchWhenGuardResolveTest : ResolveTestBase() {

  @Test
  fun testPatternBindingsResolveInGuardAndBody() {
    val file = loadByTestName()
    val annotated = dumpResolvesWithInlineMarkers(file)
    println("[DEBUG_LOG] Annotated:\n" + annotated)
    assertGold(annotated)
  }
}
