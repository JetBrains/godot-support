package com.jetbrains.godot.tscn.parser

import com.intellij.testFramework.ParsingTestCase
import com.jetbrains.godot.getBaseTestDataPath
import org.junit.jupiter.api.Test
import tscn.TscnParserDefinition
import kotlin.io.path.pathString

class TscnParserTest : ParsingTestCase("", "tscn", TscnParserDefinition()) {
    @Test fun testHeader() = doTest(true)
    @Test fun testDataLines() = doTest(true)
    @Test fun testMultiParagraphs() = doTest(true)
    @Test fun testTypedArrayWithExtResource() = doTest(true)
    @Test fun testTypedDictionaryWithExtResource() = doTest(true)
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
