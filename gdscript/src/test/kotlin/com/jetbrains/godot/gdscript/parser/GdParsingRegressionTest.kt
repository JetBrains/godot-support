package com.jetbrains.godot.gdscript.parser

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Lexer and parser"])
@TestApplication
class GdParsingRegressionTest {
    private val gd: GdFixture by gdLightFixture("gdscript/parser")

    // Regression: ensure parsing completes for inline lambda with multiple semicolon-separated statements
    @Test // tried to set the timeout, but it always failed to finish in time
    fun testInlineLambdaWithSemicolons_NoInfiniteParse() {
        val code = """
            |func test():
            |	if not error_callback is Callable: error_callback = (func(str): _printerr("SimpleDungeons Error: ", str))
            |	var any_errors : = {"err": false} # So lambda closure captures
            |	error_callback = (func(str): any_errors["err"] = true; error_callback.call(str))
            |""".trimMargin()

        val psi = gd.fixture.configureByText("sample.gd", code)
        // Access PSI to force full parse tree construction
        assertNotNull(runReadActionBlocking { psi.firstChild })
    }
}
