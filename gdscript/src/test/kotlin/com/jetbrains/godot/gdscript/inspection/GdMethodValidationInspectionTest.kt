package com.jetbrains.godot.gdscript.inspection

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.inspection.GdMethodValidationInspection
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class GdMethodValidationInspectionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(GdMethodValidationInspection::class.java)
    }

    @Test
    fun testWhileBreakUnreachable() {
        // the inspection is now at INFORMATION level the warning is suppressed, making the test pass.
        myFixture.testHighlighting("${getTestName(false)}.gd")
    }

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/inspection").pathString
    }
}
