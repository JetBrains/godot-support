package com.jetbrains.godot.gdscript.highlight

import com.intellij.testFramework.TestModeFlags
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import gdscript.annotator.GD_ANNOTATOR_ORIGINAL_SEVERITY
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@TestApplication
class GdRefIdAnnotatorTest {
    private val gd: GdSdkFixture by gdSdkFixture("highlighting")

    @BeforeEach
    fun preserveAnnotatorSeverity() {
        TestModeFlags.set(GD_ANNOTATOR_ORIGINAL_SEVERITY, true, gd.testRootDisposable)
    }

    @Test
    fun testBuiltinTypeCannotBeAssignedToVariable() {
        gd.fixture.configureByText(
            "a.gd",
            """
            |func a():
            |	var x = <error descr="Builtin type [Vector2] cannot be assigned to a variable">Vector2</error>
            |	var y = Vector2.new()
            |	var z = Vector2(1, 2)
            """.trimMargin()
        )
        gd.fixture.checkHighlighting(false, false, false)
    }
}
