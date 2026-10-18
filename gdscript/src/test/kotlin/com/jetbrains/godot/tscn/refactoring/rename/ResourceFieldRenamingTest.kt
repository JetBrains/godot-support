package com.jetbrains.godot.tscn.refactoring.rename

import com.intellij.polySymbols.testFramework.renameSymbolAtCaret
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import kotlin.io.path.pathString

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["Scenes and resources/Rename"])
class ResourceFieldRenamingTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/tscn/refactoring/rename/resourceFieldRenaming").pathString
    }

    fun testRename() {
        myFixture.configureByFiles("simple_resource.gd", "simple_resource.tres")
        myFixture.renameSymbolAtCaret("valueRenamed")
        myFixture.checkResultByFile("simple_resource.gd", "simple_resource.gd.after", false)
        myFixture.checkResultByFile("simple_resource.tres", "simple_resource.tres.after", false)
    }
}