package com.jetbrains.godot.gdscript.highlight

import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdTestCaseWithSdk
import org.junit.jupiter.api.Test

@TestApplication
class GdPolySymbolHighlightingCustomizerTest : GdTestCaseWithSdk("highlighting") {

    @Test
    fun testSymbolKindColoring() = gd.doHighlightingTest(checkSymbolNames = true, dir = false)

    @Test
    fun testSceneCameraLimitsColoring() = gd.doHighlightingTest(checkSymbolNames = true)

    @Test
    fun testAutoloadOwnDeclarationColoring() = gd.doHighlightingTest(checkSymbolNames = true, configureFileName = "global.gd")

    @Test
    fun testSuperKeywordColoring() = gd.doHighlightingTest(checkSymbolNames = true, dir = false)
}
