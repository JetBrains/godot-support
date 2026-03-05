package com.jetbrains.godot.gdscript.highlight

import com.intellij.testFramework.TestModeFlags
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.annotator.GD_ANNOTATOR_ORIGINAL_SEVERITY
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class ErrorsHighlightingTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        TestModeFlags.set(GD_ANNOTATOR_ORIGINAL_SEVERITY, true, testRootDisposable)
    }

    @Test
    fun testNestedClassErrors() {
        myFixture.testHighlighting("${getTestName(false)}.gd")
    }

    @Test
    fun testLambdaCallableMultiline() {
        myFixture.testHighlighting("${getTestName(false)}.gd");
    }

    @Test
    @Ignore("Bug in GdExprUtil.typeAccepts")
    fun testInvalidReturns() {
        myFixture.testHighlighting("${getTestName(false)}.gd")
    }

    @Test
    fun testVariadicFunctions(){
        myFixture.testHighlighting("${getTestName(false)}.gd")
    }

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/highlighting").pathString
    }
}
