package com.jetbrains.godot.gdscript.parser

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.psi.impl.DebugUtil
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.godot.gdscript.util.gdFragment
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["Debug/GDScript/Evaluation"])
@TestApplication
class GdCodeFragmentParserTest {
    private val gd: GdFixture by gdLightFixture("gdscript/parser/codeFragment")

    private fun doParseTest(text: String) {
        val fragment = gd.fixture.gdFragment(text)
        PlatformTestUtil.assertSameLinesWithFile(
            "${gd.testDataPath}/${gd.testName}.txt",
            runReadActionBlocking { DebugUtil.psiToString(fragment, true) }
        )
    }

    @Test
    fun testIdentifier() = doParseTest("member")

    @Test
    fun testSelfAttribute() = doParseTest("self.member")

    @Test
    fun testBinaryOperator() = doParseTest("a + b")

    @Test
    fun testCall() = doParseTest("self.foo()")

    @Test
    fun testChainedAttributeComparison() = doParseTest("self.velocity.x > 0")

    @Test
    fun testTrailingComment() = doParseTest("member # what is it now")

    @Test
    fun testStrayClosingParenthesis() = doParseTest(")")

    @Test
    fun testLoneOperator() = doParseTest("+")

    @Test
    fun testLoneComma() = doParseTest(",")

    @Test
    fun testKeyword() = doParseTest("if")

    @Test
    fun testTrailingDot() = doParseTest("member.")

    @Test
    fun testTwoExpressionsInARow() = doParseTest("foo bar")

    @Test
    fun testSecondStatementOnANewLine() = doParseTest("a\nb")

    @Test
    fun testIncompleteBinaryOperator() = doParseTest("a + ")

    // TODO: Reported, unlike the other two empty inputs below: leading spaces lex as INDENT, which `eof()` does not skip.
    @Test
    fun testBlankInput() = doParseTest("   ")

    @Test
    fun testLeadingWhitespaces() = doParseTest("  false")

    @Test
    fun testTrailingWhitespaces() = doParseTest("false  ")

    @Test
    fun testLeadingAndTrailingWhitespaces() = doParseTest("  false  ")

    @Test
    fun testNewlinesOnly() = doParseTest("\n\n")

    @Test
    fun testEmptyInput() = doParseTest("")
}
