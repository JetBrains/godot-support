package com.jetbrains.rider.godot.community.actions

import com.intellij.ide.ActivityTracker
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GodotToolbarUpdateActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        coroutineScope {
            launch {
                // Update all actions and toolbars to reflect Godot executable path changes
                GodotCommunityUtil.getGodotExecutablePathFlow(project).collectLatest {
                    ActivityTracker.getInstance().inc()
                }
            }
        }
    }
}