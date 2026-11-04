package com.jetbrains.godot.tscn.parser

import com.intellij.platform.testFramework.junit5.codeInsight.fixture.parsingFixture
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.testFramework.junit5.fixture.projectFixture
import com.jetbrains.godot.getBaseTestDataPath
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tscn.TscnParserDefinition
import kotlin.io.path.pathString

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["Scenes and resources/Parser"])
@TestApplication
class TresParserTest {
    private val projectFixture = projectFixture()
    private val parser by parsingFixture(
        projectFixture,
        getBaseTestDataPath(TresParserTest::class.java).resolve("testData/tscn/parser/data").pathString,
        "tres",
        false,
        TscnParserDefinition(),
    )

    @BeforeEach fun setUp() {
        parser.includeRanges = true
    }

    @Test fun testResourceWithTypedCollections() = parser.doTest(true)
}
