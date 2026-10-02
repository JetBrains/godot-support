package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.ExecutionResult
import com.intellij.execution.process.ProcessInfo
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.ConsoleView
import com.intellij.openapi.diagnostic.thisLogger
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.flowInto
import com.jetbrains.rd.util.threading.coroutines.launch
import com.jetbrains.rd.util.threading.coroutines.nextNotNullValueAsync
import com.jetbrains.rider.debugger.DebuggerWorkerProcessHandler
import com.jetbrains.rider.model.debuggerHelper.PlatformArchitecture
import com.jetbrains.rider.model.debuggerWorker.DebuggerWorkerModel
import com.jetbrains.rider.model.godot.frontendBackend.godotFrontendBackendModel
import com.jetbrains.rider.plugins.godot.model.debuggerWorker.godotDebuggerWorkerModel
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.run.dotNetCore.DotNetCoreAttachProfileState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.time.Duration.Companion.seconds

/**
 * Attaches to the game process the Godot editor has launched and stops the playing scene
 * as soon as the debug session is over.
 *
 * The runtime of the game is suspended by its diagnostic port until [resumeGame] is called, see
 * [GodotInEditorDebugExecutorFactory].
 */
class GodotInEditorAttachProfileState(
    processInfo: ProcessInfo,
    executionEnvironment: ExecutionEnvironment,
    targetPlatform: PlatformArchitecture,
    private val resumeGame: suspend () -> Unit,
    private val terminateCleanup: () -> Unit,
) : DotNetCoreAttachProfileState(processInfo, executionEnvironment, targetPlatform) {

    override fun bindSettings(lifetime: Lifetime, workerModel: DebuggerWorkerModel) {
        val project = executionEnvironment.project
        project.solution.godotFrontendBackendModel.backendSettings.enableDebuggerExtensions.flowInto(
            lifetime,
            workerModel.godotDebuggerWorkerModel.showCustomRenderers,
        )
        super.bindSettings(lifetime, workerModel)
    }

    override suspend fun beforeWorkerStart(lifetime: Lifetime, environment: ExecutionEnvironment) {
        lifetime.onTermination { terminateCleanup() }
        super.beforeWorkerStart(lifetime, environment)
    }

    override suspend fun execute(
        workerConsole: ConsoleView,
        workerProcessHandler: DebuggerWorkerProcessHandler,
        lifetime: Lifetime,
    ): ExecutionResult {
        lifetime.launch(Dispatchers.Default) {
            val registeredPid = withTimeoutOrNull(REGISTRATION_TIMEOUT) {
                workerProcessHandler.debuggerRegisteredProcessIdAvailable.nextNotNullValueAsync(lifetime).await()
            }
            if (registeredPid == null) {
                thisLogger().warn(
                    "[GODOT] the debugger did not register for the startup of the game process " +
                        "${processInfo.pid} within $REGISTRATION_TIMEOUT, resuming the game anyway"
                )
            }
            resumeGame()
        }
        return super.execute(workerConsole, workerProcessHandler, lifetime)
    }

    private companion object {
        private val REGISTRATION_TIMEOUT = 30.seconds
    }
}
