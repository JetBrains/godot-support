package com.jetbrains.godot.tscn.redCode

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.godot.gdscript.redCode.collectErrors
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["Scenes and resources/Red code"])
@TestApplication
class TscnGodotParserGapsRedCodeTest {
    private val gd: GdFixture by gdLightFixture("tscn/redCode")

    @Test
    fun testGodotParserGapsHaveNoRedCode() {
        val psiFile = gd.fixture.configureByFile("GodotParserGaps.tscn")

        val (isEmpty, message) = runReadActionBlocking {
            val errors = psiFile.children.flatMap { collectErrors(it) }
            errors.isEmpty() to errors.joinToString(" | ") { it.errorDescription + "@" + it.textRange }
        }

        assertTrue(isEmpty, "Did not expect red code; actual errors: $message")
    }
}
