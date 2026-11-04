package com.jetbrains.godot.tscn.parser

import com.intellij.openapi.application.runReadAction
import com.intellij.platform.testFramework.junit5.codeInsight.fixture.parsingFixture
import com.intellij.testFramework.junit5.TestApplication
import com.intellij.testFramework.junit5.fixture.projectFixture
import com.jetbrains.godot.getBaseTestDataPath
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tscn.TscnParserDefinition
import tscn.psi.TscnParagraph
import kotlin.io.path.pathString

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["Scenes and resources/Parser"])
@TestApplication
class TscnRecoveryParserTest {
    private val projectFixture = projectFixture()
    private val parser by parsingFixture(
        projectFixture,
        getBaseTestDataPath(TscnRecoveryParserTest::class.java).resolve("testData/tscn/parser/recoveryData").pathString,
        "tscn",
        false,
        TscnParserDefinition(),
    )

    @BeforeEach fun setUp() {
        parser.includeRanges = true
    }

    @Test
    fun testDataLineRecovery() {
        parser.doTest(false)
        val paragraphs = runReadAction { parser.file!!.children.count { it is TscnParagraph } }
        assertEquals(2, paragraphs)
    }
}
