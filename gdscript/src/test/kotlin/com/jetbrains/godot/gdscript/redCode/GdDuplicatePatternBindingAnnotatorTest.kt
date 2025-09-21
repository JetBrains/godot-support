package com.jetbrains.godot.gdscript.redCode

import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.testFramework.fixtures.BasePlatformTestCase

class GdDuplicatePatternBindingAnnotatorTest : BasePlatformTestCase() {

    fun testDuplicateBindingsAreNotReportedInPatternAndGuard() {
        val code = """
            |func test(point):
            |  match point:
            |    [var x, var x]:
            |      pass
            |    [var y, var y] when y == 1:
            |      pass
            |""".trimMargin()

        myFixture.configureByText("dup.gd", code)
        val infos = myFixture.doHighlighting()
        val errors = infos
            .filter { it.severity == HighlightSeverity.ERROR }

        assertTrue("Expect no errors", errors.isEmpty())
    }
}
