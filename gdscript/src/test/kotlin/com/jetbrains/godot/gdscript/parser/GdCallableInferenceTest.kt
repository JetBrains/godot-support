package com.jetbrains.godot.gdscript.parser

import com.intellij.psi.util.descendantsOfType
import com.jetbrains.godot.GdCodeInsightTestBase
import gdscript.psi.GdFuncDeclEx
import gdscript.psi.GdVarDeclSt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GdCallableInferenceTest : GdCodeInsightTestBase() {

    @Test
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
