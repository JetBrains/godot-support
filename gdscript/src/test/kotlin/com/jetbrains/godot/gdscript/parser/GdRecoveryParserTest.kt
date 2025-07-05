package com.jetbrains.godot.gdscript.parser

import com.intellij.testFramework.ParsingTestCase
import com.jetbrains.godot.getBaseTestDataPath
import com.jetbrains.rider.test.framework.TestProcessorRule
import gdscript.GdParserDefinition
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class GdRecoveryParserTest : ParsingTestCase("", "gd", GdParserDefinition()) {
    @Rule @JvmField val testProcessorRule: TestProcessorRule = TestProcessorRule()

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
//    @Test fun testAnnotationInner() = doTest(true)
    @Test fun testAssignStmt() = doTest(true)
    @Test fun testVarConstStmt() = doTest(true)
    @Test fun testVarConstStmt2() = doTest(true)
    @Test fun testIfStmt() = doTest(true)
    @Test fun testIfElifStmt() = doTest(true)
    @Test fun testIfElseStmt() = doTest(true)
    @Test fun testWhileStmt() = doTest(true)
    @Test fun testWhileStmt2() = doTest(true)
    @Test fun testForStmt() = doTest(true)
    @Test fun testMatchStmt() = doTest(true)
    @Test fun testMatchStmt2() = doTest(true)
    @Test fun testReturnStmt() = doTest(true)

    // Exprs
    @Test fun testArrayExpr() = doTest(true)
    @Test fun testAttributeExpr() = doTest(true)
    @Test fun testAwaitExpr() = doTest(true)
    @Test fun testBitExpr() = doTest(true)

    @Test fun testInExpr() = doTest(true)
    @Test fun testIsExpr() = doTest(true)
    @Test fun testLogicExpr() = doTest(true)
    @Test fun testNegateExpr() = doTest(true)
    @Test fun testPlusExpr() = doTest(true)
    @Test fun testShiftExpr() = doTest(true)
    @Test fun testTernaryExpr() = doTest(true)

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/parser/recoveryData").pathString
    }

    override fun skipSpaces(): Boolean {
        return false
    }

    override fun includeRanges(): Boolean {
        return true
    }

}
