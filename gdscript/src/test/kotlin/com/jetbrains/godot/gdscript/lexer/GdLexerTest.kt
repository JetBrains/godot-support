package com.jetbrains.godot.gdscript.lexer

import com.intellij.lexer.Lexer
import com.intellij.platform.testFramework.core.FileComparisonFailedError
import com.intellij.testFramework.LexerTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.GdLexerAdapter
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.opentest4j.AssertionFailedError
import kotlin.io.path.pathString

class GdLexerTest : LexerTestCase() {
    override fun createLexer(): Lexer = GdLexerAdapter()
    override fun getDirPath(): String {
        return ""
    }

    override fun getExpectedFileExtension(): String {
        return ".lexer.txt"
    }

    override fun getPathToTestDataFile(ext: String): String {
        val base = getBaseTestDataPath().resolve("testData/gdscript/parser/godotTestCases")
        val path = base.resolve(getTestName(true) + ext)
        return path.pathString
    }

    @Test fun testsignal_connect_func() = doFileTest("gd")
    @Disabled @Test fun testclass_name() = doFileTest("gd")
    @Test fun testcallableInCtor() = doFileTest("gd")
    @Test fun testLambdaCallExpr() = doFileTest("gd")
    @Disabled @Test fun testArrayWithFunc() = doFileTest("gd")
    @Test fun testlambda_callable_multiline() = doFileTest("gd")

    override fun doTest(text: String, expected: String?, lexer: Lexer) {
        try{
            super.doTest(text, expected, this.createLexer())
        }
        catch (e:FileComparisonFailedError){
            val expectedText = e.getExpectedStringPresentation()
            val actualText = e.getActualStringPresentation()
            println("EXPECTED:\n" + expectedText + "\n----\nACTUAL:\n" + actualText)
            throw e
        }
        catch (e: AssertionFailedError){
            println("EXPECTED:\n" + e.expected?.stringRepresentation + "\n----\nACTUAL:\n" + e.actual?.stringRepresentation)
            throw e
        }
    }
}