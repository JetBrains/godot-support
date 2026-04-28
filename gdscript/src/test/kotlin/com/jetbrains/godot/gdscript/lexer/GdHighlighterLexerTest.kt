package com.jetbrains.godot.gdscript.lexer

import com.intellij.lexer.Lexer
import com.intellij.platform.testFramework.core.FileComparisonFailedError
import com.intellij.testFramework.LexerTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.GdLexerHighlighterAdapter
import org.junit.ComparisonFailure
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class GdHighlighterLexerTest : LexerTestCase() {
    override fun createLexer(): Lexer = GdLexerHighlighterAdapter()
    override fun getDirPath(): String = ""

    override fun getExpectedFileExtension(): String = ".highlighter.lexer.txt"

    override fun getPathToTestDataFile(ext: String): String {
        val base = getBaseTestDataPath().resolve("testData/gdscript/parser/godotTestCases")
        val path = base.resolve(getTestName(true) + ext)
        return path.pathString
    }

    @Test fun testOnready_node_string_path() = doFileTest("gd")
    @Test fun testOnready_node_path_unclosed() = doFileTest("gd")
    @Test fun testPrint_string_unclosed() = doFileTest("gd")

    override fun doTest(text: String, expected: String?, lexer: Lexer) {
        try {
            super.doTest(text, expected, lexer)
        }
        catch (e: FileComparisonFailedError) {
            println("EXPECTED:\n" + e.getExpectedStringPresentation() + "\n----\nACTUAL:\n" + e.getActualStringPresentation())
            throw e
        }
        catch (e: ComparisonFailure) {
            println("EXPECTED:\n" + e.expected + "\n----\nACTUAL:\n" + e.actual)
            throw e
        }
    }
}
