package com.jetbrains.godot.gdscript.redCode

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdRedCodeTest : BasePlatformTestCase() {
    // Red code should NOT appear for this valid enum and typed variable initialization
    @Test
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
    @Test
    fun testDoubleQuoteStringNameLiteral() {
        val code = """var my_var = &"stringname literal""""
        test(code)
    }
    @Test
    fun testSingleQuoteStringNameLiteral() {
        val code = """var my_var = &'stringname literal'"""
        test(code)
    }
    @Test
    fun testPassForClass() {
        val code = """
            |class A:
            |	pass
        """.trimMargin()
        test(code)
    }

    @Test
    @Ignore("RIDER-131973 gdscript single line lambda is not recognized")
    fun testSingleLineLambda(){
        val code = """
class Mutex1:
	func lock():
		pass
	func unlock():
		pass
var t_mutex := Mutex1.new()
enum BuildStage { NOT_STARTED = -2, PREPARING = -1, PLACE_ROOMS = 0, PLACE_STAIRS = 1, SEPARATE_ROOMS = 2, CONNECT_ROOMS = 3, FINALIZING = 4, DONE = 5 }
var stage : BuildStage = BuildStage.NOT_STARTED :
	set(v): t_mutex.lock(); stage = v; t_mutex.unlock();
	get: t_mutex.lock(); var v = stage; t_mutex.unlock(); return v;
        """.trimMargin()
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
