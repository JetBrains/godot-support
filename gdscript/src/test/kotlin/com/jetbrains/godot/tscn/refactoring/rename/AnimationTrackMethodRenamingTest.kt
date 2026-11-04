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

/**
 * The scene copies `networking/multiplayer_bomber/bomb.tscn` of the Godot demo projects. Track 1 is
 * a method track on `NodePath(".")` that calls `explode` and `done`, and `bomb.gd` declares both.
 */
@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["Scenes and resources/Rename"])
@TestApplication
class AnimationTrackMethodRenamingTest {

    private val gd: GdFixture by gdLightFixture("tscn/refactoring/rename/animationTrackMethodRenaming")

    /**
     * A rename of the method updates the track key that calls it.
     *
     * Three values keep the old name. Track 0 is a value track, so its `keys` hold no method at
     * all. Track 2 is a method track on `NodePath("Sprite")`, and that node carries no script, so
     * the same name there points at nothing. The `done` key of track 1 is a different method.
     */
    @Test
    fun testRename() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            gd.fixture.configureByFiles("bomb.gd", "bomb.tscn")
            gd.fixture.renameSymbolAtCaret("detonate")
            gd.fixture.checkResultByFile("bomb.gd", "bomb.gd.after", false)
            gd.fixture.checkResultByFile("bomb.tscn", "bomb.tscn.after", false)
        }
    }

    /**
     * The classic rename of the method reaches the track key too. It finds the key through
     * `gdscript.search.GdOwnReferencesSearcher` and writes the new name with
     * `tscn.psi.manipulator.TscnElementManipulator`, which keeps the `&"` prefix of the key.
     */
    @Test
    fun testClassicRename() = timeoutRunBlocking {
        withContext(Dispatchers.EDT) {
            gd.fixture.configureByFiles("bomb.gd", "bomb.tscn")
            gd.fixture.renameElementAtCaret("detonate")
            gd.fixture.checkResultByFile("bomb.gd", "bomb.gd.after", false)
            gd.fixture.checkResultByFile("bomb.tscn", "bomb.tscn.after", false)
        }
    }
}
