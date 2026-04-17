package com.jetbrains.godot

import com.intellij.openapi.vfs.newvfs.impl.VfsRootAccess
import org.junit.jupiter.api.BeforeEach

/**
 * JUnit5-native replacement for the old BasePlatformTestCaseWithTestDataVFSAccess.
 *
 * Extends [GdCodeInsightTestBase] and additionally grants VFS root-access to the
 * test-data directory so that file-system-based tests can read files from there.
 */
abstract class GdCodeInsightTestBaseWithVFSAccess : GdCodeInsightTestBase() {

    @BeforeEach
    fun allowVfsRootAccess() {
        VfsRootAccess.allowRootAccess(testRootDisposable, myFixture.testDataPath)
    }
}