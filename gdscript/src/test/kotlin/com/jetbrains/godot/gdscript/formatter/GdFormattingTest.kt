package com.jetbrains.godot.gdscript.formatter

import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdFormattingTest : BasePlatformTestCase() {
    // perfectly valid code should not be changed by formatter
    fun testLambdaInConnectIndent() {
        val before = """
            |func stop_counting_on_signal(the_signal):
            |\tthe_signal.connect(func():
            |\t\tpass)
            |""".trimMargin()

        val expected = """
            |func stop_counting_on_signal(the_signal):
            |\tthe_signal.connect(func():
            |\t\tpass)
            |""".trimMargin()

        val psiFile = myFixture.configureByText("a.gd", before)
        val document = myFixture.getDocument(psiFile)

        WriteCommandAction.runWriteCommandAction(psiFile.project) {
            CodeStyleManager.getInstance(project).reformatText(psiFile, listOf(psiFile.textRange))
            CodeStyleManager.getInstance(project).reformatText(psiFile, listOf(psiFile.textRange))
        }

        assertEquals("Formatted text should match expected indentation", expected, document.text)
    }

    fun testLambdaInConnectIndent2() {
        val before = """
            |func stop_counting_on_signal(the_signal):
            |\tthe_signal.connect(func():
            |\t\tthe_signal.connect(func():
            |\t\t\t\t\t\tpass))
            |""".trimMargin()

        val expected = """
            |func stop_counting_on_signal(the_signal):
            |\tthe_signal.connect(func():
            |\t\tthe_signal.connect(func():
            |\t\t\t\t\t\tpass))
            |""".trimMargin()

        val psiFile = myFixture.configureByText("a.gd", before)
        val document = myFixture.getDocument(psiFile)

        WriteCommandAction.runWriteCommandAction(psiFile.project) {
            CodeStyleManager.getInstance(project).reformatText(psiFile, listOf(psiFile.textRange))
            CodeStyleManager.getInstance(project).reformatText(psiFile, listOf(psiFile.textRange))
        }

        assertEquals("Formatted text should match expected indentation", expected, document.text)
    }
}