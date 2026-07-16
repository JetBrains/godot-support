package com.jetbrains.godot.gdscript.highlight

import com.intellij.testFramework.TestModeFlags
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.annotator.GD_ANNOTATOR_ORIGINAL_SEVERITY
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class GdErrorsHighlightingTest : GdTestCaseWithSdk("highlighting") {

    override fun setUp() {
        super.setUp()
        TestModeFlags.set(GD_ANNOTATOR_ORIGINAL_SEVERITY, true, testRootDisposable)
    }

    @Test
    fun testNestedClassErrors() = doHighlightingTest(dir = false)

    @Test
    fun testLambdaCallableMultiline() = doHighlightingTest(dir = false)

    @Test
    @Ignore("Bug in GdExprUtil.typeAccepts")
    fun testInvalidReturns() = doHighlightingTest(dir = false)

    @Test
    fun testVariadicFunctions() = doHighlightingTest(dir = false)
}
