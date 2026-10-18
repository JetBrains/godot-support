package com.jetbrains.godot.gdscript.resolve

import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Resolve and navigation"])
class GdMatchWhenGuardResolveTest : ResolveTestBase() {

  fun testPatternBindingsResolveInGuardAndBody() {
    val file = loadByTestName()
    val annotated = dumpResolvesWithInlineMarkers(file)
    println("[DEBUG_LOG] Annotated:\n" + annotated)
    assertGold(annotated)
  }
}
