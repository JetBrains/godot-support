package tscn.parser

import com.intellij.testFramework.ParsingTestCase
import org.junit.jupiter.api.Test
import tscn.TscnParserDefinition

class TscnRecoveryParserTest : ParsingTestCase {

    constructor(): super("", "tscn", TscnParserDefinition())

    @Test fun testHeader() = doTest(true)

    override fun getTestDataPath(): String {
        return "src/test/kotlin/tscn/parser/recoveryData"
    }

    override fun skipSpaces(): Boolean {
        return false
    }

    override fun includeRanges(): Boolean {
        return true
    }

}
