package com.jetbrains.godot.gdscript.inspection

import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.inspection.GdTypeHintInspection
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Inspections"])
@TestApplication
class GdTypeHintInspectionTest {
    private val gd: GdFixture by gdLightFixture("gdscript/inspection")

    @BeforeEach
    fun enableTypeHintInspection() {
        gd.fixture.enableInspections(GdTypeHintInspection::class.java)
    }

    @Test
    fun testTypeHintInferred() {
        gd.fixture.configureByText("test.gd", """
            <weak_warning descr="Field's return type can be specified as bool">var t = true</weak_warning>
        """.trimIndent())
        gd.fixture.testHighlighting(true, false, true)
    }

    @Test
    fun testTypeHintInferredQuickFixFull() {
        gd.fixture.configureByText("test.gd", "<caret>var t = true")
        gd.fixture.doHighlighting()
        val actions = gd.fixture.availableIntentions
        val filtered = actions.filter { it.text.contains("type") || it.text.contains(":=)") }
        assertEquals(2, filtered.size)
        // Auto-infer is alphabetically first
        assertEquals("Auto-infer type (:=)", filtered[1].text)
        assertEquals("Specify variable type [bool]", filtered[0].text)

        gd.fixture.launchAction(filtered[0])
        gd.fixture.checkResult("var t: bool = true")
    }

    @Test
    fun testTypeHintInferredQuickFixShort() {
        gd.fixture.configureByText("test.gd", "<caret>var t = true")
        gd.fixture.doHighlighting()
        val actions = gd.fixture.availableIntentions
        val filtered = actions.filter { it.text.contains("type") || it.text.contains(":=)") }
        assertEquals(2, filtered.size)
        // Auto-infer is alphabetically first
        assertEquals("Auto-infer type (:=)", filtered[1].text)
        assertEquals("Specify variable type [bool]", filtered[0].text)

        gd.fixture.launchAction(filtered[1])
        gd.fixture.checkResult("var t := true")
    }

    @Test
    fun testTypeHintInferredQuickFixNanLiteral() {
        gd.fixture.configureByText("test.gd", "<caret>var t = NAN")
        gd.fixture.doHighlighting()
        val actions = gd.fixture.availableIntentions
        val filtered = actions.filter { it.text.contains("type") || it.text.contains(":=)") }
        assertEquals(2, filtered.size)
        assertEquals("Specify variable type [float]", filtered[0].text)

        gd.fixture.launchAction(filtered[0])
        gd.fixture.checkResult("var t: float = NAN")
    }

    @Test
    fun testTypeHintInferredQuickFixInfLiteral() {
        gd.fixture.configureByText("test.gd", "<caret>var t = INF")
        gd.fixture.doHighlighting()
        val actions = gd.fixture.availableIntentions
        val filtered = actions.filter { it.text.contains("type") || it.text.contains(":=)") }
        assertEquals(2, filtered.size)
        assertEquals("Specify variable type [float]", filtered[0].text)

        gd.fixture.launchAction(filtered[0])
        gd.fixture.checkResult("var t: float = INF")
    }
}
