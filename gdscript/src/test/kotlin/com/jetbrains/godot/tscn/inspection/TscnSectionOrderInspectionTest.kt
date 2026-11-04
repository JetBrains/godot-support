package com.jetbrains.godot.tscn.inspection

import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import tscn.inspection.TscnSectionOrderInspection

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["Scenes and resources/Inspections"])
@TestApplication
class TscnSectionOrderInspectionTest {

    private val gd: GdFixture by gdLightFixture("tscn/inspection")

    @BeforeEach
    fun enableInspection() {
        gd.fixture.enableInspections(TscnSectionOrderInspection::class.java)
    }

    @Test
    fun testConnectionBeforeNode() {
        gd.fixture.testHighlighting("ConnectionBeforeNode.tscn")
    }

    @Test
    fun testCorrectHeaderOrder() {
        gd.fixture.testHighlighting("CorrectHeaderOrder.tscn")
    }

    @Test
    fun testExtResourceBeforeGdScene() {
        gd.fixture.testHighlighting("ExtResourceBeforeGdScene.tscn")
    }

    @Test
    fun testMultipleOutOfOrderSections() {
        gd.fixture.testHighlighting("MultipleOutOfOrderSections.tscn")
    }

    @Test
    fun testNodeBeforeSubResource() {
        gd.fixture.testHighlighting("NodeBeforeSubResource.tscn")
    }
}
