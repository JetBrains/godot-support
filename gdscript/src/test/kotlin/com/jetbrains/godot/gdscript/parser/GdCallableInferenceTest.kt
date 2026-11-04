package com.jetbrains.godot.gdscript.parser

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.psi.util.descendantsOfType
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.psi.GdFuncDeclEx
import gdscript.psi.GdVarDeclSt
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Type inference"])
@TestApplication
class GdCallableInferenceTest {
    private val gd: GdFixture by gdLightFixture("gdscript/parser")

    @Test
    fun testLambdaInference() {
        //language=GDScript
        val code = """
            func hello():
                var thing := func() -> String:
                    return ""
        """.trimIndent()
        val file = gd.fixture.configureByText("test.gd", code)
        runReadActionBlocking {
            val varDecl = file.descendantsOfType<GdVarDeclSt>().first { it.getName() == "thing" }
            val funcDecl = varDecl.descendantsOfType<GdFuncDeclEx>().single()
            assertEquals("Callable", varDecl.returnType)
            assertEquals("String", funcDecl.invokedReturnType)
        }
    }
}
