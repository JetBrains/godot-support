package com.jetbrains.godot.parser

import com.intellij.testFramework.ParsingTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.GdParserDefinition
import kotlin.io.path.pathString

class GdComplexRecoveryData : ParsingTestCase("", "gd", GdParserDefinition()) {

    fun testLambda() = doTest(true)
    fun testLambda2() = doTest(true)

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
