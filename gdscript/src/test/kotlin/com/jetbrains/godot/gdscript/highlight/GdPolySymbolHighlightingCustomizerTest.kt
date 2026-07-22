package com.jetbrains.godot.gdscript.highlight

import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class GdPolySymbolHighlightingCustomizerTest : GdTestCaseWithSdk("highlighting") {

    @Test
    fun testSymbolKindColoring() = doHighlightingTest(checkSymbolNames = true, dir = false)

    @Test
    fun testSceneCameraLimitsColoring() = doHighlightingTest(checkSymbolNames = true)
}
