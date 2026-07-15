package com.jetbrains.godot.gdscript.highlight

import com.intellij.testFramework.TestModeFlags
import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import gdscript.annotator.GD_ANNOTATOR_ORIGINAL_SEVERITY
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdRefIdAnnotatorTest : GdTestCaseWithSdk("highlighting") {

    override fun setUp() {
        super.setUp()
        TestModeFlags.set(GD_ANNOTATOR_ORIGINAL_SEVERITY, true, testRootDisposable)
    }

    @Test
    fun testBuiltinTypeCannotBeAssignedToVariable() {
        myFixture.configureByText(
            "a.gd",
            """
            |func a():
            |	var x = <error descr="Builtin type [Vector2] cannot be assigned to a variable">Vector2</error>
            |	var y = Vector2.new()
            |	var z = Vector2(1, 2)
            """.trimMargin()
        )
        myFixture.checkHighlighting(false, false, false)
    }
}
