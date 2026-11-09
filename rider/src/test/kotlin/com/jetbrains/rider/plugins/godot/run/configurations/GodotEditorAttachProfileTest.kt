package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.process.ProcessInfo
import com.intellij.icons.AllIcons
import com.jetbrains.rider.model.debuggerHelper.PlatformArchitecture
import com.jetbrains.rider.test.shared.constants.TeamCityTags
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertSame
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test

@Tag(TeamCityTags.Plugins.Godot.General)
class GodotEditorAttachProfileTest {
    @Test
    fun `profile name and icon match attach to process`() {
        val processInfo = ProcessInfo(12345, "Godot", "Godot.exe", "")
        val profile = GodotEditorAttachProfile(processInfo, PlatformArchitecture.X64, {}, {})

        assertEquals("12345:Godot", profile.name)
        assertSame(AllIcons.Actions.StartDebugger, profile.icon)
    }
}
