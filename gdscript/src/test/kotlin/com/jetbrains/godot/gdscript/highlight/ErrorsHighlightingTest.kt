package com.jetbrains.godot.gdscript.highlight

import com.intellij.testFramework.TestModeFlags
import com.jetbrains.godot.GdCodeInsightTestBase
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.annotator.GD_ANNOTATOR_ORIGINAL_SEVERITY
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import kotlin.io.path.pathString

class ErrorsHighlightingTest : GdCodeInsightTestBase() {

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/highlighting").pathString
    }

    @BeforeEach
    fun setUpHighlighting() {
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
    @Disabled("Bug in GdExprUtil.typeAccepts")
    fun testInvalidReturns() {
        myFixture.testHighlighting("${getTestName(false)}.gd")
    }

    @Test
    fun testVariadicFunctions(){
        myFixture.testHighlighting("${getTestName(false)}.gd")
    }
}
