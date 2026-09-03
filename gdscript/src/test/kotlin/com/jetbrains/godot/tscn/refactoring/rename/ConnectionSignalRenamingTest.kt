package com.jetbrains.godot.tscn.refactoring.rename

import com.intellij.polySymbols.testFramework.renameSymbolAtCaret
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import kotlin.io.path.pathString

class ConnectionSignalRenamingTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/tscn/refactoring/rename/connectionSignalRenaming").pathString
    }

    /**
     * The `from` node of the connection is a child, and its script declares the signal. The handler
     * in the script of the `to` node keeps its name.
     */
    fun testRename() {
        myFixture.configureByFiles("player1.gd", "scene_a.gd", "scene_a.tscn")
        myFixture.renameSymbolAtCaret("defeated")
        myFixture.checkResultByFile("player1.gd", "player1.gd.after", false)
        myFixture.checkResultByFile("scene_a.gd", "scene_a.gd.after", false)
        myFixture.checkResultByFile("scene_a.tscn", "scene_a.tscn.after", false)
    }
}
