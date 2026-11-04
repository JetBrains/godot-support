package com.jetbrains.godot.tscn.refactoring.rename

import com.intellij.polySymbols.testFramework.renameSymbolAtCaret
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["Scenes and resources/Rename"])
@TestApplication
class ResourceFieldRenamingTest {
    private val gd: GdFixture by gdLightFixture("tscn/refactoring/rename/resourceFieldRenaming")

    @Test
    fun testRename() = gd.doConfiguredTest(
        dir = false,
        configureFile = false,
        additionalFiles = listOf("simple_resource.gd", "simple_resource.tres"),
    ) {
        renameSymbolAtCaret("valueRenamed")
        checkResultByFile("simple_resource.gd", "simple_resource.gd.after", false)
        checkResultByFile("simple_resource.tres", "simple_resource.tres.after", false)
    }
}
