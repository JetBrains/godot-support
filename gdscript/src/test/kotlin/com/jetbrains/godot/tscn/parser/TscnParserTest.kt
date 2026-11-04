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
class TscnParserTest {
    private val projectFixture = projectFixture()
    private val parser by parsingFixture(
        projectFixture,
        getBaseTestDataPath(TscnParserTest::class.java).resolve("testData/tscn/parser/data").pathString,
        "tscn",
        false,
        TscnParserDefinition(),
    )

    @BeforeEach fun setUp() {
        parser.includeRanges = true
    }

    @Test fun testHeader() = parser.doTest(true)
    @Test fun testDataLines() = parser.doTest(true)
    @Test fun testMultiParagraphs() = parser.doTest(true)
    @Test fun testTypedArrayWithExtResource() = parser.doTest(true)
    @Test fun testTypedDictionaryWithExtResource() = parser.doTest(true)
    @Test fun testMultilineText() = parser.doTest(true)
    @Test fun testSlashKey() = parser.doTest(true)
    @Test fun testColonKey() = parser.doTest(true)
    @Test fun testDefaultLibraryKey() = parser.doTest(true)
    @Test fun testJson() = parser.doTest(true)
}
