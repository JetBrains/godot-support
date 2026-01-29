package com.jetbrains.godot.gdscript.redCode

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class GdDocstringsNoRedCode : BasePlatformTestCase() {
    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/parser/godotTestCases").pathString
    }

    @Test
    fun testDocstringsHaveNoRedCode(){
        val psiFile = myFixture.configureByFile("strings.gd")
        val errors = psiFile.children.flatMap { collectErrors(it) }

        // Ensure there are no PsiErrorElements (no red code)
        assertTrue(
            "Did not expect red code; actual errors: " +
                errors.joinToString(" | ") { it.errorDescription + "@" + it.textRange },
            errors.isEmpty()
        )
    }
}
