package com.jetbrains.godot.gdscript.codeInsight

import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Inlay hints"])
@TestApplication
class GdInlayParameterHintsTest {

    private val gd: GdFixture by gdLightFixture("gdscript/codeInsight")

    @Test
    fun testInlayHintsForSimpleFunctionCall() {
        val code = """
            |func foo(a: int, b: String) -> void:
            |	pass
            |
            |func test() -> void:
            |	foo(<hint text="a:"/>1, <hint text="b:"/>"hello")
        """.trimMargin()
        gd.fixture.configureByText("test.gd", code)
        gd.fixture.testInlays()
    }

    @Test
    fun testInlayHintsForQualifiedMethodCall() {
        gd.fixture.addFileToProject("TestInput.gd", """
            class_name TestInput

            static func get_vector1(negative_x: StringName, positive_x: StringName, negative_y: StringName, positive_y: StringName, deadzone: float = -1.0) -> Vector2:
            	return Vector2(0,0)
        """.trimIndent())

        val code = """
            |func info() -> void:
            |	TestInput.get_vector1(<hint text="negative_x:"/>"0", <hint text="positive_x:"/>"1", <hint text="negative_y:"/>"1", <hint text="positive_y:"/>"1")
        """.trimMargin()
        gd.fixture.configureByText("Test.gd", code)
        gd.fixture.testInlays()
    }

    @Test
    fun testInlayHintsForVariadicFunctionCall() {
        val code = """
            |func my_func(a, b = 0, ...args):
            |	pass
            |
            |func test() -> void:
            |	my_func(<hint text="a:"/>1, <hint text="b:"/>2, 3, 4, 5)
        """.trimMargin()
        gd.fixture.configureByText("test.gd", code)
        gd.fixture.testInlays()
    }

    @Test
    fun testInlayHintsForNewObjectCall() {
        val code = """
            |class_name WithInit
            |
            |func _init(a):
            |	pass
            |
            |class Outer:
            |
            |	func test():
            |		var w2 := WithInit.new(<hint text="a:"/>1)
        """.trimMargin()
        gd.fixture.configureByText("test.gd", code)
        gd.fixture.testInlays()
    }

}
