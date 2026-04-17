package com.jetbrains.godot.tscn.parser

import com.jetbrains.godot.getBaseTestDataPath
import org.junit.jupiter.api.Test
import kotlin.io.path.pathString

class TresParserTest : TscnParsingTestBase(fileExt = "tres") {
    @Test fun testResourceWithTypedCollections() = doTest(true)

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/tscn/parser/data").pathString
    }
}
