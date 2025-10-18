package com.jetbrains.godot.gdscript.lexer

import com.intellij.lexer.Lexer
import com.intellij.platform.testFramework.core.FileComparisonFailedError
import com.intellij.testFramework.LexerTestCase
import com.intellij.testFramework.fixtures.IdeaTestExecutionPolicy
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.GdLexerAdapter
import org.junit.ComparisonFailure
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString
import kotlin.io.path.readText

@RunWith(JUnit4::class)
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
    @Ignore @Test fun testclass_name() = doFileTest("gd")
    @Test fun testcallableInCtor() = doFileTest("gd")
    @Test fun testLambdaCallExpr() = doFileTest("gd")
    @Ignore @Test fun testArrayWithFunc() = doFileTest("gd")

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
        catch (e: ComparisonFailure){
            println("EXPECTED:\n" + e.expected + "\n----\nACTUAL:\n" + e.actual)
            throw e
        }
    }
}