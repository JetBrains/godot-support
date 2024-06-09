package gdscript.parser

import com.intellij.testFramework.ParsingTestCase
import org.junit.jupiter.api.Test

class GdComplexParserTest : ParsingTestCase {

    constructor(): super("", "gd", gdscript.GdParserDefinition())

    @Test fun testAttributes() = doTest(true)
    @Test fun testSingleLineEnum() = doTest(true)
    @Test fun testBoolWithComparison() = doTest(true)
    @Test fun testNestedLoops() = doTest(true)
    @Test fun testNestedLambda() = doTest(true)

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
