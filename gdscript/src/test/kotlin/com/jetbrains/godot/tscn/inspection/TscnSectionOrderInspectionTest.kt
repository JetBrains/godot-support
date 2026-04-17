// Path: dotnet/Plugins/godot-support/gdscript/src/test/kotlin/com/jetbrains/godot/tscn/inspection/TscnSectionOrderInspectionTest.kt

package com.jetbrains.godot.tscn.inspection

import com.jetbrains.godot.GdCodeInsightTestBase
import com.jetbrains.godot.getBaseTestDataPath
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tscn.inspection.TscnSectionOrderInspection
import kotlin.io.path.pathString

class TscnSectionOrderInspectionTest : GdCodeInsightTestBase() {

    @BeforeEach
    fun enableInspections() {
        myFixture.enableInspections(TscnSectionOrderInspection::class.java)
    }

    @Test
    fun testConnectionBeforeNode() {
        myFixture.testHighlighting("${getTestName(false)}.tscn")
    }

    @Test
    fun testCorrectHeaderOrder() {
        myFixture.testHighlighting("${getTestName(false)}.tscn")
    }

    @Test
    fun testExtResourceBeforeGdScene() {
        myFixture.testHighlighting("${getTestName(false)}.tscn")
    }

    @Test
    fun testMultipleOutOfOrderSections() {
        myFixture.testHighlighting("${getTestName(false)}.tscn")
    }

    @Test
    fun testNodeBeforeSubResource() {
        myFixture.testHighlighting("${getTestName(false)}.tscn")
    }

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/tscn/inspection").pathString
    }
}
