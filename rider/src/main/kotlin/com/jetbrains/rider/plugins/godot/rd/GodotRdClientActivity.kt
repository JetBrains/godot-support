package com.jetbrains.rider.plugins.godot.rd

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.jetbrains.rd.util.threading.coroutines.launch
import com.jetbrains.rider.godot.community.GdScriptProjectLifetimeService
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil

class GodotRdClientActivity : ProjectActivity {
    override suspend fun execute(project: Project) {
        GdScriptProjectLifetimeService.getLifetime(project).launch {
            GodotCommunityUtil.awaitGodotProject(project)
            GodotRdClientService.getInstance(project).start()
        }
    }
}
