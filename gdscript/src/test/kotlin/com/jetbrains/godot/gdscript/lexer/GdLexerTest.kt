package com.jetbrains.godot.gdscript.lexer

import com.intellij.lexer.Lexer
import com.intellij.openapi.util.text.StringUtil
import com.jetbrains.godot.GdCodeInsightTestBase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.GdLexerAdapter
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError
import java.io.File
import kotlin.io.path.pathString

class GdLexerTest : GdCodeInsightTestBase() {

    private fun createLexer(): Lexer = GdLexerAdapter()

    private fun getExpectedFileExtension(): String = ".lexer.txt"

    private fun getPathToTestDataFile(ext: String): String {
        val base = getBaseTestDataPath().resolve("testData/gdscript/parser/godotTestCases")
        return base.resolve(getTestName(true) + ext).pathString
    }

    /** Formats lexer token output in the same format as LexerTestCase.printTokens(). */
    private fun printTokens(text: String, lexer: Lexer): String {
        val sb = StringBuilder()
        lexer.start(text)
        while (lexer.tokenType != null) {
            val tokenType = lexer.tokenType.toString()
            val rawText = text.substring(lexer.tokenStart, lexer.tokenEnd)
            val escapedText = StringUtil.escapeStringCharacters(rawText)
            sb.append("$tokenType ('$escapedText')\n")
            lexer.advance()
        }
        return sb.toString()
    }

    private fun doFileTest(fileExt: String) {
        val sourcePath = getPathToTestDataFile(".$fileExt")
        val expectedPath = getPathToTestDataFile(getExpectedFileExtension())
        val text = File(sourcePath).readText()

        val actual = try {
            printTokens(text, createLexer())
        } catch (e: Exception) {
            throw RuntimeException("Lexer failed on $sourcePath", e)
        }

        val expectedFile = File(expectedPath)
        if (!expectedFile.exists()) {
            expectedFile.writeText(actual)
            throw AssertionError(
                "Golden file $expectedPath did not exist — created from actual output. Re-run the test."
            )
        }

        val expected = expectedFile.readText()
        if (expected != actual) {
            throw AssertionFailedError(
                "Lexer output differs for ${getTestName(true)} — expected file: $expectedPath",
                expected, actual
            )
        }
    }

    @Test fun testsignal_connect_func() = doFileTest("gd")
    @Disabled @Test fun testclass_name() = doFileTest("gd")
    @Test fun testcallableInCtor() = doFileTest("gd")
    @Test fun testLambdaCallExpr() = doFileTest("gd")
    @Disabled @Test fun testArrayWithFunc() = doFileTest("gd")
    @Test fun testlambda_callable_multiline() = doFileTest("gd")
}
