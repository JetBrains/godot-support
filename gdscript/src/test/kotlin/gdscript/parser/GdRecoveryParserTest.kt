package gdscript.parser

import com.intellij.testFramework.ParsingTestCase
import org.junit.jupiter.api.Test

class GdRecoveryParserTest : ParsingTestCase {

    constructor(): super("", "gd", gdscript.GdNewParserDefinition())

    @Test fun testInheritance() = doTest(true)
    @Test fun testClassName() = doTest(true)
    @Test fun testAnnotation() = doTest(true)
    @Test fun testClassConst() = doTest(true)
    @Test fun testClassVar() = doTest(true)

    override fun getTestDataPath(): String {
        return "src/test/kotlin/gdscript/parser/recoveryData"
    }

    override fun skipSpaces(): Boolean {
        return false
    }

    override fun includeRanges(): Boolean {
        return true
    }

}
