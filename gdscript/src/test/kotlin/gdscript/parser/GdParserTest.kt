package gdscript.parser

import com.intellij.testFramework.ParsingTestCase
import gdscript.GdNewParserDefinition
import org.junit.jupiter.api.Test

class GdParserTest : ParsingTestCase {

    constructor(): super("", "gd", GdNewParserDefinition())

    @Test fun testClassName() = doTest(true)
    @Test fun testExtension() = doTest(true)
    @Test fun testClassVar() = doTest(true)
    @Test fun testClassConst() = doTest(true)
    @Test fun testFunction() = doTest(true)

    override fun getTestDataPath(): String {
        return "src/test/kotlin/gdscript/parser/data"
    }

    override fun skipSpaces(): Boolean {
        return false
    }

    override fun includeRanges(): Boolean {
        return true
    }

}
