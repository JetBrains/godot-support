package com.jetbrains.godot.tscn.parser

import com.intellij.testFramework.ParsingTestCase
import com.jetbrains.godot.getBaseTestDataPath
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import tscn.TscnParserDefinition
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class TresParserTest : ParsingTestCase("", "tres", TscnParserDefinition()) {
    @Test fun testResourceWithTypedCollections() = doTest(true)

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/tscn/parser/data").pathString
    }

    override fun skipSpaces(): Boolean {
        return false
    }

    override fun includeRanges(): Boolean {
        return true
    }
}
