package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.ExecutionException
import com.intellij.execution.runners.ProgramRunner
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.jetbrains.rd.ide.model.debuggerAutoAttachModel
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.lifetime.LifetimeDefinition
import com.jetbrains.rd.util.threading.coroutines.asCoroutineDispatcher
import com.jetbrains.rd.util.threading.coroutines.launch
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.projectView.solution
import com.jetbrains.rider.protocol.protocol
import com.jetbrains.rider.run.withSuspendableDiagnosticPort
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.atomic.AtomicBoolean

private val Project.protocolDispatcher get() = protocol.scheduler.asCoroutineDispatcher

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

internal class GodotGameDiagnosticPort private constructor(
    private val project: Project,
    private val lifetime: Lifetime,
    override val sessionId: String,
    address: String,
) : GodotEditorDebugPort {
    /**
     * The environment variables for the game process. They enable Edit and Continue (hot reload),
     * and make the game connect to the diagnostic port.
     */
    override val gameEnvironment: Map<String, String> = withSuspendableDiagnosticPort(
        mapOf(
            DOTNET_MODIFIABLE_ASSEMBLIES to "debug",
            COMPLUS_FORCEENC to "1",
        ),
        address,
    )

    private val resumed = AtomicBoolean(false)

    override suspend fun awaitGameProcessId(): Int {
        val model = project.solution.debuggerAutoAttachModel
        val pid = withContext(project.protocolDispatcher) {
            model.awaitNewProcessConnection.startSuspending(lifetime, sessionId).toInt()
        }
        if (pid <= 0) {
            throw ExecutionException(GodotPluginBundle.message("godot.debug.in.editor.game.not.connected"))
        }
        return pid
    }

    override suspend fun resume() {
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

    companion object {
        private const val DOTNET_MODIFIABLE_ASSEMBLIES = "DOTNET_MODIFIABLE_ASSEMBLIES"
        private const val COMPLUS_FORCEENC = "COMPLUS_FORCEENC"

        suspend fun open(project: Project, lifetime: Lifetime): GodotGameDiagnosticPort = withContext(project.protocolDispatcher) {
            val model = project.solution.debuggerAutoAttachModel
            val server = model.createServer.startSuspending(lifetime, Unit)
            lifetime.onTermination {
                project.protocol.scheduler.queue { model.disposeServer.fire(server.sessionId) }
            }
            GodotGameDiagnosticPort(project, lifetime, server.sessionId, server.connectionAddress)
        }
    }
}
