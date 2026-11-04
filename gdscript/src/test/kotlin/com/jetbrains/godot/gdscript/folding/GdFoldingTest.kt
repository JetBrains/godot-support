package com.jetbrains.godot.gdscript.folding

import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.jupiter.api.Test

/** Folding tests for [gdscript.formatter.GdFoldingBuilder]. */
@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["GDScript/Folding"])
@TestApplication
class GdFoldingTest {
    private val gd: GdFixture by gdLightFixture("gdscript/folding")

    private fun doFoldingTest() {
        gd.fixture.testFoldingWithCollapseStatus("${gd.testDataPath}/${gd.testName}.gd")
    }

    @Test
    fun testRegion() = doFoldingTest()

    @Test
    fun testNestedRegion() = doFoldingTest()

    // Godot recognizes only `#region` with no space; `# region` must not fold.
    @Test
    fun testRegionWithSpaceNotFolded() = doFoldingTest()

    @Test
    fun testMultilineString() = doFoldingTest()

    @Test
    fun testSingleLineStringNotFolded() = doFoldingTest()

    @Test
    fun testDict() = doFoldingTest()

    @Test
    fun testArray() = doFoldingTest()

    @Test
    fun testSuiteAndClass() = doFoldingTest()
}
