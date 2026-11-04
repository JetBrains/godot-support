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
class GdRecoveryParserTest {
    private val projectFixture = projectFixture()
    private val parser by parsingFixture(
        projectFixture,
        getBaseTestDataPath(GdRecoveryParserTest::class.java).resolve("testData/gdscript/parser/recoveryData").pathString,
        "gd",
        false,
        GdParserDefinition(),
    )

    @Test fun testInheritance() = parser.doTest(true)
    @Test fun testClassName() = parser.doTest(true)
    @Test fun testAnnotation() = parser.doTest(true)
    @Test fun testClassConst() = parser.doTest(true)
    @Test fun testClassVar() = parser.doTest(true)
    @Test fun testClassVarSetGet() = parser.doTest(true)
    @Test fun testClassVarSetGetMethod() = parser.doTest(true)
    @Test fun testClassVarSetGetMethodMultiline() = parser.doTest(true)
    @Test fun testSignal() = parser.doTest(true)
    @Test fun testEnumAnonymous() = parser.doTest(true)
    @Test fun testEnumNamed() = parser.doTest(true)
    @Test fun testMethod() = parser.doTest(true)
    @Test fun testClass() = parser.doTest(true)

    // Stmts
//    @Test fun testAnnotationInner() = parser.doTest(true)
    @Test fun testAssignStmt() = parser.doTest(true)
    @Test fun testVarConstStmt() = parser.doTest(true)
    @Test fun testVarConstStmt2() = parser.doTest(true)
    @Test fun testIfStmt() = parser.doTest(true)
    @Test fun testIfElifStmt() = parser.doTest(true)
    @Test fun testIfElseStmt() = parser.doTest(true)
    @Test fun testWhileStmt() = parser.doTest(true)
    @Test fun testWhileStmt2() = parser.doTest(true)
    @Test fun testForStmt() = parser.doTest(true)
    @Test fun testMatchStmt() = parser.doTest(true)
    @Test fun testMatchStmt2() = parser.doTest(true)
    @Test fun testReturnStmt() = parser.doTest(true)

    // Exprs
    @Test fun testArrayExpr() = parser.doTest(true)
    @Test fun testAttributeExpr() = parser.doTest(true)
    @Test fun testAwaitExpr() = parser.doTest(true)
    @Test fun testBitExpr() = parser.doTest(true)

    @Test fun testInExpr() = parser.doTest(true)
    @Test fun testIsExpr() = parser.doTest(true)
    @Test fun testLogicExpr() = parser.doTest(true)
    @Test fun testNegateExpr() = parser.doTest(true)
    @Test fun testPlusExpr() = parser.doTest(true)
    @Test fun testShiftExpr() = parser.doTest(true)
    @Test fun testTernaryExpr() = parser.doTest(true)
}
