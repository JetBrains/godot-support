package com.jetbrains.godot.gdscript.parser

import com.jetbrains.godot.getBaseTestDataPath
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class GdComplexRecoveryDataTest : GdParsingTestCase() {
    @Ignore("Hard to work with non completely valid test data")
    @Test fun testLambda() = doTest(true)
    @Ignore("Hard to work with non completely valid test data")
    @Test fun testLambda2() = doTest(true)

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve( "testData/gdscript/parser/complexRecoveryData").pathString
    }
}
