// Path: dotnet/Plugins/godot-support/gdscript/src/test/kotlin/com/jetbrains/godot/tscn/inspection/TscnSectionOrderInspectionTest.kt

package com.jetbrains.godot.tscn.inspection

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import tscn.inspection.TscnSectionOrderInspection
import kotlin.io.path.pathString

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["Scenes and resources/Inspections"])
@RunWith(JUnit4::class)
class TscnSectionOrderInspectionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
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
