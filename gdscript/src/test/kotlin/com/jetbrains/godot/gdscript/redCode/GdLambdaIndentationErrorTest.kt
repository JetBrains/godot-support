package com.jetbrains.godot.gdscript.redCode

import com.jetbrains.godot.GdCodeInsightTestBase
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class GdLambdaIndentationErrorTest : GdCodeInsightTestBase() {
    private fun lineStartOffsetOf(text: String, needle: String): Int {
        val idx = text.indexOf(needle)
        if (idx == -1) return -1
        // go back to previous newline and return next char index
        val nl = text.lastIndexOf('\n', idx)
        return if (nl == -1) 0 else nl + 1
    }

    //RIDER-129478 Expect indent error at the start of the next line after lambda colon
    @Test
    @Disabled("RIDER-129478")
    fun testindentExpectedAfterLambdaColon() {
        val code = """
            |func stop_counting_on_signal(the_signal):
            |\tthe_signal.connect(func():
            |\t\tthe_signal.connect(func():
            |\t\tpass))
            |""".trimMargin()

        val psiFile = myFixture.configureByText("a.gd", code)

        // Find all PsiErrorElements in file
        val errors = psiFile.children.flatMap { collectErrors(it) }
        assertTrue(errors.isNotEmpty(), "Expected at least one PsiErrorElement")

        // We expect an error at the beginning of the line with 'pass'
        val targetStart = lineStartOffsetOf(code, "pass")
        assertTrue(targetStart >= 0, "Couldn't locate target line start in the test source")

        // Check error appears on the same line as 'pass'
        val lineEnd = code.indexOf('\n', targetStart).let { if (it == -1) code.length else it }
        val onSameLine = errors.any { it.textRange.startOffset in targetStart until lineEnd }

        assertTrue(
            onSameLine,
            "Expected an error on the line of 'pass'; actual errors: " +
                errors.joinToString(" | ") { it.errorDescription + "@" + it.textRange }
        )
    }
}
