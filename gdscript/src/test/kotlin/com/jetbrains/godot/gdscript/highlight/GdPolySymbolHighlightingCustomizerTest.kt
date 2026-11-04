package com.jetbrains.godot.gdscript.highlight

import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdSdkFixture
import com.jetbrains.godot.gdscript.gdSdkFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Highlighting"])
@TestApplication
class GdPolySymbolHighlightingCustomizerTest {
    private val gd: GdSdkFixture by gdSdkFixture("gdscript/highlighting")

    @Test
    fun testSymbolKindColoring() = gd.doHighlightingTest(checkSymbolNames = true, dir = false)

    @Test
    fun testSceneCameraLimitsColoring() = gd.doHighlightingTest(checkSymbolNames = true)

    @Test
    fun testAutoloadOwnDeclarationColoring() = gd.doHighlightingTest(checkSymbolNames = true, configureFileName = "global.gd")

    @Test
    fun testSuperKeywordColoring() = gd.doHighlightingTest(checkSymbolNames = true, dir = false)
}
