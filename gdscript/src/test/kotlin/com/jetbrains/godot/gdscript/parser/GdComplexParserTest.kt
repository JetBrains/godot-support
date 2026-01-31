package com.jetbrains.godot.gdscript.parser

import com.jetbrains.godot.getBaseTestDataPath
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class GdComplexParserTest : GdParsingTestCase() {
    @Test fun testAttributes() = doTest(true)
    @Test fun testSingleLineEnum() = doTest(true)
    @Test fun testBoolWithComparison() = doTest(true)
    @Test fun testNestedLoops() = doTest(true)

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/parser/complexData").pathString
    }
}
