package com.jetbrains.godot.tscn.refactoring.rename

import com.intellij.openapi.application.EDT
import com.intellij.polySymbols.testFramework.renameSymbolAtCaret
import com.intellij.testFramework.common.timeoutRunBlocking
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.Test

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["Scenes and resources/Rename"])
@TestApplication
class ScriptClassRenamingTest {
    private val gd: GdFixture by gdLightFixture("tscn/refactoring/rename/scriptClassRenaming")

    @Test
    fun testRename() = timeoutRunBlocking {
        gd.fixture.configureByFiles("simple_resource.gd", "simple_resource.tres")
        withContext(Dispatchers.EDT) {
            gd.fixture.renameSymbolAtCaret("SimpleResourceRenamed")
        }
        gd.fixture.checkResultByFile("simple_resource.gd", "simple_resource.gd.after", false)
        gd.fixture.checkResultByFile("simple_resource.tres", "simple_resource.tres.after", false)
    }
}
