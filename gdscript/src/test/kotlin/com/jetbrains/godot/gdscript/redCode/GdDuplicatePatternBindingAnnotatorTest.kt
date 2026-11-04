package com.jetbrains.godot.gdscript.redCode

import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Red code"])
@TestApplication
class GdDuplicatePatternBindingAnnotatorTest {

    private val gd: GdFixture by gdLightFixture("gdscript/redCode")

    @Test
    fun testDuplicateBindingsAreNotReportedInPatternAndGuard() {
        val code = """
            |func test(point):
            |  match point:
            |    [var x, var x]:
            |      pass
            |    [var y, var y] when y == 1:
            |      pass
            |""".trimMargin()

        gd.fixture.configureByText("dup.gd", code)
        val infos = gd.fixture.doHighlighting()
        val errors = infos
            .filter { it.severity == HighlightSeverity.ERROR }

        assertTrue(errors.isEmpty(), "Expect no errors")
    }
}
