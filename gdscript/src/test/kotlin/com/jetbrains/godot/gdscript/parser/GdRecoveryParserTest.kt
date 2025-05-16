package com.jetbrains.godot.parser

import com.intellij.testFramework.ParsingTestCase
import gdscript.GdParserDefinition

class GdRecoveryParserTest : ParsingTestCase("", "gd", GdParserDefinition()) {

    fun testInheritance() = doTest(true)
    fun testClassName() = doTest(true)
    fun testAnnotation() = doTest(true)
    fun testClassConst() = doTest(true)
    fun testClassVar() = doTest(true)
    fun testClassVarSetGet() = doTest(true)
    fun testClassVarSetGetMethod() = doTest(true)
    fun testClassVarSetGetMethodMultiline() = doTest(true)
    fun testSignal() = doTest(true)
    fun testEnumAnonymous() = doTest(true)
    fun testEnumNamed() = doTest(true)
    fun testMethod() = doTest(true)
    fun testClass() = doTest(true)

    // Stmts
//    fun testAnnotationInner() = doTest(true)
    fun testAssignStmt() = doTest(true)
    fun testVarConstStmt() = doTest(true)
    fun testVarConstStmt2() = doTest(true)
    fun testIfStmt() = doTest(true)
    fun testIfElifStmt() = doTest(true)
    fun testIfElseStmt() = doTest(true)
    fun testWhileStmt() = doTest(true)
    fun testWhileStmt2() = doTest(true)
    fun testForStmt() = doTest(true)
    fun testMatchStmt() = doTest(true)
    fun testMatchStmt2() = doTest(true)
    fun testReturnStmt() = doTest(true)

    // Exprs
    fun testArrayExpr() = doTest(true)
    fun testAttributeExpr() = doTest(true)
    fun testAwaitExpr() = doTest(true)
    fun testBitExpr() = doTest(true)

    fun testInExpr() = doTest(true)
    fun testIsExpr() = doTest(true)
    fun testLogicExpr() = doTest(true)
    fun testNegateExpr() = doTest(true)
    fun testPlusExpr() = doTest(true)
    fun testShiftExpr() = doTest(true)
    fun testTernaryExpr() = doTest(true)

    override fun getTestDataPath(): String {
        return "testData/gdscript/parser/recoveryData"
    }

    override fun skipSpaces(): Boolean {
        return false
    }

    override fun includeRanges(): Boolean {
        return true
    }

}
