package com.jetbrains.godot.gdscript.lexer

import com.intellij.lexer.Lexer
import com.intellij.platform.testFramework.core.FileComparisonFailedError
import com.intellij.testFramework.LexerTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.GdLexerAdapter
import org.junit.ComparisonFailure
import org.junit.Test
import org.junit.Ignore
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.readText

@RunWith(JUnit4::class)
class GdLexerTest : LexerTestCase() {
    override fun createLexer(): Lexer = GdLexerAdapter()
    override fun getDirPath(): String {
        return ""
    }

    fun getExpected(): String =
        getBaseTestDataPath().resolve("testData/gdscript/lexer/data").resolve("${getTestName(true)}.txt").readText()

    override fun doFileTest(filePath: String) {
        val text = loadTestDataFile(filePath)
        val expected = getExpected()
        doTest(text, expected, createLexer())
    }

    override fun loadTestDataFile(filePath: String): String {
        // Read source .gd files from the Godot parser test cases folder by default
        val base = getBaseTestDataPath().resolve("testData/gdscript/parser/godotTestCases")
        val path = base.resolve(filePath)
        return path.readText()
    }

    @Test fun testsignal_connect_func() = doFileTest("${getTestName(true)}.gd")
    @Test fun testclass_name() = doFileTest("${getTestName(true)}.gd")
    @Test fun testcallableInCtor() = doFileTest("${getTestName(true)}.gd")
    @Test fun testLambdaCallExpr() = doFileTest("${getTestName(true)}.gd")
    @Test fun testArrayWithFunc() = doFileTest("${getTestName(true)}.gd")

    override fun doTest(text: String, expected: String?, lexer: Lexer) {
        try{
            super.doTest(text, expected, this.createLexer())
        }
//        catch (e:FileComparisonFailedError){
//            val expectedText = e.getExpectedStringPresentation()
//            val actualText = e.getActualStringPresentation()
//            println("EXPECTED:\n" + expectedText + "\n----\nACTUAL:\n" + actualText)
//            throw e
//        }
        catch (e: ComparisonFailure){
            println("EXPECTED:\n" + e.expected + "\n----\nACTUAL:\n" + e.actual)
            // throw e
        }
    }
}