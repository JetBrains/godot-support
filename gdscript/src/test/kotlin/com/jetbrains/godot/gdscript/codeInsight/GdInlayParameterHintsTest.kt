package com.jetbrains.godot.gdscript.codeInsight

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdInlayParameterHintsTest : BasePlatformTestCase() {

    @Test
    fun testInlayHintsForSimpleFunctionCall() {
        val code = """
            |func foo(a: int, b: String) -> void:
            |	pass
            |
            |func test() -> void:
            |	foo(<hint text="a:"/>1, <hint text="b:"/>"hello")
        """.trimMargin()
        myFixture.configureByText("test.gd", code)
        myFixture.testInlays()
    }

    @Test
    fun testInlayHintsForQualifiedMethodCall() {
        myFixture.addFileToProject("TestInput.gd", """
            class_name TestInput

            static func get_vector1(negative_x: StringName, positive_x: StringName, negative_y: StringName, positive_y: StringName, deadzone: float = -1.0) -> Vector2:
            	return Vector2(0,0)
        """.trimIndent())

        val code = """
            |func info() -> void:
            |	TestInput.get_vector1(<hint text="negative_x:"/>"0", <hint text="positive_x:"/>"1", <hint text="negative_y:"/>"1", <hint text="positive_y:"/>"1")
        """.trimMargin()
        myFixture.configureByText("Test.gd", code)
        myFixture.testInlays()
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
        myFixture.configureByText("test.gd", code)
        myFixture.testInlays()
    }

}
