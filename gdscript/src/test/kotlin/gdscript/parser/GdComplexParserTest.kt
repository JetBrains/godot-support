package gdscript.parser

import com.intellij.testFramework.ParsingTestCase
import org.junit.jupiter.api.Test

class GdComplexParserTest : ParsingTestCase {

    constructor(): super("", "gd", gdscript.GdNewParserDefinition())

    @Test fun testSingleLineEnum() = doTest(true)

    override fun getTestDataPath(): String {
        return "src/test/kotlin/gdscript/parser/complexData"
    }

    override fun skipSpaces(): Boolean {
        return false
    }

    override fun includeRanges(): Boolean {
        return true
    }

}
