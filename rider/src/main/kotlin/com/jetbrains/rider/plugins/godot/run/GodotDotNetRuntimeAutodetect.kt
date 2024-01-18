package com.jetbrains.rider.plugins.godot.run

import com.intellij.openapi.project.Project
import com.jetbrains.rider.model.RdTargetFrameworkId
import com.jetbrains.rider.model.RunnableProjectKind
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.runtime.DotNetRuntimeAutodetect
import com.jetbrains.rider.runtime.RiderDotNetActiveRuntimeHost
import com.jetbrains.rider.runtime.RuntimeDetectionResult
import com.jetbrains.rider.runtime.dotNetCore.DotNetCoreRuntime

class GodotDotNetRuntimeAutodetect(private val project: Project): DotNetRuntimeAutodetect {
    override fun detectRuntime(dotNetCoreRuntime: DotNetCoreRuntime?, riderDotNetActiveRuntimeHost: RiderDotNetActiveRuntimeHost, projectKind: RunnableProjectKind?, tfm: RdTargetFrameworkId?, exePath: String): RuntimeDetectionResult? {
        if (dotNetCoreRuntime == null)
            return null

        if (GodotProjectDiscoverer.getInstance(project).godotCorePath.value != null)
            return RuntimeDetectionResult.create(dotNetCoreRuntime)

        return null
    }
}