package com.jetbrains.rider.plugins.godot.statistics

import com.intellij.internal.statistic.eventLog.EventLogGroup
import com.intellij.internal.statistic.service.fus.collectors.CounterUsagesCollector
import com.intellij.openapi.project.Project

internal object GodotEditorUsagesCollector : CounterUsagesCollector() {
    private val GROUP = EventLogGroup("rider.godot.editor", 2)
    private val DEBUG_CURRENT_SCENE_REQUESTED = GROUP.registerEvent("debug.current.scene.requested")
    private val OPEN_IN_RIDER_REQUESTED = GROUP.registerEvent("open.in.rider.requested")

    override fun getGroup(): EventLogGroup = GROUP

    fun logDebugCurrentSceneRequested(project: Project) {
        DEBUG_CURRENT_SCENE_REQUESTED.log(project)
    }

    fun logOpenInRiderRequested(project: Project) {
        OPEN_IN_RIDER_REQUESTED.log(project)
    }
}
