package com.jetbrains.godot.gdscript.resolve

class GdMatchWhenGuardResolveTest : ResolveTestBase() {

  fun testPatternBindingsResolveInGuardAndBody() {
    val file = loadByTestName()
    val annotated = dumpResolvesWithInlineMarkers(file)
    println("[DEBUG_LOG] Annotated:\n" + annotated)
    assertGold(annotated)
  }
}
