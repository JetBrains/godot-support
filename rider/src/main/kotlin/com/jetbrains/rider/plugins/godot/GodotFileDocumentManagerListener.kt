package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.components.serviceIfCreated
import com.intellij.openapi.fileEditor.FileDocumentManagerListener
import com.intellij.openapi.project.ProjectManager
import com.intellij.util.application
import com.jetbrains.rd.util.reactive.valueOrDefault
import com.jetbrains.rider.build.BuildHost
import com.jetbrains.rider.build.BuildParameters
import com.jetbrains.rider.model.BuildTarget
import com.jetbrains.rider.model.rdShellModel
import com.jetbrains.rider.protocol.protocolHostIfExists
import com.jetbrains.rider.model.godot.frontendBackend.godotFrontendBackendModel
import com.jetbrains.rider.projectView.solution

class GodotFileDocumentManagerListener : FileDocumentManagerListener {

    // RIDER-127586 Build in Rider when focus moves out so that Inspector in Godot would reload and show exports
    override fun beforeAllDocumentsSaving() {
        val projectManager = serviceIfCreated<ProjectManager>() ?: return
        val openedGodotProjects = projectManager.openProjects.filter { !it.isDisposed
                                                                       && (GodotProjectDiscoverer.getInstance(it).isGodotProject.valueOrDefault(false))
                                                                       && (GodotProjectDiscoverer.getInstance(it).godotDescriptor.valueOrNull?.isPureGdScriptProject == false)

        }.toList()
        for (project in openedGodotProjects)
            application.invokeLater {
                if (project.isDisposed) return@invokeLater
                val buildAutomatically = project.solution.godotFrontendBackendModel.backendSettings.buildAutomatically.valueOrDefault(false)
                if (!buildAutomatically) return@invokeLater
                val isActive = project.protocolHostIfExists?.protocol?.rdShellModel?.isApplicationActive?.valueOrNull
                if (isActive != null && !isActive) {
                    val buildHost = BuildHost.getInstance(project)
                    if (buildHost.ready.value && !buildHost.building.value) {
                        buildHost.requestBuild(BuildParameters(BuildTarget(), emptyList(), silentMode = true)) {}
                    }
                }
            }
    }
}