package com.jetbrains.godot.tscn.parser

import com.intellij.testFramework.ParsingTestCase
import com.jetbrains.godot.getBaseTestDataPath
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import tscn.TscnParserDefinition
import tscn.psi.TscnParagraph
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class TscnRecoveryParserTest : ParsingTestCase("", "tscn", TscnParserDefinition()) {
    @Test
    fun testDataLineRecovery() {
        doTest(false)
        assertEquals(2, myFile.children.count { it is TscnParagraph })
    }

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/tscn/parser/recoveryData").pathString
    }

    override fun skipSpaces(): Boolean {
        return false
    }

    override fun includeRanges(): Boolean {
        return true
    }
}
