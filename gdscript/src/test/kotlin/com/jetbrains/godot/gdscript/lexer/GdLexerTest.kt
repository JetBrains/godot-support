package com.jetbrains.godot.gdscript.lexer

import com.intellij.lexer.Lexer
import com.intellij.testFramework.LexerTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.GdLexerAdapter
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.readText

@RunWith(JUnit4::class)
class GdLexerTest : LexerTestCase() {
    override fun createLexer(): Lexer = GdLexerAdapter()
    override fun getDirPath(): String {
        return ""
    }

    fun getText(): String = getBaseTestDataPath().resolve("testData/gdscript/parser/data").resolve("${getTestName(true)}.gd").readText()

    @Test
//    @Ignore("how to point it on where to dump results?")
    fun testlambda_nested() = doTest(getText())


    @Test
    @Ignore("how to point it on where to dump results?")
    fun testLambdaCallExpr() = doTest(getText())

    @Test
    @Ignore("how to point it on where to dump results?")
    fun testcallableInCtor() = doTest(getText())
}
