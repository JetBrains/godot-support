package com.jetbrains.godot.gdscript.redCode

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Red code"])
@TestApplication
class GdRedCodeTest {
    private val gd: GdFixture by gdLightFixture("gdscript/redCode")

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
    @Disabled("RIDER-131973 gdscript single line lambda is not recognized")
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
        val psiFile = gd.fixture.configureByText("a.gd", code)

        val (isEmpty, message) = runReadActionBlocking {
            val errors = psiFile.children.flatMap { collectErrors(it) }
            errors.isEmpty() to errors.joinToString(" | ") { it.errorDescription + "@" + it.textRange }
        }

        // Ensure there are no PsiErrorElements (no red code)
        assertTrue(isEmpty, "Did not expect red code; actual errors: $message")
    }
}
