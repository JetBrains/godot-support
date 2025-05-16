package com.jetbrains.godot.parser

import com.intellij.testFramework.ParsingTestCase
import gdscript.GdParserDefinition

class GdComplexParserTest : ParsingTestCase("", "gd", GdParserDefinition()) {

    fun testAttributes() = doTest(true)
    fun testSingleLineEnum() = doTest(true)
    fun testBoolWithComparison() = doTest(true)
    fun testNestedLoops() = doTest(true)
    fun testNestedLambda() = doTest(true)

    override fun getTestDataPath(): String {
        return "testData/gdscript/parser/complexData"
    }

    override fun skipSpaces(): Boolean {
        return false
    }

    override fun includeRanges(): Boolean {
        return true
    }

}
