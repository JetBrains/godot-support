package com.jetbrains.rider.godot.community

import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.project.Project

object LspLifecycleUtil {
    private val EP = ExtensionPointName.create<LspRunningStatusProvider>(
        "com.intellij.rider.godot.community.lspStatusProvider")

    fun isLspRunning(project: Project): Boolean =
        EP.extensionList.any { it.isLspRunning(project) }

    fun ensureLspRunning(project: Project) {
        if (!isLspRunning(project)) {
            EP.extensionList.forEach { it.restartLsp(project) }
        }
    }
}
