package com.jetbrains.godot.gdscript.parser

import com.intellij.psi.util.descendantsOfType
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.psi.GdFuncDeclEx
import gdscript.psi.GdVarDeclSt

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Type inference"])
class GdCallableInferenceTest : BasePlatformTestCase() {

    fun testLambdaInference() {
        //language=GDScript
        val code = """
            func hello():
                var thing := func() -> String:
                    return ""
        """.trimIndent()
        val file = myFixture.configureByText("test.gd", code)
        val varDecl = file.descendantsOfType<GdVarDeclSt>().first { it.getName() == "thing" }
        val funcDecl = varDecl.descendantsOfType<GdFuncDeclEx>().single()
        assertEquals("Callable", varDecl.returnType)
        assertEquals("String", funcDecl.invokedReturnType)
    }
}
