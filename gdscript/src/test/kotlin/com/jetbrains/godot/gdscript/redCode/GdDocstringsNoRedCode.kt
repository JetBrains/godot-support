package com.jetbrains.godot.gdscript.redCode

import com.jetbrains.godot.GdCodeInsightTestBase
import com.jetbrains.godot.getBaseTestDataPath
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import kotlin.io.path.pathString

class GdDocstringsNoRedCode : GdCodeInsightTestBase() {
    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/parser/godotTestCases").pathString
    }

    @Test
    fun testDocstringsHaveNoRedCode() {
        val psiFile = myFixture.configureByFile("strings.gd")
        val errors = psiFile.children.flatMap { collectErrors(it) }

        // Ensure there are no PsiErrorElements (no red code)
        assertTrue(
            errors.isEmpty(),
            "Did not expect red code; actual errors: " +
                errors.joinToString(" | ") { it.errorDescription + "@" + it.textRange }
        )
    }
}
