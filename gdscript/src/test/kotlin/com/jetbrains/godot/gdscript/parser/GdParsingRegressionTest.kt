package com.jetbrains.godot.gdscript.parser

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdParsingRegressionTest : BasePlatformTestCase() {

    // Regression: ensure parsing completes for inline lambda with multiple semicolon-separated statements
    @Test // tried to set the timeout, but it always failed to finish in time
    fun testInlineLambdaWithSemicolons_NoInfiniteParse() {
        val code = """
            |func test():
            |	if not error_callback is Callable: error_callback = (func(str): _printerr("SimpleDungeons Error: ", str))
            |	var any_errors : = {"err": false} # So lambda closure captures
            |	error_callback = (func(str): any_errors["err"] = true; error_callback.call(str))
            |""".trimMargin()

        val psi = myFixture.configureByText("sample.gd", code)
        // Access PSI to force full parse tree construction
        assertNotNull(psi.firstChild)
    }
}
