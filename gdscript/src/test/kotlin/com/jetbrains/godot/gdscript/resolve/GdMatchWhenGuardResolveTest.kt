package com.jetbrains.godot.gdscript.resolve

import com.intellij.openapi.application.EDT
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdResolveFixture
import com.jetbrains.godot.gdscript.gdResolveFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Resolve and navigation"])
@TestApplication
class GdMatchWhenGuardResolveTest {
  private val gd: GdResolveFixture by gdResolveFixture("gdscript/resolve")

  @Test
  fun testPatternBindingsResolveInGuardAndBody() = timeoutRunBlocking {
    withContext(Dispatchers.EDT) {
      val file = gd.loadByTestName()
      val annotated = gd.dumpResolvesWithInlineMarkers(file)
      println("[DEBUG_LOG] Annotated:\n" + annotated)
      gd.assertGold(annotated)
    }
  }
}
