package com.jetbrains.godot.gdscript.highlight

import com.intellij.testFramework.TestModeFlags
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.annotator.GD_ANNOTATOR_ORIGINAL_SEVERITY
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.io.path.pathString

@TestApplication
class GdErrorsHighlightingTest : GdTestCaseWithSdk("highlighting") {

    @BeforeEach
    fun preserveAnnotatorSeverity() {
        TestModeFlags.set(GD_ANNOTATOR_ORIGINAL_SEVERITY, true, gd.testRootDisposable)
    }

    @Test
    fun testNestedClassErrors() = gd.doHighlightingTest(dir = false)

    @Test
    fun testLambdaCallableMultiline() = gd.doHighlightingTest(dir = false)

    @Test
    @Disabled("Bug in GdExprUtil.typeAccepts")
    fun testInvalidReturns() = gd.doHighlightingTest(dir = false)

    @Test
    fun testVariadicFunctions() = gd.doHighlightingTest(dir = false)
}
