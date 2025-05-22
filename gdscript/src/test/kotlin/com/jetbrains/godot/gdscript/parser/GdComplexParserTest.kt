package com.jetbrains.godot.parser

import com.intellij.testFramework.ParsingTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.GdParserDefinition
import kotlin.io.path.pathString

class GdComplexParserTest : ParsingTestCase("", "gd", GdParserDefinition()) {

    fun testAttributes() = doTest(true)
    fun testSingleLineEnum() = doTest(true)
    fun testBoolWithComparison() = doTest(true)
    fun testNestedLoops() = doTest(true)
    fun testNestedLambda() = doTest(true)

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
