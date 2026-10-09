package com.jetbrains.godot.gdscript.lexer

import com.intellij.platform.testFramework.junit5.codeInsight.fixture.lexerFixture
import com.intellij.testFramework.junit5.fixture.TestFixtures
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.GdLexerAdapter
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import kotlin.io.path.pathString

@TestFixtures
class GdLexerTest {
    private val lexer by lexerFixture(
        getBaseTestDataPath(GdLexerTest::class.java).resolve("testData/gdscript/parser/godotTestCases").pathString,
        ".lexer.txt",
    ) { GdLexerAdapter() }

    @Test fun testOnready_node_string_path() = lexer.doFileTest("gd")
    @Test fun testScratch() = lexer.doFileTest("gd")

    @Test fun testsignal_connect_func() = lexer.doFileTest("gd")
    @Disabled @Test fun testclass_name() = lexer.doFileTest("gd")
    @Test fun testcallableInCtor() = lexer.doFileTest("gd")
    @Test fun testLambdaCallExpr() = lexer.doFileTest("gd")
    @Disabled @Test fun testArrayWithFunc() = lexer.doFileTest("gd")
    @Test fun testlambda_callable_multiline() = lexer.doFileTest("gd")
    @Test fun testlambdaAsArgument() = lexer.doFileTest("gd")
    @Test fun testlambdaAsArgumentReturnLastArg() = lexer.doFileTest("gd")
    @Test fun testlambdaAsArgumentReturnCommaNextLine() = lexer.doFileTest("gd")
    @Test fun testlambdaAsArgumentReturnMultiStmt() = lexer.doFileTest("gd")
    @Test fun testlambdaAsArgumentReturnNestedIf() = lexer.doFileTest("gd")
    @Test fun testlambdaAsArgumentNestedIfElse() = lexer.doFileTest("gd")
    @Test fun testsemicolon_as_end_statement() = lexer.doFileTest("gd")
    @Test fun testsemicolon_as_terminator() = lexer.doFileTest("gd")
    @Test fun testsemicolon_separated_statements_one_line() = lexer.doFileTest("gd")
    @Test fun testsemicolon_multiple_after_statement_suite() = lexer.doFileTest("gd")
    @Test fun testsemicolon_multiple_alone_suite() = lexer.doFileTest("gd")
    @Test fun testsemicolon_multiple_on_one_line() = lexer.doFileTest("gd")

    @Test
    fun testQuotedNodePathAcceptsAnyNodeName() {
        assertAll(
            { assertLexesTo("%\"Main-Menu\"", "NODE_PATH_LEX ('%\"Main-Menu\"')") },
            { assertLexesTo("\$\"Ünïcode (2)\"", "NODE_PATH_LEX ('\$\"Ünïcode (2)\"')") },
            // the unquoted form must stay narrow, so that `-` keeps being an operator
            { assertLexesTo("%Main-Menu", "NODE_PATH_LEX ('%Main')") },
            { assertLexesTo("%Main-Menu", "MINUS ('-')") },
            { assertLexesTo("%\"Main Menu\"", "NODE_PATH_LEX ('%\"Main Menu\"')") },
            { assertLexesTo("%fo%o", "NODE_PATH_LEX ('%fo')") },
            { assertLexesTo("%fo%o", "NODE_PATH_LEX ('%o')") },
            { assertLexesTo("%\"fo\".size() % %\'o\'.size()", "MOD ('%')") },
        )
    }

    private fun assertLexesTo(text: String, expectedToken: String) {
        val tokens = lexer.printTokens(text, 0)
        assertTrue(tokens.contains(expectedToken), "`$text` should lex to $expectedToken but was:\n$tokens")
    }
}