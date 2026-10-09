package com.jetbrains.godot.gdscript.lexer

import com.intellij.platform.testFramework.junit5.codeInsight.fixture.lexerFixture
import com.intellij.testFramework.junit5.fixture.TestFixtures
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.GdLexerHighlighterAdapter
import org.junit.jupiter.api.Test
import kotlin.io.path.pathString

@TestFixtures
class GdHighlighterLexerTest {
    private val lexer by lexerFixture(
        getBaseTestDataPath(GdHighlighterLexerTest::class.java).resolve("testData/gdscript/parser/godotTestCases").pathString,
        ".highlighter.lexer.txt",
    ) { GdLexerHighlighterAdapter() }

    @Test fun testOnready_node_string_path() = lexer.doFileTest("gd")
    @Test fun testOnready_node_path_unclosed() = lexer.doFileTest("gd")
    @Test fun testPrint_string_unclosed() = lexer.doFileTest("gd")
}
