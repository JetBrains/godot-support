package gdscript.parser

import com.intellij.testFramework.ParsingTestCase
import org.junit.jupiter.api.Test

class GdParserTest : ParsingTestCase {

    // https://docs.godotengine.org/en/stable/tutorials/scripting/gdscript/gdscript_basics.html

    constructor(): super("", "gd", gdscript.GdNewParserDefinition())
//    constructor(): super("", "gd", gdscript.GdParserDefinition())

    @Test fun testClassName() = doTest(true)
    @Test fun testExtension() = doTest(true)
    @Test fun testExtensionExt() = doTest(true)
    @Test fun testAnnotation() = doTest(true)
    @Test fun testAnnotationParam() = doTest(true)
    @Test fun testAnnotationError() = doTest(true)
    @Test fun testClassConst() = doTest(true)
    @Test fun testClassVar() = doTest(true)
    // @Test fun testClassVarSetGet() = doTest(true)
    // TODO více řádkové
    @Test fun testClassVarSetGetMethod() = doTest(true)

    @Test fun testSignal() = doTest(true)
    @Test fun testEnumAnonymous() = doTest(true)
    @Test fun testEnumNamed() = doTest(true)

    // Stmts
    @Test fun testFunctionSimple() = doTest(true)
    @Test fun testVarConstStmt() = doTest(true)
    @Test fun testVarConstWithEmptyStmt() = doTest(true)
    @Test fun testIfStmt() = doTest(true)
    @Test fun testWhileStmt() = doTest(true)
    @Test fun testForStmt() = doTest(true)
    @Test fun testMatchStmt() = doTest(true)
    @Test fun testAwaitStmt() = doTest(true)
    @Test fun testAssignStmt() = doTest(true)

    // Exprs
    @Test fun testNestedCallExpr() = doTest(true)
    @Test fun testArrayExpr() = doTest(true)
    @Test fun testCastExpr() = doTest(true)
    // TODO ternary
    @Test fun testLogicExpr() = doTest(true)
    @Test fun testNegateExpr() = doTest(true)
    @Test fun testInExpr() = doTest(true)
    @Test fun testComparisonExpr() = doTest(true)

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
