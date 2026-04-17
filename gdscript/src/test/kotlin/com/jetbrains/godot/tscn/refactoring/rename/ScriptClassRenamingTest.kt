package com.jetbrains.godot.tscn.refactoring.rename

import com.jetbrains.godot.GdCodeInsightTestBaseWithVFSAccess
import com.jetbrains.godot.getBaseTestDataPath
import org.junit.jupiter.api.Test
import kotlin.io.path.pathString

class ScriptClassRenamingTest : GdCodeInsightTestBaseWithVFSAccess() {

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/tscn/refactoring/rename/scriptClassRenaming").pathString
    }

    @Test
    fun testRename() {
        myFixture.configureByFiles("simple_resource.gd", "simple_resource.tres")
        myFixture.renameElementAtCaret("SimpleResourceRenamed")
        myFixture.checkResultByFile("simple_resource.gd", "simple_resource.gd.after", false)
        myFixture.checkResultByFile("simple_resource.tres", "simple_resource.tres.after", false)
    }
}
