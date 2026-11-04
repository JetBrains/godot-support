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
class ConnectionSignalRenamingTest {
    private val gd: GdFixture by gdLightFixture("tscn/refactoring/rename/connectionSignalRenaming")

    /**
     * The `from` node of the connection is a child, and its script declares the signal. The handler
     * in the script of the `to` node keeps its name.
     */
    @Test
    fun testRename() = timeoutRunBlocking {
        gd.fixture.configureByFiles("player1.gd", "scene_a.gd", "scene_a.tscn")
        withContext(Dispatchers.EDT) { gd.fixture.renameSymbolAtCaret("defeated") }
        gd.fixture.checkResultByFile("player1.gd", "player1.gd.after", false)
        gd.fixture.checkResultByFile("scene_a.gd", "scene_a.gd.after", false)
        gd.fixture.checkResultByFile("scene_a.tscn", "scene_a.tscn.after", false)
    }
}
