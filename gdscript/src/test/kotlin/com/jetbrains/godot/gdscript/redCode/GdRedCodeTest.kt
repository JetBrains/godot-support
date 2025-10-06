package com.jetbrains.godot.gdscript.redCode

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdRedCodeTest : BasePlatformTestCase() {
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

      test(code)
  }

    fun testDoubleQuoteStringNameLiteral(){
        val code = """var my_var = &"stringname literal""""
        test(code)
    }

    fun testSingleQuoteStringNameLiteral(){
        val code = """var my_var = &'stringname literal'"""
        test(code)
    }

    fun testPassForClass(){
        val code = """
            |class A:
            |	pass
        """.trimIndent()
        test(code)
    }

    private fun test(code: String) {
        val psiFile = myFixture.configureByText("a.gd", code)

        val errors = psiFile.children.flatMap { collectErrors(it) }

        // Ensure there are no PsiErrorElements (no red code)
        assertTrue(
            "Did not expect red code; actual errors: " +
                errors.joinToString(" | ") { it.errorDescription + "@" + it.textRange },
            errors.isEmpty()
        )
    }
}
