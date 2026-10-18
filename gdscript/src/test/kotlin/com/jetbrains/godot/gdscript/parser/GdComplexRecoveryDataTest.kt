package com.jetbrains.godot.gdscript.parser

import com.jetbrains.godot.getBaseTestDataPath
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Lexer and parser"])
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
