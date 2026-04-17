package com.jetbrains.godot.gdscript.parser

import com.jetbrains.godot.getBaseTestDataPath
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.io.path.pathString

class GdComplexRecoveryDataTest : GdParsingTestCase() {
    @Disabled("Hard to work with non completely valid test data")
    @Test fun testLambda() = doTest(true)
    @Disabled("Hard to work with non completely valid test data")
    @Test fun testLambda2() = doTest(true)

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve( "testData/gdscript/parser/complexRecoveryData").pathString
    }
}
