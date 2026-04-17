package com.jetbrains.godot.tscn.parser

import com.jetbrains.godot.getBaseTestDataPath
import org.junit.jupiter.api.Test
import kotlin.io.path.pathString

class TscnParserTest : TscnParsingTestBase(fileExt = "tscn") {
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
}
