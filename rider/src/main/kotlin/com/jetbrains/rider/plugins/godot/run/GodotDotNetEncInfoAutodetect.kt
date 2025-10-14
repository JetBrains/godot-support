package com.jetbrains.rider.plugins.godot.run

import com.intellij.openapi.project.Project
import com.jetbrains.rider.debugger.editAndContinue.DotNetEncInfoAutodetect
import com.jetbrains.rider.model.debuggerWorker.EncInfo
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.runtime.RiderDotNetActiveRuntimeHost
import com.jetbrains.rider.runtime.dotNetCore.DotNetCoreRuntime

class GodotDotNetEncInfoAutodetect(private val project: Project): DotNetEncInfoAutodetect {
    override fun detectEncInfo(dotNetCoreRuntime: DotNetCoreRuntime?, riderDotNetActiveRuntimeHost: RiderDotNetActiveRuntimeHost, exePath: String): EncInfo? {
        if (dotNetCoreRuntime == null)
            return null

        if (GodotProjectDiscoverer.getInstance(project).godot4Path.value != null && riderDotNetActiveRuntimeHost.activeDotNetSdkVersion != null){
            val discoverer = GodotProjectDiscoverer.getInstance(project)
            val tfm = discoverer.godotDescriptor.valueOrNull?.tfm ?: return null
            return EncInfo(tfm)
        }
        return null
    }
}