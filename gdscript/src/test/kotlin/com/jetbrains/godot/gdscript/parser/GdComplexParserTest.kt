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
class GdComplexParserTest : ParsingTestCase("", "gd", GdParserDefinition()) {
    @Rule @JvmField val testProcessorRule: TestProcessorRule = TestProcessorRule()

    @Test fun testAttributes() = doTest(true)
    @Test fun testSingleLineEnum() = doTest(true)
    @Test fun testBoolWithComparison() = doTest(true)
    @Test fun testNestedLoops() = doTest(true)
    @Test fun testNestedLambda() = doTest(true)

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/parser/complexData").pathString
    }

    override fun skipSpaces(): Boolean {
        return false
    }

    override fun includeRanges(): Boolean {
        return true
    }

}
