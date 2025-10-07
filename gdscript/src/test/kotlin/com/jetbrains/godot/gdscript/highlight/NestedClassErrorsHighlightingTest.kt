package com.jetbrains.godot.gdscript.highlight

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import org.junit.Ignore
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import kotlin.io.path.pathString

@RunWith(JUnit4::class)
class NestedClassErrorsHighlightingTest : BasePlatformTestCase() {
    @Test
    fun testNestedClassErrors() {
        myFixture.testHighlighting("${getTestName(false)}.gd")
    }

    @Test
    @Ignore("Bug in GdExprUtil.typeAccepts")
    fun testInvalidReturns() {
        myFixture.testHighlighting("${getTestName(false)}.gd")
    }

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/highlighting").pathString
    }
}
