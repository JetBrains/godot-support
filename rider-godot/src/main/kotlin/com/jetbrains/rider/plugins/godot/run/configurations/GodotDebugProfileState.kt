package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.KillableProcessHandler
import com.intellij.execution.process.ProcessAdapter
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.Key
import com.intellij.util.ui.UIUtil
import com.jetbrains.rd.platform.util.application
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.lifetime.onTermination
import com.jetbrains.rdclient.util.idea.toIOFile
import com.jetbrains.rider.debugger.DebuggerHelperHost
import com.jetbrains.rider.debugger.DebuggerWorkerProcessHandler
import com.jetbrains.rider.debugger.tryWriteMessageToConsoleView
import com.jetbrains.rider.model.debuggerWorker.OutputMessageWithSubject
import com.jetbrains.rider.model.debuggerWorker.OutputSubject
import com.jetbrains.rider.model.debuggerWorker.OutputType
import com.jetbrains.rider.plugins.godot.GodotServer
import com.jetbrains.rider.run.WorkerRunInfo
import com.jetbrains.rider.run.configurations.remote.MonoConnectRemoteProfileState
import com.jetbrains.rider.util.NetUtils
import org.jetbrains.concurrency.AsyncPromise
import org.jetbrains.concurrency.Promise
import java.io.File

class GodotDebugProfileState(private val remoteConfiguration: GodotDebugRunConfiguration, executionEnvironment: ExecutionEnvironment)
    : MonoConnectRemoteProfileState(remoteConfiguration, executionEnvironment) {
    private val logger = Logger.getInstance(GodotDebugProfileState::class.java)
    private val project = executionEnvironment.project

    override fun execute(executor: Executor, runner: ProgramRunner<*>, workerProcessHandler: DebuggerWorkerProcessHandler, lifetime: Lifetime): ExecutionResult {
        val path = remoteConfiguration.godotPath
        val args = mutableListOf(path, "--path", project.basePath.toString())
        val godotPort = remoteConfiguration.port
        val commandLine = GeneralCommandLine(args)
        commandLine.environment.set("GODOT_MONO_DEBUGGER_AGENT", "--debugger-agent=transport=dt_socket,address=127.0.0.1:$godotPort,server=n,suspend=y")
        commandLine.workDirectory = File(path).parentFile

        val monoConnectResult = super.execute(executor, runner, workerProcessHandler)

        workerProcessHandler.debuggerWorkerRealHandler.addProcessListener(object : ProcessAdapter() {
            override fun startNotified(event: ProcessEvent) {
                val godotProcessHandler = KillableProcessHandler(commandLine) // starts a process

                lifetime.onTermination {
                    if (!godotProcessHandler.isProcessTerminated) {
                        godotProcessHandler.destroyProcess()
                    }
                }

                godotProcessHandler.addProcessListener(object : ProcessListener {
                    override fun onTextAvailable(processEvent: ProcessEvent, key: Key<*>) {
                        monoConnectResult.executionConsole.tryWriteMessageToConsoleView(
                                OutputMessageWithSubject(processEvent.text, OutputType.Info, OutputSubject.Default)
                        )
                    }

                    override fun processTerminated(processEvent: ProcessEvent) {
                        monoConnectResult.executionConsole.tryWriteMessageToConsoleView(OutputMessageWithSubject("Godot player terminated with exit code " + processEvent.exitCode, OutputType.Warning, OutputSubject.Default))
                        application.invokeLater {
                            monoConnectResult.processHandler.detachProcess()
                        }
                    }

                    override fun startNotified(processEvent: ProcessEvent) {
                        monoConnectResult.executionConsole.tryWriteMessageToConsoleView(OutputMessageWithSubject("Godot player started." + "\r\n", OutputType.Info, OutputSubject.Default))
                    }
                })

                godotProcessHandler.startNotify()
                super.startNotified(event)
            }
        })

        return monoConnectResult
    }

    override fun createWorkerRunCmd(lifetime: Lifetime, helper: DebuggerHelperHost, port: Int): Promise<WorkerRunInfo> {
        val result = AsyncPromise<WorkerRunInfo>()
        application.executeOnPooledThread {
            try {
                val path = GodotServer.getGodotPath(project)
                if (path.isEmpty() || !path.toIOFile().exists())
                    result.setError("Failed to determine path to Godot executable.")
                else {
                    remoteConfiguration.godotPath = path

                    val godotPort = NetUtils.findFreePort(500013, setOf(port))
                    remoteConfiguration.port = godotPort

                    UIUtil.invokeLaterIfNeeded {
                        logger.trace("DebuggerWorker port: $port")
                        logger.trace("Connecting to Godot with port: ${remoteConfiguration.port}")
                        super.createWorkerRunCmd(lifetime, helper, port).onSuccess { result.setResult(it) }.onError { result.setError(it) }
                    }
                }
            }
            catch (e: Exception) {
                result.setError(e)
            }
        }
        return result
    }
}