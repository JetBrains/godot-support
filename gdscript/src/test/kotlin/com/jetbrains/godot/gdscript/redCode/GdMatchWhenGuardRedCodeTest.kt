package com.jetbrains.godot.gdscript.redCode

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Red code"])
@TestApplication
class GdMatchWhenGuardRedCodeTest {
    private val gd: GdFixture by gdLightFixture("gdscript/redCode")

    @Test
    fun testMatchWhenGuardHasNoRedCode() {
        val code = """
      |func change(setting: String, newValue: Variant, batch_edit = false):
      |  match setting:
      |    "graphics_sky" when not batch_edit:
      |      tweak_environment_all()
      |    "graphics_lod":
      |      _apply_lod(newValue)
      |""".trimMargin()

        val psiFile = gd.fixture.configureByText("a.gd", code)

        val (isEmpty, message) = runReadActionBlocking {
            val errors = psiFile.children.flatMap { collectErrors(it) }
            errors.isEmpty() to errors.joinToString(" | ") { it.errorDescription + "@" + it.textRange }
        }

        assertTrue(
            isEmpty,
            "Did not expect red code for match with a 'when' guard; actual errors: $message"
        )
    }

    @Test
    fun testPatternGuardsExamplesHaveNoRedCode() {
        val code = """
      |func foo(point: Vector2):
      |  match point:
      |    [0, 0]:
      |      print("Origin")
      |    [_, 0]:
      |      print("Point on X-axis")
      |    [0, _]:
      |      print("Point on Y-axis")
      |    [var x, var y] when y == x:
      |      print("Point on line y = x")
      |    [var x, var y] when y == -x:
      |      print("Point on line y = -x")
      |    [var x, var y]:
      |      print("Point (%s, %s)" % [x, y])
      |""".trimMargin()

        val psiFile = gd.fixture.configureByText("guards.gd", code)

        val (isEmpty, message) = runReadActionBlocking {
            val errors = psiFile.children.flatMap { collectErrors(it) }
            errors.isEmpty() to errors.joinToString(" | ") { it.errorDescription + "@" + it.textRange }
        }

        assertTrue(
            isEmpty,
            "Did not expect red code for match pattern guards examples; actual errors: $message"
        )
    }
}
