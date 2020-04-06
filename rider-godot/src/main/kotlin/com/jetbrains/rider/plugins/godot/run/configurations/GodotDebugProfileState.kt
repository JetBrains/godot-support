package com.jetbrains.rider.plugins.unity.run.configurations

import com.intellij.execution.ExecutionResult
import com.intellij.execution.Executor
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ProgramRunner
import com.intellij.openapi.diagnostic.Logger
import com.intellij.util.ui.UIUtil
import com.jetbrains.rd.platform.util.application
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.lifetime.onTermination
import com.jetbrains.rider.debugger.DebuggerHelperHost
import com.jetbrains.rider.debugger.DebuggerWorkerProcessHandler
import com.jetbrains.rider.plugins.godot.GodotServer
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfiguration
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

        application.executeOnPooledThread {
            Thread.sleep(3000)
            val path = GodotServer.getGodotPath(project)
            val args = mutableListOf(path, "--path", project.basePath.toString())
            val processBuilder = ProcessBuilder(args)
            val godotPort = remoteConfiguration.port
            processBuilder.environment().set("GODOT_MONO_DEBUGGER_AGENT", "--debugger-agent=transport=dt_socket,address=127.0.0.1:$godotPort,server=n")
            processBuilder.directory(File(path).parentFile)
            val process = processBuilder.start()
            lifetime.onTermination {
                if (process.isAlive) process.destroyForcibly()
            }
        }
        return super.execute(executor, runner, workerProcessHandler)
    }

    override fun createWorkerRunCmd(lifetime: Lifetime, helper: DebuggerHelperHost, port: Int): Promise<WorkerRunInfo> {

        val result = AsyncPromise<WorkerRunInfo>()
        application.executeOnPooledThread {
            try {
                val godotPort = NetUtils.findFreePort(500013)

                remoteConfiguration.listenPortForConnections = true
                remoteConfiguration.port = godotPort

                UIUtil.invokeLaterIfNeeded {
                    logger.trace("DebuggerWorker port: $port")
                    logger.trace("Connecting to Godot with port: ${remoteConfiguration.port}")
                    super.createWorkerRunCmd(lifetime, helper, port).onSuccess { result.setResult(it) }.onError { result.setError(it) }
                }
            }
            catch (e: Exception) {
                result.setError(e)
            }
        }
        return result
    }
}