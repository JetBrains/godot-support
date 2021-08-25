package com.jetbrains.rider.plugins.godot

import com.intellij.execution.ProgramRunnerUtil
import com.intellij.execution.RunManager
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.ide.impl.ProjectUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.WindowManager
import com.intellij.util.BitUtil
import com.jetbrains.rd.framework.impl.RdTask
import com.jetbrains.rd.platform.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rd.util.reactive.AddRemove
import com.jetbrains.rider.debugger.DebuggerInitializingState
import com.jetbrains.rider.debugger.RiderDebugActiveDotNetSessionsTracker
import com.jetbrains.rider.model.godot.frontendBackend.frontendBackendModel
import com.jetbrains.rider.plugins.godot.run.GodotRunConfigurationGenerator
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.run.configurations.remote.DotNetRemoteConfiguration
import com.jetbrains.rider.run.configurations.remote.MonoRemoteConfigType
import com.jetbrains.rider.util.NetUtils
import java.awt.Frame

class FrontendBackendHost(project: Project) : ProtocolSubscribedProjectComponent(project) {
    val model = project.solution.frontendBackendModel

    init {
        model.activateRider.advise(projectComponentLifetime) {
            ProjectUtil.focusProjectWindow(project, true)
            val frame = WindowManager.getInstance().getFrame(project)
            if (frame != null) {
                if (BitUtil.isSet(frame.extendedState, Frame.ICONIFIED))
                    frame.extendedState = BitUtil.set(frame.extendedState, Frame.ICONIFIED, false)
            }
        }

        model.startDebuggerServer.set { lt, _ ->
            val task = RdTask<Int>()
            val runManager = RunManager.getInstance(project)
            val configurationType = ConfigurationTypeUtil.findConfigurationType(MonoRemoteConfigType::class.java)
            val runConfiguration = runManager.createConfiguration(GodotRunConfigurationGenerator.ATTACH_CONFIGURATION_NAME, configurationType.factory)
            val remoteConfiguration = runConfiguration.configuration as DotNetRemoteConfiguration
            remoteConfiguration.listenPortForConnections = true
            remoteConfiguration.port = NetUtils.findFreePort(500013)
            remoteConfiguration.address = "127.0.0.1"

            val processTracker: RiderDebugActiveDotNetSessionsTracker = RiderDebugActiveDotNetSessionsTracker.getInstance(project)
            processTracker.dotNetDebugProcesses.change.advise(projectComponentLifetime) { (event, debugProcess) ->
                if (event == AddRemove.Add) {
                    debugProcess.initializeDebuggerTask.debuggerInitializingState.advise(lt) {
                        if (it == DebuggerInitializingState.Initialized)
                            task.set(remoteConfiguration.port)
                        if (it == DebuggerInitializingState.Canceled)
                            task.set(0)
                    }
                }
            }

            ProgramRunnerUtil.executeConfiguration(runConfiguration, DefaultDebugExecutor.getDebugExecutorInstance())

            task
        }
    }

    companion object {
        fun getInstance(project: Project): FrontendBackendHost = project.getComponent(FrontendBackendHost::class.java)
    }
}