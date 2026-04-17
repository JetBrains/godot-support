package com.jetbrains.godot.gdscript.inspection

import com.jetbrains.godot.GdCodeInsightTestBase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.inspection.GdMethodValidationInspection
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.io.path.pathString

class GdMethodValidationInspectionTest : GdCodeInsightTestBase() {

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/inspection").pathString
    }

    @BeforeEach
    fun enableInspections() {
        myFixture.enableInspections(GdMethodValidationInspection::class.java)
    }

    @Test
    @Disabled("RIDER-137812 GDScript unreachable code false positive warning")
    fun testWhileBreakUnreachable() {
        myFixture.testHighlighting("${getTestName(false)}.gd")
    }
}
