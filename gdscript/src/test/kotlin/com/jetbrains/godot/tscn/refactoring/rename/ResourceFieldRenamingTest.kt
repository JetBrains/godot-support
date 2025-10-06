package com.jetbrains.godot.tscn.refactoring.rename

import com.jetbrains.godot.BasePlatformTestCaseWithTestDataVFSAccess
import com.jetbrains.godot.getBaseTestDataPath
import kotlin.io.path.pathString

class ResourceFieldRenamingTest : BasePlatformTestCaseWithTestDataVFSAccess() {

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/tscn/refactoring/rename/resourceFieldRenaming").pathString
    }

    fun testRename() {
        myFixture.configureByFiles("simple_resource.gd", "simple_resource.tres")
        myFixture.renameElementAtCaret("valueRenamed")
        myFixture.checkResultByFile("simple_resource.gd", "simple_resource.gd.after", false)
        myFixture.checkResultByFile("simple_resource.tres", "simple_resource.tres.after", false)
    }
}