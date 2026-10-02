package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.CantRunException
import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.RuntimeConfigurationError
import com.intellij.execution.configurations.WithoutOwnBeforeRunSteps
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.runners.RunConfigurationWithSuppressedDefaultRunAction
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.xdebugger.attach.LocalAttachHost
import com.jetbrains.rd.ide.model.debuggerAutoAttachModel
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.lifetime.LifetimeDefinition
import com.jetbrains.rd.util.threading.coroutines.asCoroutineDispatcher
import com.jetbrains.rd.util.threading.coroutines.launch
import com.jetbrains.rider.debugger.DebuggerHelperHost
import com.jetbrains.rider.debugger.IRiderDebuggable
import com.jetbrains.rider.model.godot.frontendGodot.FrontendGodotModel
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.plugins.godot.rd.GodotRdClientService
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.protocol.protocol
import com.jetbrains.rider.run.configurations.AsyncExecutorFactory
import com.jetbrains.rider.run.configurations.RiderAsyncRunConfiguration
import com.jetbrains.rider.run.withSuspendableDiagnosticPort
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.JComponent
import javax.swing.JPanel
import kotlin.time.Duration.Companion.seconds

/**
 * Calls on the backend model may only be started on the scheduler of the frontend protocol, while the launch itself
 * runs on a background thread, so every such call has to hop onto this dispatcher first.
 */
private val Project.protocolDispatcher get() = protocol.scheduler.asCoroutineDispatcher

/**
 * Asks the running Godot editor to play the currently edited scene and attaches the .NET Core debugger
 * to the game process the editor has spawned. Godot 4 only, since the game runs on CoreCLR there.
 */
class GodotInEditorDebugRunConfiguration(project: Project, factory: ConfigurationFactory) : RiderAsyncRunConfiguration(
    GodotPluginBundle.message("godot.debug.in.editor.configuration.name"),
    project,
    factory,
    { Editor() },
    GodotInEditorDebugExecutorFactory(project),
), IRiderDebuggable, RunConfigurationWithSuppressedDefaultRunAction, WithoutOwnBeforeRunSteps {

    override fun checkConfiguration() {
        if (GodotProjectDiscoverer.getInstance(project).godot4Path.value == null) {
            throw RuntimeConfigurationError(GodotPluginBundle.message("godot.debug.in.editor.not.godot4"))
        }
        if (!GodotRdClientService.getInstance(project).isConnected) {
            throw RuntimeConfigurationError(GodotPluginBundle.message("godot.debug.in.editor.not.connected"))
        }
    }

    private class Editor : SettingsEditor<GodotInEditorDebugRunConfiguration>() {
        override fun resetEditorFrom(configuration: GodotInEditorDebugRunConfiguration) {
        }

        override fun applyEditorTo(configuration: GodotInEditorDebugRunConfiguration) {
        }

        override fun createEditor(): JComponent = JPanel()
    }
}

class GodotInEditorDebugExecutorFactory(private val project: Project) : AsyncExecutorFactory {
    override suspend fun create(executorId: String, environment: ExecutionEnvironment, lifetime: Lifetime): RunProfileState {
        if (executorId != DefaultDebugExecutor.EXECUTOR_ID) {
            throw CantRunException(GodotPluginBundle.message("godot.debug.in.editor.debug.only"))
        }

        val rdClientService = GodotRdClientService.getInstance(project)
        if (!rdClientService.isConnected) {
            throw ExecutionException(GodotPluginBundle.message("godot.debug.in.editor.not.connected"))
        }

        val cleanupLifetime = lifetime.createNested()
        var stateCreated = false
        try {
            val state = withContext(Dispatchers.Default) {
                val diagnosticPort = GodotGameDiagnosticPort.open(project, lifetime) { port ->
                    environment.callback = createGodotInEditorDebugCallback(cleanupLifetime, environment.callback) {
                        try {
                            port.resume()
                        } finally {
                            try {
                                rdClientService.stopPlayingScene()
                            } finally {
                                port.dispose()
                            }
                        }
                    }
                }
                if (!rdClientService.playCurrentSceneForDebug(diagnosticPort.gameArgument)) {
                    throw ExecutionException(GodotPluginBundle.message("godot.debug.in.editor.play.failed"))
                }
                val gamePid = diagnosticPort.awaitGameProcessId()
                val processInfo = LocalAttachHost.INSTANCE.getProcessListAsync().firstOrNull { it.pid == gamePid }
                    ?: throw ExecutionException(GodotPluginBundle.message("godot.debug.in.editor.game.process.not.found"))
                val platform = withContext(project.protocolDispatcher) {
                    DebuggerHelperHost.getInstance(project).getProcessArchitecture(lifetime, gamePid)
                }
                GodotInEditorAttachProfileState(
                    processInfo,
                    environment,
                    platform,
                    diagnosticPort::resume,
                    cleanupLifetime::terminate,
                )
            }
            stateCreated = true
            return state
        } finally {
            if (!stateCreated) cleanupLifetime.terminate()
        }
    }
}

