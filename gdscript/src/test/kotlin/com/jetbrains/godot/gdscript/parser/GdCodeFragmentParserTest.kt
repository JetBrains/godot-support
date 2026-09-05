package com.jetbrains.godot.gdscript.parser

import com.intellij.psi.impl.DebugUtil
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.gdscript.util.gdFragment
import com.jetbrains.godot.getBaseTestDataPath
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class GdCodeFragmentParserTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String =
        getBaseTestDataPath().resolve("testData/gdscript").pathString

    private fun doParseTest(text: String) {
        val fragment = myFixture.gdFragment(text)
        assertSameLinesWithFile(
            "$testDataPath/parser/codeFragment/${getTestName(true)}.txt",
            DebugUtil.psiToString(fragment, true)
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
