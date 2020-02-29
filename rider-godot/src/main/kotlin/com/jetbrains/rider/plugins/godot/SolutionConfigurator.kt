package com.jetbrains.rider.plugins.godot

import com.intellij.execution.RunManager
import com.intellij.openapi.project.Project
import com.jetbrains.rdclient.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rider.projectView.SolutionConfigurationManager
import com.jetbrains.rider.projectView.solution

class SolutionConfigurator(project: Project) : ProtocolSubscribedProjectComponent(project) {

    init {
        project.solution.isLoaded.advise(componentLifetime){
            if (it) {
                val projectDiscoverer = GodotProjectDiscoverer.getInstance(project)
                if (projectDiscoverer.getIsGodotProject) {
                    val runManager = RunManager.getInstance(project)
                    // Tools solution configuration is a default one in Godot Editor
                    val solutionManager = SolutionConfigurationManager.getInstance(project)
                    val active = solutionManager.activeConfigurationAndPlatform
                    if (active == null || !active.configuration.contains("Tools")) {
                        val tools = solutionManager.solutionConfigurationsAndPlatforms.firstOrNull { it.configuration.contains("Tools") }
                        if (tools != null){
                            solutionManager.activeConfigurationAndPlatform = tools
                        }
                    }
                }
            }
        }
    }
}