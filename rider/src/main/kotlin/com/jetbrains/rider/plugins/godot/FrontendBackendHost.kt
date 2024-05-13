package com.jetbrains.rider.plugins.godot

import com.intellij.execution.ProgramRunnerUtil
import com.intellij.execution.RunManager
import com.intellij.execution.configurations.ConfigurationTypeUtil
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.ide.impl.ProjectUtil
import com.intellij.openapi.client.ClientProjectSession
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.WindowManager
import com.intellij.util.BitUtil
import com.jetbrains.rd.framework.impl.RdTask
import com.jetbrains.rd.platform.util.idea.LifetimedService
import com.jetbrains.rd.protocol.SolutionExtListener
import com.jetbrains.rd.util.firstOrNull
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.AddRemove
import com.jetbrains.rd.util.reactive.adviseNotNull
import com.jetbrains.rd.util.reactive.flowInto
import com.jetbrains.rider.debugger.*
import com.jetbrains.rider.model.debuggerWorker.OutputMessageWithSubject
import com.jetbrains.rider.model.debuggerWorker.OutputSubject
import com.jetbrains.rider.model.debuggerWorker.OutputType
import com.jetbrains.rider.model.godot.frontendBackend.GodotFrontendBackendModel
import com.jetbrains.rider.model.godot.frontendBackend.TestRunnerOutputEventType
import com.jetbrains.rider.plugins.godot.model.debuggerWorker.godotDebuggerWorkerModel
import com.jetbrains.rider.plugins.godot.run.GodotRunConfigurationGenerator
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDotNetRemoteConfiguration
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDotNetRemoteConfigurationFactory
import com.jetbrains.rider.run.configurations.remote.MonoRemoteConfigType
import com.jetbrains.rider.util.NetUtils
import java.awt.Frame

@Service(Service.Level.PROJECT)
class FrontendBackendHost : LifetimedService() {
    class ProtocolListener : SolutionExtListener<GodotFrontendBackendModel> {
        private val debugProcesses = mutableMapOf<Int, DotNetDebugProcess>()
        override fun extensionCreated(lifetime: Lifetime, session: ClientProjectSession, model: GodotFrontendBackendModel) {
            val project = session.project
            model.activateRider.advise(lifetime) {
                ProjectUtil.focusProjectWindow(project, true)
                val frame = WindowManager.getInstance().getFrame(project)
                if (frame != null) {
                    if (BitUtil.isSet(frame.extendedState, Frame.ICONIFIED))
                        frame.extendedState = BitUtil.set(frame.extendedState, Frame.ICONIFIED, false)
                }
            }

            model.onTestRunnerOutputEvent.advise(lifetime) { output->
                debugProcesses.filter{it.key == output.port}.firstOrNull()?.value?.console?.tryWriteMessageToConsoleView(
                    OutputMessageWithSubject(
                        output = "${output.message}\r\n",
                        type = when (output.type) {
                            TestRunnerOutputEventType.Message -> OutputType.Info
                            TestRunnerOutputEventType.Error -> OutputType.Error
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
                        debugProcesses[remoteConfiguration.port] = debugProcess
                        debugProcess.sessionLifetime.onTermination { debugProcesses.remove(remoteConfiguration.port) }
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

            GodotProjectDiscoverer.getInstance(project).godot3Path.adviseNotNull(lifetime){
                model.godotPath.set(it)
            }

            GodotProjectDiscoverer.getInstance(project).godot4Path.adviseNotNull(lifetime){ s ->
                // for Godot3 this is done in a separate place
                RiderDebuggerWorkerModelManager.getModels().adviseNotNull(lifetime){
                    model.backendSettings.enableDebuggerExtensions.flowInto(lifetime,
                        it.value.godotDebuggerWorkerModel.showCustomRenderers)
                }

                model.godotPath.set(s)
            }
        }
    }

    companion object {
        fun getInstance(project: Project): FrontendBackendHost = project.getService(FrontendBackendHost::class.java)
    }
}