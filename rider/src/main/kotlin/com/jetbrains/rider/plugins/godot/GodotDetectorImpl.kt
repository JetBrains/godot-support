package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.lifetime
import com.jetbrains.rd.util.threading.coroutines.nextTrueValue
import com.jetbrains.rider.godot.GodotDetector
import com.jetbrains.rider.godot.GodotProjectInfo
import com.jetbrains.rider.model.aIChatModel
import com.jetbrains.rider.projectView.solution

class GodotDetectorImpl : GodotDetector {
    override suspend fun godotProjectInfo(project: Project): GodotProjectInfo {
        // Wait for the model to be ready before proceeding
        project.solution.aIChatModel.isReady.nextTrueValue()

        val result = project.solution.aIChatModel.godotContext.startSuspending(project.lifetime, Unit)

        return GodotProjectInfo(result.isGodot, result.isPureGdScriptProject, result.sdk)
    }
}