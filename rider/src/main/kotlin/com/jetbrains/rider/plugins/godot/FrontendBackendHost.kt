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
import com.jetbrains.rd.util.lifetime.onTermination
import com.jetbrains.rd.util.reactive.*
import com.jetbrains.rider.debugger.DebuggerInitializingState
import com.jetbrains.rider.debugger.DotNetDebugProcess
import com.jetbrains.rider.debugger.RiderDebugActiveDotNetSessionsTracker
import com.jetbrains.rider.debugger.tryWriteMessageToConsoleView
import com.jetbrains.rider.model.debuggerWorker.OutputMessageWithSubject
import com.jetbrains.rider.model.debuggerWorker.OutputSubject
import com.jetbrains.rider.model.debuggerWorker.OutputType
import com.jetbrains.rider.model.godot.frontendBackend.GameOutputEventType
import com.jetbrains.rider.model.godot.frontendBackend.godotFrontendBackendModel
import com.jetbrains.rider.plugins.godot.run.GodotRunConfigurationGenerator
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDotNetRemoteConfiguration
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDotNetRemoteConfigurationFactory
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.run.configurations.remote.MonoRemoteConfigType
import com.jetbrains.rider.util.NetUtils
import java.awt.Frame

class FrontendBackendHost(project: Project) : ProtocolSubscribedProjectComponent(project) {
    private val model = project.solution.godotFrontendBackendModel
    private val myDebugProcessProperty: IProperty<DotNetDebugProcess?> = Property(null)

    init {
        model.activateRider.advise(projectComponentLifetime) {
            ProjectUtil.focusProjectWindow(project, true)
            val frame = WindowManager.getInstance().getFrame(project)
            if (frame != null) {
                if (BitUtil.isSet(frame.extendedState, Frame.ICONIFIED))
                    frame.extendedState = BitUtil.set(frame.extendedState, Frame.ICONIFIED, false)
            }
        }

        model.onGameOutputEvent.advise(projectComponentLifetime) {
            myDebugProcessProperty.value?.console?.tryWriteMessageToConsoleView(
                OutputMessageWithSubject(
                    output = "${it.message}\r\n",
                    type = when (it.type) {
                        GameOutputEventType.Message -> OutputType.Info
                        GameOutputEventType.Error -> OutputType.Error
                    },
                    subject = OutputSubject.Default
                )
            )
        }

        model.startDebuggerServer.set { lt, _ ->
            val task = RdTask<Int>()
            val runManager = RunManager.getInstance(project)
            val configurationType = ConfigurationTypeUtil.findConfigurationType(MonoRemoteConfigType::class.java)
            val runConfiguration = runManager.createConfiguration(
                GodotRunConfigurationGenerator.ATTACH_CONFIGURATION_NAME,
                GodotDotNetRemoteConfigurationFactory(configurationType)
            )
            val remoteConfiguration = runConfiguration.configuration as GodotDotNetRemoteConfiguration
            remoteConfiguration.listenPortForConnections = true
            remoteConfiguration.port = NetUtils.findFreePort(500013)
            remoteConfiguration.address = "127.0.0.1"

            val processTracker: RiderDebugActiveDotNetSessionsTracker =
                RiderDebugActiveDotNetSessionsTracker.getInstance(project)
            processTracker.dotNetDebugProcesses.change.advise(lt) { (event, debugProcess) ->
                if (event == AddRemove.Add) {
                    debugProcess.initializeDebuggerTask.debuggerInitializingState.advise(lt) {
                        if (it == DebuggerInitializingState.Initialized) {
                            myDebugProcessProperty.set(debugProcess)
                            debugProcess.sessionLifetime.onTermination { myDebugProcessProperty.set(null) }
                            task.set(remoteConfiguration.port)
                        }
                        if (it == DebuggerInitializingState.Canceled)
                            task.set(0)
                    }
                }
            }

            ProgramRunnerUtil.executeConfiguration(runConfiguration, DefaultDebugExecutor.getDebugExecutorInstance())

            task
        }

        GodotProjectDiscoverer.getInstance(project).godotPath.adviseNotNull(projectComponentLifetime){
            model.godotPath.set(it)
        }
    }

    companion object {
        fun getInstance(project: Project): FrontendBackendHost = project.getComponent(FrontendBackendHost::class.java)
    }
}