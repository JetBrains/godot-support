package com.jetbrains.godot.tscn.refactoring.rename

import com.intellij.polySymbols.testFramework.renameSymbolAtCaret
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import kotlin.io.path.pathString

/**
 * The scene copies `networking/multiplayer_bomber/bomb.tscn` of the Godot demo projects. Track 1 is
 * a method track on `NodePath(".")` that calls `explode` and `done`, and `bomb.gd` declares both.
 */
class AnimationTrackMethodRenamingTest : BasePlatformTestCase() {

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/tscn/refactoring/rename/animationTrackMethodRenaming").pathString
    }

    /**
     * A rename of the method updates the track key that calls it.
     *
     * Three values keep the old name. Track 0 is a value track, so its `keys` hold no method at
     * all. Track 2 is a method track on `NodePath("Sprite")`, and that node carries no script, so
     * the same name there points at nothing. The `done` key of track 1 is a different method.
     */
    fun testRename() {
        myFixture.configureByFiles("bomb.gd", "bomb.tscn")
        myFixture.renameSymbolAtCaret("detonate")
        myFixture.checkResultByFile("bomb.gd", "bomb.gd.after", false)
        myFixture.checkResultByFile("bomb.tscn", "bomb.tscn.after", false)
    }

    /**
     * The classic rename of the method reaches the track key too. It finds the key through
     * `gdscript.search.GdOwnReferencesSearcher` and writes the new name with
     * `tscn.psi.manipulator.TscnElementManipulator`, which keeps the `&"` prefix of the key.
     */
    fun testClassicRename() {
        myFixture.configureByFiles("bomb.gd", "bomb.tscn")
        myFixture.renameElementAtCaret("detonate")
        myFixture.checkResultByFile("bomb.gd", "bomb.gd.after", false)
        myFixture.checkResultByFile("bomb.tscn", "bomb.tscn.after", false)
    }
}
