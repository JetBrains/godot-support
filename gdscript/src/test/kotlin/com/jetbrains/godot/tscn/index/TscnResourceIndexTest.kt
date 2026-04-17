package com.jetbrains.godot.tscn.index

import com.intellij.psi.search.GlobalSearchScope
import com.jetbrains.godot.GdCodeInsightTestBaseWithVFSAccess
import com.jetbrains.godot.getBaseTestDataPath
import gdscript.utils.ProjectUtil.contentScope
import com.jetbrains.rider.godot.community.tscn.TscnFileType
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tscn.index.impl.TscnResourceIndex
import kotlin.io.path.pathString

class TscnResourceIndexTest : GdCodeInsightTestBaseWithVFSAccess() {

    override fun getTestDataPath(): String {
        return getBaseTestDataPath().resolve("testData/tscn/multi").pathString
    }

    @Test
    fun testTscnWithScript() {
        myFixture.configureByFiles("TscnWithScript.tscn", "Player.cs")

        val key = "res://Player.cs"
        val searchScope = GlobalSearchScope.getScopeRestrictedByFileTypes(project.contentScope(), TscnFileType)
        val headers = TscnResourceIndex.INSTANCE.getScoped(key, project, searchScope)

        // Verify that we found at least one header
        assertTrue(
            headers.isNotEmpty(),
            "Expected to find TscnResourceHeader in stub index for path: $key"
        )
    }
}
