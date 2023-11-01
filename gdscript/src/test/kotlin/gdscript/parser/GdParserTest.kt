package gdscript.parser

import com.intellij.testFramework.ParsingTestCase
import gdscript.GdNewParserDefinition
import gdscript.GdParserDefinition
import org.junit.jupiter.api.Test

class GdParserTest : ParsingTestCase {

    // https://docs.godotengine.org/en/stable/tutorials/scripting/gdscript/gdscript_basics.html

    constructor(): super("", "gd", GdNewParserDefinition())
//    constructor(): super("", "gd", GdParserDefinition())

    @Test fun testClassName() = doTest(true)
    @Test fun testExtension() = doTest(true)
    @Test fun testExtensionExt() = doTest(true)
    @Test fun testAnnotation() = doTest(true)
    @Test fun testAnnotationParam() = doTest(true)
    @Test fun testAnnotationError() = doTest(true)
    @Test fun testClassConst() = doTest(true)
    @Test fun testClassVar() = doTest(true)
    // TODO více řádkové
    @Test fun testClassVarSetGetMethod() = doTest(true)

    @Test fun testSignal() = doTest(true)
    @Test fun testEnumAnonymous() = doTest(true)
    @Test fun testEnumNamed() = doTest(true)
    @Test fun testFunctionSimple() = doTest(true)

//    @Test fun testFunction() = doTest(true)
//    @Test fun testClassVarSetGet() = doTest(true)

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
