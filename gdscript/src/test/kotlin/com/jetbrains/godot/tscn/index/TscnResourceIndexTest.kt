package com.jetbrains.godot.tscn.index

import com.intellij.psi.search.GlobalSearchScope
import com.jetbrains.godot.BasePlatformTestCaseWithTestDataVFSAccess
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.utils.ProjectUtil.contentScope
import tscn.TscnFileType
import tscn.index.impl.TscnResourceIndex
import kotlin.io.path.pathString

class TscnResourceIndexTest : BasePlatformTestCaseWithTestDataVFSAccess() {

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/tscn/multi").pathString
    }

    fun testTscnWithScript() {
        myFixture.configureByFiles("TscnWithScript.tscn", "Player.cs")

        val key = "res://Player.cs"
        val searchScope = GlobalSearchScope.getScopeRestrictedByFileTypes(project.contentScope(), TscnFileType)
        val headers = TscnResourceIndex.INSTANCE.getScoped(key, project, searchScope)

        // Verify that we found at least one header
        assertTrue("Expected to find TscnResourceHeader in stub index for path: $key",
                   headers.isNotEmpty())
    }
}
