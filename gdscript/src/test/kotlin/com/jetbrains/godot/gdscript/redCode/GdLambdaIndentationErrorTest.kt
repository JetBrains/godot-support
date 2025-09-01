package com.jetbrains.godot.gdscript.redCode

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Ignore

class GdLambdaIndentationErrorTest : BasePlatformTestCase() {
    private fun lineStartOffsetOf(text: String, needle: String): Int {
        val idx = text.indexOf(needle)
        if (idx == -1) return -1
        // go back to previous newline and return next char index
        val nl = text.lastIndexOf('\n', idx)
        return if (nl == -1) 0 else nl + 1
    }


    //RIDER-129478 Expect indent error at the start of the next line after lambda colon
    @Ignore("RIDER-129478") fun testindentExpectedAfterLambdaColon() {
        val code = """
            |func stop_counting_on_signal(the_signal):
            |\tthe_signal.connect(func():
            |\t\tthe_signal.connect(func():
            |\t\tpass))
            |""".trimMargin()

        val psiFile = myFixture.configureByText("a.gd", code)

        // Find all PsiErrorElements in file
        val errors = psiFile.children.flatMap { collectErrors(it) }
        assertTrue("Expected at least one PsiErrorElement", errors.isNotEmpty())

        // We expect an error at the beginning of the line with 'set_process(false)'
        val targetStart = lineStartOffsetOf(code, "set_process(false)")
        assertTrue("Couldn't locate target line start in the test source", targetStart >= 0)

        // Check error appears on the same line as 'set_process(false)'
        val lineEnd = code.indexOf('\n', targetStart).let { if (it == -1) code.length else it }
        val onSameLine = errors.any { it.textRange.startOffset in targetStart until lineEnd }

        assertTrue(
            "Expected an error on the line of 'set_process(false)'; actual errors: " +
                    errors.joinToString(" | ") { it.errorDescription + "@" + it.textRange },
            onSameLine
        )
    }

    private fun collectErrors(element: PsiElement): List<PsiErrorElement> {
        val list = mutableListOf<PsiErrorElement>()
        if (element is PsiErrorElement) list.add(element)
        element.children.forEach { list.addAll(collectErrors(it)) }
        return list
    }
}