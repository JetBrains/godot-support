package com.jetbrains.godot.gdscript.parser

import com.intellij.platform.testFramework.junit5.codeInsight.fixture.parsingFixture
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.testFramework.junit5.fixture.projectFixture
import com.jetbrains.godot.getBaseTestDataPath
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.GdParserDefinition
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.io.path.pathString

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Lexer and parser"])
@TestApplication
class GdComplexRecoveryDataTest {
    private val projectFixture = projectFixture()
    private val parser by parsingFixture(
        projectFixture,
        getBaseTestDataPath(GdComplexRecoveryDataTest::class.java).resolve("testData/gdscript/parser/complexRecoveryData").pathString,
        "gd",
        false,
        GdParserDefinition(),
    )

    @Disabled("Hard to work with non completely valid test data")
    @Test fun testLambda() = parser.doTest(true)
    @Disabled("Hard to work with non completely valid test data")
    @Test fun testLambda2() = parser.doTest(true)
}
