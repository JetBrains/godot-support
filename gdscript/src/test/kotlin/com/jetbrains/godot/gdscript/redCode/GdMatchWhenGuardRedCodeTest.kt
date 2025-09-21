package com.jetbrains.godot.gdscript.redCode

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdMatchWhenGuardRedCodeTest : BasePlatformTestCase() {
    fun testMatchWhenGuardHasNoRedCode() {
        val code = """
      |func change(setting: String, newValue: Variant, batch_edit = false):
      |  match setting:
      |    "graphics_sky" when not batch_edit:
      |      tweak_environment_all()
      |    "graphics_lod":
      |      _apply_lod(newValue)
      |""".trimMargin()

        val psiFile = myFixture.configureByText("a.gd", code)

        val errors = psiFile.children.flatMap { collectErrors(it) }

        assertTrue(
            "Did not expect red code for match with a 'when' guard; actual errors: " +
            errors.joinToString(" | ") { it.errorDescription + "@" + it.textRange },
            errors.isEmpty()
        )
    }

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

        val psiFile = myFixture.configureByText("guards.gd", code)

        val errors = psiFile.children.flatMap { collectErrors(it) }

        assertTrue(
            "Did not expect red code for match pattern guards examples; actual errors: " +
            errors.joinToString(" | ") { it.errorDescription + "@" + it.textRange },
            errors.isEmpty()
        )
    }
}
