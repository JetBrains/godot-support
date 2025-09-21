package com.jetbrains.godot.gdscript.redCode

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdEnumRedCodeTest : BasePlatformTestCase() {
  // Red code should NOT appear for this valid enum and typed variable initialization
  fun testEnumHasNoRedCode() {
    val code = """
      |enum _Anim {
      |  FLOOR,
      |  AIR,
      |}
      |
      |var anim : _Anim = _Anim.FLOOR
      |""".trimMargin()

    val psiFile = myFixture.configureByText("a.gd", code)

    val errors = psiFile.children.flatMap { collectErrors(it) }

    // Ensure there are no PsiErrorElements (no red code)
    assertTrue(
      "Did not expect red code for a simple enum and typed variable; actual errors: " +
        errors.joinToString(" | ") { it.errorDescription + "@" + it.textRange },
      errors.isEmpty()
    )
  }
}
