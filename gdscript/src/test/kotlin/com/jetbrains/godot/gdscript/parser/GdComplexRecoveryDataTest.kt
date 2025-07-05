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
class GdComplexRecoveryDataTest : ParsingTestCase("", "gd", GdParserDefinition()) {
    @Rule @JvmField val testProcessorRule: TestProcessorRule = TestProcessorRule()

    @Test fun testLambda() = doTest(true)
    @Test fun testLambda2() = doTest(true)

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve( "testData/gdscript/parser/complexRecoveryData").pathString
    }

    override fun skipSpaces(): Boolean {
        return false
    }

    override fun includeRanges(): Boolean {
        return true
    }
}
