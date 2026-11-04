package com.jetbrains.godot.gdscript.inspection

import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.inspection.GdMethodValidationInspection
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Inspections"])
@TestApplication
class GdMethodValidationInspectionTest {
    private val gd: GdFixture by gdLightFixture("gdscript/inspection")

    @BeforeEach
    fun enableMethodValidationInspection() {
        gd.fixture.enableInspections(GdMethodValidationInspection::class.java)
    }

    @Test
    fun testWhileBreakUnreachable() {
        // the inspection is now at INFORMATION level the warning is suppressed, making the test pass.
        gd.fixture.testHighlighting("WhileBreakUnreachable.gd")
    }
}
