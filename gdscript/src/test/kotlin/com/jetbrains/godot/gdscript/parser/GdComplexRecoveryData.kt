package com.jetbrains.godot.parser

import com.intellij.testFramework.ParsingTestCase
import gdscript.GdParserDefinition

class GdComplexRecoveryData : ParsingTestCase("", "gd", GdParserDefinition()) {

    fun testLambda() = doTest(true)
    fun testLambda2() = doTest(true)

    override fun getTestDataPath(): String {
        return "testData/gdscript/parser/complexRecoveryData"
    }

    override fun skipSpaces(): Boolean {
        return false
    }

    override fun includeRanges(): Boolean {
        return true
    }
}
