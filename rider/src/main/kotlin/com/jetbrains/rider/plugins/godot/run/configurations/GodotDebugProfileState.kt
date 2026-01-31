package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.KillableProcess
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.AnsiEscapeDecoder
import com.intellij.execution.process.KillableProcessHandler
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.ui.ConsoleView
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.openapi.rd.createNestedDisposable
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.SystemInfo
import com.intellij.util.NetworkUtils
import com.jetbrains.rd.util.addUnique
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.flowInto
import com.jetbrains.rider.debugger.DebuggerHelperHost
import com.jetbrains.rider.debugger.DebuggerWorkerProcessHandler
import com.jetbrains.rider.debugger.tryWriteMessageToConsoleView
import com.jetbrains.rider.model.debuggerWorker.DebuggerWorkerModel
import com.jetbrains.rider.model.debuggerWorker.OutputMessageWithSubject
import com.jetbrains.rider.model.debuggerWorker.OutputSubject
import com.jetbrains.rider.model.debuggerWorker.OutputType
import com.jetbrains.rider.model.godot.frontendBackend.godotFrontendBackendModel
import com.jetbrains.rider.plugins.godot.model.debuggerWorker.godotDebuggerWorkerModel
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.run.ExternalConsoleMediator
import com.jetbrains.rider.run.WorkerRunInfo
import com.jetbrains.rider.run.configurations.TerminalMode
import com.jetbrains.rider.run.configurations.remote.RemoteConfiguration
import com.jetbrains.rider.run.createEmptyConsoleCommandLine
import com.jetbrains.rider.run.environment.ExecutableType
import com.jetbrains.rider.shared.run.withRawParameters

class GodotDebugProfileState(private val exeConfiguration: GodotDebugRunConfiguration, private val remoteConfiguration: RemoteConfiguration, executionEnvironment: ExecutionEnvironment)
    : GodotMonoConnectRemoteProfileState(remoteConfiguration, executionEnvironment) {
    private val ansiEscapeDecoder = AnsiEscapeDecoder()

    override suspend fun createDebuggerWorker(
        workerCmd: GeneralCommandLine,
        protocolModel: DebuggerWorkerModel,
        protocolServerPort: Int,
        projectLifetime: Lifetime
    ): DebuggerWorkerProcessHandler {

        val debuggerWorkerLifetime = projectLifetime.createNested()

        val frontendBackendModel = executionEnvironment.project.solution.godotFrontendBackendModel
        frontendBackendModel.backendSettings.enableDebuggerExtensions.flowInto(debuggerWorkerLifetime,
            protocolModel.godotDebuggerWorkerModel.showCustomRenderers)

        return super.createDebuggerWorker(workerCmd, protocolModel, protocolServerPort, projectLifetime).apply {
            addProcessListener(object : ProcessAdapter() {
                override fun processTerminated(event: ProcessEvent) { debuggerWorkerLifetime.terminate() }
            })
        }
    }

    override fun execute(executor: Executor?, runner: ProgramRunner<*>): ExecutionResult? {
        throw UnsupportedOperationException("Should use overload with session")
    }

    override suspend fun execute(executor: Executor, runner: ProgramRunner<*>, workerProcessHandler: DebuggerWorkerProcessHandler): ExecutionResult {
        throw UnsupportedOperationException("Should use overload with session")
    }

    override suspend fun execute(executor: Executor, runner: ProgramRunner<*>, workerProcessHandler: DebuggerWorkerProcessHandler, lifetime: Lifetime): ExecutionResult {
        val envs = exeConfiguration.parameters.envs.toMutableMap()
        envs.addUnique(lifetime, "GODOT_MONO_DEBUGGER_AGENT", "--debugger-agent=transport=dt_socket,address=127.0.0.1:${remoteConfiguration.port},server=n,suspend=y")
        val runCommandLine = createEmptyConsoleCommandLine(exeConfiguration.parameters.terminalMode, if (SystemInfo.isWindows) ExecutableType.Windows else ExecutableType.Console)
            .withEnvironment(envs)
            .withParentEnvironmentType(if (exeConfiguration.parameters.isPassParentEnvs) {
                GeneralCommandLine.ParentEnvironmentType.CONSOLE
            } else {
                GeneralCommandLine.ParentEnvironmentType.NONE
            })
            .withExePath(exeConfiguration.parameters.exePath)
            .withWorkDirectory(exeConfiguration.parameters.workingDirectory)
            .withRawParameters(exeConfiguration.parameters.programParameters)

        val commandLineString = runCommandLine.commandLineString
        val monoConnectResult = super.execute(executor, runner, workerProcessHandler)
        workerProcessHandler.addProcessListener(object : ProcessAdapter() {
            override fun startNotified(event: ProcessEvent) {
                val targetProcessHandler = if (exeConfiguration.parameters.terminalMode == TerminalMode.ExternalConsole)
                    ExternalConsoleMediator.createProcessHandler(runCommandLine)
                else
                    KillableProcessHandler(runCommandLine)

                lifetime.onTermination {
                    if (!targetProcessHandler.isProcessTerminated) {
                        (targetProcessHandler as KillableProcess).killProcess()
                    }
                }

                targetProcessHandler.addProcessListener(object : ProcessListener {
                    override fun onTextAvailable(processEvent: ProcessEvent, key: Key<*>) {
                        ansiEscapeDecoder.escapeText(processEvent.text, ProcessOutputTypes.STDOUT) { textChunk, attributes ->
                            val chunkContentType = ConsoleViewContentType.getConsoleViewType(attributes)
                            (monoConnectResult.executionConsole as? ConsoleView)?.print(textChunk, chunkContentType)
                        }
                    }

                    override fun processTerminated(processEvent: ProcessEvent) {
                        monoConnectResult.executionConsole.tryWriteMessageToConsoleView(OutputMessageWithSubject(output = "Process \"$commandLineString\" terminated with exit code ${processEvent.exitCode}.\r\n", type = OutputType.Warning, subject = OutputSubject.Default))
                    }
                })

                targetProcessHandler.startNotify()
                super.startNotified(event)
            }
        }, lifetime.createNestedDisposable())

        return monoConnectResult
    }

    override suspend fun createWorkerRunInfo(lifetime: Lifetime, helper: DebuggerHelperHost, port: Int): WorkerRunInfo {
        val runCmd = super.createWorkerRunInfo(lifetime, helper, port)

        remoteConfiguration.listenPortForConnections = true
        remoteConfiguration.port = NetworkUtils.findFreePort(500013, setOf(port))
        remoteConfiguration.address = "127.0.0.1"

        return runCmd
    }
}