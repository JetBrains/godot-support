package com.jetbrains.godot

import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess
import com.intellij.testFramework.fixtures.BasePlatformTestCase

abstract class BasePlatformTestCaseWithTestDataVFSAccess : BasePlatformTestCase() {
    override fun setUp() {
        super.setUp()
        // Allow reading expected .after files from the test data directory
        val path = testDataPath
        VfsRootAccess.allowRootAccess(testRootDisposable, path)
    }
}