package com.jetbrains.godot.gdscript.highlight

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import kotlin.io.path.pathString

class NestedClassErrorsHighlightingTest : BasePlatformTestCase() {

    fun testNestedClassErrors() {
        myFixture.testHighlighting("${getTestName(false)}.gd")
    }

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/gdscript/highlighting").pathString
    }
}
