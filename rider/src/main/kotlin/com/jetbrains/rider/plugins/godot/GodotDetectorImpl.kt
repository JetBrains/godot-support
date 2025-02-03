package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.lifetime
import com.jetbrains.rd.util.threading.coroutines.asCoroutineDispatcher
import com.jetbrains.rider.godot.GodotDetector
import com.jetbrains.rider.godot.GodotProjectInfo
import com.jetbrains.rider.model.aIChatModel
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.protocol.protocol
import kotlinx.coroutines.withContext

class GodotDetectorImpl: GodotDetector {
    override suspend fun godotProjectInfo(project: Project): GodotProjectInfo {
        val result = withContext(project.protocol.scheduler.asCoroutineDispatcher) {
            project.solution.aIChatModel.godotContext.startSuspending(project.lifetime, Unit)
        }
        return GodotProjectInfo(result.isGodot, result.isPureGdScriptProject, result.sdk)
    }
}