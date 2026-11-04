package com.jetbrains.godot.gdscript.parser

import com.intellij.platform.testFramework.junit5.codeInsight.fixture.parsingFixture
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.testFramework.junit5.fixture.projectFixture
import com.jetbrains.godot.getBaseTestDataPath
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.GdParserDefinition
import org.junit.jupiter.api.Test
import kotlin.io.path.pathString

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Lexer and parser"])
@TestApplication
class GdParserTest {
    // https://docs.godotengine.org/en/stable/tutorials/scripting/gdscript/gdscript_basics.html

    private val projectFixture = projectFixture()
    private val parser by parsingFixture(
        projectFixture,
        getBaseTestDataPath(GdParserTest::class.java).resolve("testData/gdscript/parser/data").pathString,
        "gd",
        false,
        GdParserDefinition(),
    )

    @Test fun testExtension() = parser.doTest(true)
    @Test fun testExtensionExt() = parser.doTest(true)
    @Test fun testClassName() = parser.doTest(true)
    @Test fun testAnnotation() = parser.doTest(true)
    @Test fun testAnnotationParam() = parser.doTest(true)
    @Test fun testClassConst() = parser.doTest(true)
    @Test fun testClassVar() = parser.doTest(true)
    @Test fun testClassVarAs() = parser.doTest(true)
    @Test fun testClassVarSetGet() = parser.doTest(true)
    @Test fun testClassVarGetParentheses() = parser.doTest(true, true)
    @Test fun testClassVarSetGetMethod() = parser.doTest(true)
    @Test fun testClassVarSetGetMethodMultiline() = parser.doTest(true)
    @Test fun testTypedArray() = parser.doTest(true)
    @Test fun testTypedDictionary() = parser.doTest(true)
    @Test fun testSignal() = parser.doTest(true)
    @Test fun testEnumAnonymous() = parser.doTest(true)
    @Test fun testMethodVararg() = parser.doTest(true)
    @Test fun testMethodArrayReturnType() = parser.doTest(true)
    @Test fun testClass() = parser.doTest(true)
    @Test fun testCommentWithinCode() = parser.doTest(true)

    // Stmts
    @Test fun testAnnotationInner() = parser.doTest(true)
    @Test fun testAssignStmt() = parser.doTest(true)
    @Test fun testVarConstStmt() = parser.doTest(true)
    @Test fun testVarConstWithEmptyStmt() = parser.doTest(true)
    @Test fun testIfStmt() = parser.doTest(true)
    @Test fun testWhileStmt() = parser.doTest(true)
    @Test fun testForStmt() = parser.doTest(true)
    @Test fun testMatchStmt() = parser.doTest(true)
    @Test fun testMatchStmt2() = parser.doTest(true)
    @Test fun testMatchWhen() = parser.doTest(true)
    @Test fun testMatchEmptyDictPattern() = parser.doTest(true)

    // Exprs
    @Test fun testArrayExpr() = parser.doTest(true)
    @Test fun testCastExpr() = parser.doTest(true)
    @Test fun testTernaryExpr() = parser.doTest(true)
    @Test fun testLogicExpr() = parser.doTest(true)
    @Test fun testNegateExpr() = parser.doTest(true)
    @Test fun testInExpr() = parser.doTest(true)
    @Test fun testComparisonExpr() = parser.doTest(true)
    @Test fun testBitExpr() = parser.doTest(true)
    @Test fun testShiftExpr() = parser.doTest(true)
    @Test fun testPlusExpr() = parser.doTest(true)
    @Test fun testFactorExpr() = parser.doTest(true)
    @Test fun testSignExpr() = parser.doTest(true)
    @Test fun testBitNotExpr() = parser.doTest(true)
    @Test fun testIsExpr() = parser.doTest(true)
    @Test fun testCallExpr() = parser.doTest(true)
    @Test fun testAwaitExpr() = parser.doTest(true)
    @Test fun testAttributeExpr() = parser.doTest(true)
    @Test fun testFuncDeclExpr() = parser.doTest(true)
    @Test fun testPercentUniqueName() = parser.doTest(true)
    @Test fun testPercentUniqueNameAfterAssignment() = parser.doTest(true)

    @Test fun testFuncDeclExprExt() = parser.doTest(true)
    @Test fun testFuncDeclExprParam() = parser.doTest(true)
    @Test fun testlambda_nested() = parser.doTest(true, true)
    @Test fun testlambda_inline_close_paren() = parser.doTest(true, true)
    @Test fun testlambda_inline_close_paren_newline() = parser.doTest(true, true)
    @Test fun testlambda_inline_nested() = parser.doTest(true, true)
    @Test fun testLambdaArrayTailFileLevelClose() = parser.doTest(true, true)
    @Test fun testLambdaArrayTailBodyColumnClose() = parser.doTest(true, true)
    @Test fun testLambdaArrayTailNestedSuite() = parser.doTest(true, true)
    @Test fun testLambdaArrayTailNestedCollection() = parser.doTest(true, true)
    @Test fun testLambdaArrayTailTrailingComma() = parser.doTest(true, true)
    @Test fun testLambdaArrayTailEofClose() = parser.doTest(true, true)
    @Test fun testLambdaDictTailFileLevelClose() = parser.doTest(true, true)
    @Test fun testLambdaDictTailBodyColumnClose() = parser.doTest(true, true)
    @Test fun testLambdaDictTailNestedSuite() = parser.doTest(true, true)
    @Test fun testLambdaDictTailNestedCollection() = parser.doTest(true, true)
    @Test fun testLambdaDictTailTrailingComma() = parser.doTest(true, true)
    @Test fun testLambdaArrayUnterminated() = parser.doTest(true)
    @Test fun testLambdaDictUnterminated() = parser.doTest(true)
    @Test fun testLambdaCallVarTail() = parser.doTest(true, true)
    @Test fun testLambdaCallClassVarTail() = parser.doTest(true, true)
    @Test fun testLambdaCallEmptyBody() = parser.doTest(true)
    @Test fun testLambdaCallUnterminated() = parser.doTest(true)
    @Test fun testLambdaCallCommaUnterminated() = parser.doTest(true)
    @Test fun testNestedCallExpr() = parser.doTest(true)
    @Test fun testPrimaryBracketExpr() = parser.doTest(true)
    @Test fun testDictDeclExpr() = parser.doTest(true)
}
