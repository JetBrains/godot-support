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
    @Test fun testClassVarSetGet() = doTest(true)
    @Test fun testClassVarSetGetMethod() = doTest(true)
    @Test fun testClassVarSetGetMethodMultiline() = doTest(true)
    @Test fun testSignal() = doTest(true)
    @Test fun testEnumAnonymous() = doTest(true)
    @Test fun testEnumNamed() = doTest(true)
    @Test fun testMethod() = doTest(true)
    @Test fun testClass() = doTest(true)

    // Stmts
    @Test fun testAssignStmt() = doTest(true)
    @Test fun testVarConstStmt() = doTest(true)
    @Test fun testIfStmt() = doTest(true)
    @Test fun testIfElifStmt() = doTest(true)
    @Test fun testIfElseStmt() = doTest(true)
    @Test fun testWhileStmt() = doTest(true)
    @Test fun testWhileStmt2() = doTest(true)
    @Test fun testForStmt() = doTest(true)
    @Test fun testMatchStmt() = doTest(true)
    @Test fun testMatchStmt2() = doTest(true)

    // Exprs
    @Test fun testArrayExpr() = doTest(true)
    @Test fun testAttributeExpr() = doTest(true)
    @Test fun testAwaitExpr() = doTest(true)
    @Test fun testBitExpr() = doTest(true)

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
