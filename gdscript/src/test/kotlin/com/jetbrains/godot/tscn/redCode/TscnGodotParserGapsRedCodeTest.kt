package com.jetbrains.godot.tscn.redCode

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.gdscript.redCode.collectErrors
import com.jetbrains.godot.getBaseTestDataPath
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["Scenes and resources/Red code"])
@RunWith(JUnit4::class)
class TscnGodotParserGapsRedCodeTest : BasePlatformTestCase() {
    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/tscn/redCode").pathString
    }

    @Test
    fun testGodotParserGapsHaveNoRedCode() {
        val psiFile = myFixture.configureByFile("GodotParserGaps.tscn")
        val errors = psiFile.children.flatMap { collectErrors(it) }

        assertTrue(
            "Did not expect red code; actual errors: " +
                errors.joinToString(" | ") { it.errorDescription + "@" + it.textRange },
            errors.isEmpty()
        )
    }
}
