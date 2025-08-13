package com.jetbrains.godot.tscn.parser

import com.intellij.testFramework.ParsingTestCase
import com.jetbrains.godot.getBaseTestDataPath
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import tscn.TscnParserDefinition
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class TscnParserTest : ParsingTestCase("", "tscn", TscnParserDefinition()) {
    @Test fun testHeader() = doTest(true)
    @Test fun testDataLines() = doTest(true)
    @Test fun testMultiParagraphs() = doTest(true)
    @Test fun testMultilineText() = doTest(true)
    @Test fun testSlashKey() = doTest(true)
    @Test fun testJson() = doTest(true)

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/tscn/parser/data").pathString
    }

    override fun skipSpaces(): Boolean {
        return false
    }

    override fun includeRanges(): Boolean {
        return true
    }
}
