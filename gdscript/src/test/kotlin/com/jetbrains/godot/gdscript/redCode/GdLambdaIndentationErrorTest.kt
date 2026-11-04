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
class GdLambdaIndentationErrorTest {
    private val gd: GdFixture by gdLightFixture("gdscript/redCode")

    private fun lineStartOffsetOf(text: String, needle: String): Int {
        val idx = text.indexOf(needle)
        if (idx == -1) return -1
        // go back to previous newline and return next char index
        val nl = text.lastIndexOf('\n', idx)
        return if (nl == -1) 0 else nl + 1
    }

    //RIDER-129478 Expect indent error at the start of the next line after lambda colon
    @Test
    fun testIndentExpectedAfterLambdaColon() {
        val code = """
            |func stop_counting_on_signal(the_signal):
            |\tthe_signal.connect(func():
            |\t\tthe_signal.connect(func():
            |\t\tpass))
            |""".trimMargin()

        val psiFile = gd.fixture.configureByText("a.gd", code)

        val errors = runReadActionBlocking {
            // Find all PsiErrorElements in file
            psiFile.children.flatMap { collectErrors(it) }
        }
        assertTrue(errors.isNotEmpty(), "Expected at least one PsiErrorElement")

        // We expect an error at the beginning of the line with 'pass'
        val targetStart = lineStartOffsetOf(code, "pass")
        assertTrue(targetStart >= 0, "Couldn't locate target line start in the test source")

        // Check error appears on the same line as 'set_process(false)'
        val lineEnd = code.indexOf('\n', targetStart).let { if (it == -1) code.length else it }
        val onSameLine = errors.any { runReadActionBlocking { it.textRange.startOffset } in targetStart until lineEnd }

        assertTrue(
            onSameLine,
            "Expected an error on the line of 'set_process(false)'; actual errors: " +
                    errors.joinToString(" | ") { it.errorDescription + "@" + runReadActionBlocking { it.textRange } },
        )
    }
}
