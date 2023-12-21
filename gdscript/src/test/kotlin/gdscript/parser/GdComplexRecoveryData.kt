package gdscript.parser

import com.intellij.testFramework.ParsingTestCase
import org.junit.jupiter.api.Test

class GdComplexRecoveryData : ParsingTestCase {

    constructor(): super("", "gd", gdscript.GdNewParserDefinition())

    @Test fun testLambda() = doTest(true)
    @Test fun testLambda2() = doTest(true)

    override fun getTestDataPath(): String {
        return "src/test/kotlin/gdscript/parser/complexRecoveryData"
    }

    override fun skipSpaces(): Boolean {
        return false
    }

    override fun includeRanges(): Boolean {
        return true
    }
}
