package tscn.parser

import com.intellij.testFramework.ParsingTestCase
import org.junit.jupiter.api.Test
import tscn.TscnParserDefinition

class TscnParserTest : ParsingTestCase {

    constructor(): super("", "tscn", TscnParserDefinition())

    @Test fun testHeader() = doTest(true)
    @Test fun testDataLines() = doTest(true)
    @Test fun testMultiParagraphs() = doTest(true)
    @Test fun testMultilineText() = doTest(true)
    @Test fun testSlashKey() = doTest(true)
    @Test fun testJson() = doTest(true)

    override fun getTestDataPath(): String {
        return "src/test/kotlin/tscn/parser/data"
    }

    override fun skipSpaces(): Boolean {
        return false
    }

    override fun includeRanges(): Boolean {
        return true
    }

}
