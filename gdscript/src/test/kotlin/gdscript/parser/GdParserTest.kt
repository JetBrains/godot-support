package gdscript.parser

import com.intellij.testFramework.ParsingTestCase
import gdscript.GdParserDefinition
import org.junit.jupiter.api.Test

class GdParserTest : ParsingTestCase {

    constructor(): super("", "gd", GdParserDefinition())

    @Test
    fun testClassName() = doTest(true)

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
