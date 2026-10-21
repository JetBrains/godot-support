package com.jetbrains.godot.gdscript.highlight

import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import org.junit.jupiter.api.Test

@TestApplication
class GdPolySymbolHighlightingCustomizerTest {
    private val gd: GdSdkFixture by gdSdkFixture("highlighting")

    @Test
    fun testSymbolKindColoring() = gd.doHighlightingTest(checkSymbolNames = true, dir = false)

    @Test
    fun testSceneCameraLimitsColoring() = gd.doHighlightingTest(checkSymbolNames = true)

    @Test
    fun testAutoloadOwnDeclarationColoring() = gd.doHighlightingTest(checkSymbolNames = true, configureFileName = "global.gd")

    @Test
    fun testSuperKeywordColoring() = gd.doHighlightingTest(checkSymbolNames = true, dir = false)
}
