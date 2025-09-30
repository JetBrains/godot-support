package com.jetbrains.godot.tscn.refactoring.rename

import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.jetbrains.godot.getBaseTestDataPath
import kotlin.io.path.pathString

class ScriptClassRenamingTest : BasePlatformTestCase() {
    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/tscn/refactoring/rename/scriptClassRenaming").pathString
    }

    override fun setUp() {
        super.setUp()
        // Allow reading expected .after files from the test data directory
        val path = getTestDataPath()
        com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess.allowRootAccess(testRootDisposable, path)
    }

    fun testRename() {
        myFixture.configureByFiles("simple_resource.gd", "simple_resource.tres")
        myFixture.renameElementAtCaret("SimpleResourceRenamed")
        myFixture.checkResultByFile("simple_resource.gd", "simple_resource.gd.after", false)
        myFixture.checkResultByFile("simple_resource.tres", "simple_resource.tres.after", false)
    }
}