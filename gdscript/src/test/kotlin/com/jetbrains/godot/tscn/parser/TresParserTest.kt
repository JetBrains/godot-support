package com.jetbrains.godot.tscn.parser

import com.intellij.testFramework.ParsingTestCase
import com.jetbrains.godot.getBaseTestDataPath
import org.junit.jupiter.api.Test
import tscn.TscnParserDefinition
import kotlin.io.path.pathString

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
