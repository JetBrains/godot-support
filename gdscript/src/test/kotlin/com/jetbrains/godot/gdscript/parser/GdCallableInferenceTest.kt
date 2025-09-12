package com.jetbrains.godot.gdscript.parser

import com.intellij.psi.util.descendantsOfType
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import gdscript.psi.GdFuncDeclEx
import gdscript.psi.GdVarDeclSt

class GdCallableInferenceTest : BasePlatformTestCase() {

    fun testLambdaInference() {
        //language=GDScript
        val code = """
            func hello():
                var thing := func() -> String:
                    return ""
        """.trimIndent()
        val file = myFixture.configureByText("test.gd", code)
        val varDecl = file.descendantsOfType<GdVarDeclSt>().first { it.name == "thing" }
        val funcDecl = varDecl.descendantsOfType<GdFuncDeclEx>().single()
        assertEquals("Callable", varDecl.returnType)
        assertEquals("String", funcDecl.invokedReturnType)
    }
}
