package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.components.serviceIfCreated
import com.intellij.openapi.fileEditor.FileDocumentManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectManager
import com.intellij.util.application
import com.jetbrains.rd.util.reactive.ViewableMap
import com.jetbrains.rd.util.reactive.valueOrDefault
import com.jetbrains.rider.build.BuildHost
import com.jetbrains.rider.build.BuildParameters
import com.jetbrains.rider.build.BuildToolWindowService
import com.jetbrains.rider.model.BuildMessageKind
import com.jetbrains.rider.model.BuildTarget
import com.jetbrains.rider.model.MessageBuildEvent
import com.jetbrains.rider.model.godot.frontendBackend.GodotEditorState
import com.jetbrains.rider.model.godot.frontendBackend.godotFrontendBackendModel
import com.jetbrains.rider.model.rdShellModel
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.protocol.protocolHostIfExists

class GodotFileDocumentManagerListener : FileDocumentManagerListener {

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
                // it makes no sense to build, if Godot is not running
                if (project.solution.godotFrontendBackendModel.editorState.valueOrDefault(GodotEditorState.Disconnected) != GodotEditorState.Connected)
                    return@invokeLater
                val isActive = project.protocolHostIfExists?.protocol?.rdShellModel?.isApplicationActive?.valueOrNull
                if (isActive != null && !isActive) {
                    val buildHost = BuildHost.getInstance(project)
                    if (buildHost.ready.value && !buildHost.building.value) {
                        val mainProjectPath = GodotProjectDiscoverer.getInstance(project).godotDescriptor.valueOrNull?.mainProjectPath ?: return@invokeLater
                        buildHost.requestBuild(BuildParameters(BuildTarget(), listOf(mainProjectPath), silentMode = true)) {
                            showAutoBuildNotification(project)
                        }
                    }
                }
            }
    }

    private fun showAutoBuildNotification(project: Project) {
        val context = BuildToolWindowService.getInstance(project).getOrCreateContext(project)
        //  context.showToolWindowIfHidden(false) // Is is too annoying, when the toolwindows pops-up every time, RIDER-134163
        val message = GodotPluginBundle.message("build.automatically.text")
        context.addOutputMessage(MessageBuildEvent(message = message,
                                                   projectId = null,
                                                   kind = BuildMessageKind.Message), ViewableMap())
    }
}