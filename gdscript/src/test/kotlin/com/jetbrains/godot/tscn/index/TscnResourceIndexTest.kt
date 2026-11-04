package com.jetbrains.godot.tscn.index

import com.intellij.openapi.application.runReadActionBlocking
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.testFramework.junit5.TestApplication
import com.jetbrains.godot.gdscript.GdFixture
import com.jetbrains.godot.gdscript.gdLightFixture
import com.jetbrains.rider.godot.community.tscn.TscnFileType
import com.jetbrains.rider.test.annotations.Subsystem
import com.jetbrains.rider.test.annotations.report.ChecklistItems
import com.jetbrains.rider.test.reporting.SubsystemConstants
import gdscript.utils.ProjectUtil.contentScope
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import tscn.index.impl.TscnResourceIndex

@Subsystem(SubsystemConstants.GODOT)
@ChecklistItems(["Scenes and resources/Resource index"])
@TestApplication
class TscnResourceIndexTest {

    private val gd: GdFixture by gdLightFixture("tscn/multi")

    @Test
    fun testTscnWithScript() {
        gd.fixture.configureByFiles("TscnWithScript.tscn", "Player.cs")

        val key = "res://Player.cs"
        val headers = runReadActionBlocking {
            val searchScope = GlobalSearchScope.getScopeRestrictedByFileTypes(gd.project.contentScope(), TscnFileType)
            TscnResourceIndex.INSTANCE.getScoped(key, gd.project, searchScope)
        }

        // Verify that we found at least one header
        assertTrue(headers.isNotEmpty(),
                   "Expected to find TscnResourceHeader in stub index for path: $key")
    }
}
