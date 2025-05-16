package com.jetbrains.godot.tscn.parser

import com.intellij.testFramework.ParsingTestCase
import tscn.TscnParserDefinition

class TscnParserTest : ParsingTestCase("", "tscn", TscnParserDefinition()) {

    fun testHeader() = doTest(true)
    fun testDataLines() = doTest(true)
    fun testMultiParagraphs() = doTest(true)
    fun testMultilineText() = doTest(true)
    fun testSlashKey() = doTest(true)
    fun testJson() = doTest(true)

    override fun getTestDataPath(): String {
        return "testData/tscn/parser/data"
    }

    override fun skipSpaces(): Boolean {
        return false
    }

    override fun includeRanges(): Boolean {
        return true
    }
}
