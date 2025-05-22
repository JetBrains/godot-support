package com.jetbrains.godot.parser

import com.intellij.testFramework.ParsingTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.GdParserDefinition
import kotlin.io.path.pathString


class GdParserTest : ParsingTestCase("", "gd", GdParserDefinition()) {

    // https://docs.godotengine.org/en/stable/tutorials/scripting/gdscript/gdscript_basics.html

    fun testExtension() = doTest(true)
    fun testExtensionExt() = doTest(true)
    fun testClassName() = doTest(true)
    fun testAnnotation() = doTest(true)
    fun testAnnotationParam() = doTest(true)
    fun testClassConst() = doTest(true)
    fun testClassVar() = doTest(true)
    fun testClassVarAs() = doTest(true)
    fun testClassVarSetGet() = doTest(true)
    fun testClassVarSetGetMethod() = doTest(true)
    fun testClassVarSetGetMethodMultiline() = doTest(true)
    fun testSignal() = doTest(true)
    fun testEnumAnonymous() = doTest(true)
    fun testEnumNamed() = doTest(true)
    fun testFunctionSimple() = doTest(true)
    fun testMethodVararg() = doTest(true)
    fun testMethodArrayReturnType() = doTest(true)
    fun testClass() = doTest(true)
    fun testCommentWithinCode() = doTest(true)

    // Stmts
    fun testAnnotationInner() = doTest(true)
    fun testAssignStmt() = doTest(true)
    fun testVarConstStmt() = doTest(true)
    fun testVarConstWithEmptyStmt() = doTest(true)
    fun testIfStmt() = doTest(true)
    fun testWhileStmt() = doTest(true)
    fun testForStmt() = doTest(true)
    fun testMatchStmt() = doTest(true)
    fun testMatchStmt2() = doTest(true)

    // Exprs
    fun testArrayExpr() = doTest(true)
    fun testCastExpr() = doTest(true)
    fun testTernaryExpr() = doTest(true)
    fun testLogicExpr() = doTest(true)
    fun testNegateExpr() = doTest(true)
    fun testInExpr() = doTest(true)
    fun testComparisonExpr() = doTest(true)
    fun testBitExpr() = doTest(true)
    fun testShiftExpr() = doTest(true)
    fun testPlusExpr() = doTest(true)
    fun testFactorExpr() = doTest(true)
    fun testSignExpr() = doTest(true)
    fun testBitNotExpr() = doTest(true)
    fun testIsExpr() = doTest(true)
    fun testCallExpr() = doTest(true)
    fun testAwaitExpr() = doTest(true)
    fun testAttributeExpr() = doTest(true)
    fun testFuncDeclExpr() = doTest(true)

    fun testFuncDeclExprExt() = doTest(true)
    fun testFuncDeclExprParam() = doTest(true)
    fun testLambdaCallExpr() = doTest(true)
    fun testNestedCallExpr() = doTest(true)
    fun testPrimaryBracketExpr() = doTest(true)
    fun testDictDeclExpr() = doTest(true)

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/parser/data").pathString
    }

    override fun skipSpaces(): Boolean {
        return false
    }

    override fun includeRanges(): Boolean {
        return true
    }

}