/** Runs cleanup when startup fails or the cleanup lifetime ends. */
@ApiStatus.Internal
fun createGodotInEditorDebugCallback(
    cleanupLifetime: LifetimeDefinition,
    previousCallback: ProgramRunner.Callback?,
    cleanup: suspend () -> Unit,
): ProgramRunner.Callback {
    cleanupLifetime.lifetime.launch(Dispatchers.Default, start = CoroutineStart.UNDISPATCHED) {
        try {
            awaitCancellation()
        } finally {
            withContext(NonCancellable) {
                cleanup()
            }
        }
    }

    return object : ProgramRunner.Callback {
        override fun processNotStarted(error: Throwable?) {
            cleanupLifetime.terminate()
            previousCallback?.processNotStarted(error)
        }

        override fun processStarted(descriptor: RunContentDescriptor?) {
            if (descriptor == null) cleanupLifetime.terminate()
            previousCallback?.processStarted(descriptor)
        }
    }
}

private class GodotGameDiagnosticPort private constructor(
    private val project: Project,
    private val lifetime: Lifetime,
    private val sessionId: String,
    address: String,
) {
    val gameArgument: String = "${FrontendGodotModel.diagnosticPortsArgument}=${suspendableDiagnosticPortsValue(address)}"

    private val resumed = AtomicBoolean(false)
    private val disposed = AtomicBoolean(false)

    suspend fun awaitGameProcessId(): Int {
        val model = project.solution.debuggerAutoAttachModel
        val pid = withTimeoutOrNull(CONNECT_TIMEOUT) {
            withContext(project.protocolDispatcher) {
                model.awaitNewProcessConnection.startSuspending(lifetime, sessionId).toInt()
            }
        }
        if (pid == null || pid <= 0) {
            throw ExecutionException(GodotPluginBundle.message("godot.debug.in.editor.game.not.connected"))
        }
        return pid
    }

    suspend fun resume() {
        if (!resumed.compareAndSet(false, true)) return
        withContext(NonCancellable + project.protocolDispatcher) {
            try {
                project.solution.debuggerAutoAttachModel.resumeCurrentProcess.startSuspending(lifetime, sessionId)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                // The game either never connected or is already gone, so there is nothing left to resume.
                thisLogger().info("[GODOT] failed to resume the game process: $e")
            }
        }
    }

    fun dispose() {
        if (!disposed.compareAndSet(false, true)) return
        project.protocol.scheduler.queue {
            project.solution.debuggerAutoAttachModel.disposeServer.fire(sessionId)
        }
    }

    companion object {
        private const val DOTNET_DIAGNOSTIC_PORTS = "DOTNET_DiagnosticPorts"

        // Playing a scene builds the project first. This means this has to cover both build
        // and connection. This value might not cover big projects, at that point we tell
        // the user they should build first.
        private val CONNECT_TIMEOUT = 60.seconds

        suspend fun open(
            project: Project,
            lifetime: Lifetime,
            onCreated: (GodotGameDiagnosticPort) -> Unit,
        ): GodotGameDiagnosticPort = withContext(project.protocolDispatcher) {
            val server = project.solution.debuggerAutoAttachModel.createServer.startSuspending(lifetime, Unit)
            GodotGameDiagnosticPort(project, lifetime, server.sessionId, server.connectionAddress).also { port ->
                onCreated(port)
            }
        }

        /**
         * `connect` makes the game dial out to us, `suspend` freezes its runtime until we resume it. The same format
         * Rider uses when it starts a process with a diagnostic port itself.
         */
        private fun suspendableDiagnosticPortsValue(address: String): String =
            withSuspendableDiagnosticPort(emptyMap(), address).getValue(DOTNET_DIAGNOSTIC_PORTS)
    }
}
