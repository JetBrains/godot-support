package com.jetbrains.godot.gdscript.redCode

import com.intellij.openapi.application.runReadActionBlocking
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
class GdDocstringsNoRedCode {
    private val gd: GdFixture by gdLightFixture("gdscript/parser/godotTestCases")

    @Test
    fun testDocstringsHaveNoRedCode() {
        val psiFile = gd.fixture.configureByFile("strings.gd")

        val (isEmpty, message) = runReadActionBlocking {
            val errors = psiFile.children.flatMap { collectErrors(it) }
            errors.isEmpty() to errors.joinToString(" | ") { it.errorDescription + "@" + it.textRange }
        }

        // Ensure there are no PsiErrorElements (no red code)
        assertTrue(isEmpty, "Did not expect red code; actual errors: $message")
    }
}
