package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.diagnostic.rethrowControlFlowException
import com.intellij.execution.ExecutionException
import com.intellij.execution.ProgramRunnerUtil
import com.intellij.execution.RunManager
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.process.ProcessInfo
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.application.EDT
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.platform.ide.progress.TaskCancellation
import com.intellij.platform.ide.progress.withBackgroundProgress
import com.intellij.xdebugger.attach.LocalAttachHost
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.lifetime.LifetimeDefinition
import com.jetbrains.rd.util.lifetime.isAlive
import com.jetbrains.rd.util.threading.coroutines.asCoroutineDispatcher
import com.jetbrains.rd.util.threading.coroutines.launch
import com.jetbrains.rider.debugger.DebuggerHelperHost
import com.jetbrains.rider.debugger.attach.RiderAttachConfiguration
import com.jetbrains.rider.debugger.attach.RiderAttachConfigurationType
import com.jetbrains.rider.model.debuggerHelper.PlatformArchitecture
import com.jetbrains.rider.model.godot.frontendGodot.DebugSession
import com.jetbrains.rider.model.godot.frontendGodot.DebugSessionStatus
import com.jetbrains.rider.model.godot.frontendGodot.EnvironmentVariable
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.plugins.godot.rd.GodotRdClientService
import com.jetbrains.rider.protocol.protocol
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import org.jetbrains.annotations.ApiStatus
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference

@ApiStatus.Internal
interface GodotEditorDebugPort {
    val sessionId: String
    val gameEnvironment: Map<String, String>

    suspend fun awaitGameProcessId(): Int
    suspend fun resume()
}

@ApiStatus.Internal
fun interface GodotEditorGameWaiter {
    suspend fun await(port: GodotEditorDebugPort): Int?
}

@Suppress("IncorrectCancellationExceptionHandling")
private fun createGodotEditorGameWaiter(project: Project): GodotEditorGameWaiter = GodotEditorGameWaiter { port ->
    try {
        withBackgroundProgress(
            project,
            GodotPluginBundle.message("godot.debug.in.editor.waiting.for.game"),
            TaskCancellation.cancellable(),
        ) {
            port.awaitGameProcessId()
        }
    } catch (_: CancellationException) {
        currentCoroutineContext().ensureActive()
        GodotRdClientService.getInstance(project).stopPlayingScene()
        null
    }
}

/** Opens a port. The port releases its resources when [open]'s `lifetime` terminates. */
@ApiStatus.Internal
fun interface GodotEditorDebugPortFactory {
    suspend fun open(lifetime: Lifetime): GodotEditorDebugPort
}

@ApiStatus.Internal
fun interface GodotEditorDebugSessionStarter {
    suspend fun start(
        processInfo: ProcessInfo,
        targetPlatform: PlatformArchitecture,
        port: GodotEditorDebugPort,
        lifetime: LifetimeDefinition,
        onFinished: () -> Unit,
    )
}

@Service(Service.Level.PROJECT)
class GodotEditorDebugSessionService @ApiStatus.Internal constructor(
    private val project: Project,
    private val portFactory: GodotEditorDebugPortFactory,
    private val sessionStarter: GodotEditorDebugSessionStarter,
    private val gameWaiter: GodotEditorGameWaiter = createGodotEditorGameWaiter(project),
) {
    @Suppress("unused")
    constructor(project: Project) : this(
        project,
        GodotEditorDebugPortFactory { lifetime -> GodotGameDiagnosticPort.open(project, lifetime) },
        GodotEditorDebugSessionStarter { processInfo, platform, port, lifetime, onFinished ->
            startGodotDebugSession(project, processInfo, platform, port, lifetime, onFinished)
        },
    )

    private class ActiveSession(
        val port: GodotEditorDebugPort,
        val lifetime: LifetimeDefinition,
    ) {
        val finished = AtomicBoolean(false)
        val gameConnected = AtomicBoolean(false)
    }

    private val sessionReserved = AtomicBoolean(false)
    private val activeSession = AtomicReference<ActiveSession?>()

    suspend fun prepare(lifetime: Lifetime, callLifetime: Lifetime = lifetime): DebugSession {
        if (!sessionReserved.compareAndSet(false, true)) {
            return DebugSession(DebugSessionStatus.Busy, "", emptyList())
        }

        val sessionLifetime = lifetime.createNested()
        var session: ActiveSession? = null
        try {
            val openedPort = portFactory.open(sessionLifetime.lifetime)
            if (!callLifetime.isAlive) {
                return DebugSession(DebugSessionStatus.Failed, "", emptyList())
            }
            val newSession = ActiveSession(openedPort, sessionLifetime)
            session = newSession
            activeSession.set(newSession)
            sessionLifetime.lifetime.onTermination { finish(newSession) }
            sessionLifetime.lifetime.launch(Dispatchers.Default) { waitForGameAndStart(newSession) }

            val environment = openedPort.gameEnvironment.map { (name, value) ->
                EnvironmentVariable(name, value)
            }
            return DebugSession(DebugSessionStatus.Ready, openedPort.sessionId, environment)
        } catch (e: Exception) {
            rethrowControlFlowException(e)
            thisLogger().warn("[GODOT] failed to prepare a debug session", e)
            session?.let(::finish)
            return DebugSession(DebugSessionStatus.Failed, "", emptyList())
        } finally {
            if (session == null) {
                sessionLifetime.terminate()
                sessionReserved.set(false)
            }
        }
    }

    fun onLaunchFailed(sessionId: String) {
        val session = activeSession.get()?.takeIf { it.port.sessionId == sessionId } ?: return
        finish(session)
    }

    fun onSceneStopped(sessionId: String) {
        val session = activeSession.get()?.takeIf { it.port.sessionId == sessionId } ?: return
        if (!session.gameConnected.get()) {
            finish(session)
        }
    }

    @ApiStatus.Internal
    fun onGameConnected(sessionId: String) {
        activeSession.get()?.takeIf { it.port.sessionId == sessionId }?.gameConnected?.set(true)
    }

    private suspend fun waitForGameAndStart(session: ActiveSession) {
        try {
            val processId = gameWaiter.await(session.port)
            if (processId == null) {
                finish(session)
                return
            }
            onGameConnected(session.port.sessionId)
            if (session.finished.get()) return
            val processInfo = LocalAttachHost.INSTANCE.getProcessListAsync().firstOrNull { it.pid == processId }
                ?: throw ExecutionException(GodotPluginBundle.message("godot.debug.in.editor.game.process.not.found"))
            val targetPlatform = withContext(project.protocol.scheduler.asCoroutineDispatcher) {
                DebuggerHelperHost.getInstance(project).getProcessArchitecture(session.lifetime.lifetime, processId)
            }
            sessionStarter.start(processInfo, targetPlatform, session.port, session.lifetime) { finish(session) }
        } catch (e: Exception) {
            rethrowControlFlowException(e)
            if (!session.finished.get()) {
                thisLogger().warn("[GODOT] failed to attach to the game process", e)
                showErrorNotification(e)
                session.port.resume()
                GodotRdClientService.getInstance(project).stopPlayingScene()
                finish(session)
            }
        }
    }

    private fun showErrorNotification(error: Exception) {
        val content = error.message?.takeIf { it.isNotBlank() }
            ?: GodotPluginBundle.message("godot.debug.in.editor.launch.failed")
        NotificationGroupManager.getInstance()
            .getNotificationGroup(NOTIFICATION_GROUP_ID)
            .createNotification(GodotPluginBundle.message("godot.debug.in.editor.notification.title"), content, NotificationType.ERROR)
            .notify(project)
    }

    private fun finish(session: ActiveSession) {
        if (!session.finished.compareAndSet(false, true)) return
        session.lifetime.terminate()
        if (activeSession.compareAndSet(session, null)) {
            sessionReserved.set(false)
        }
    }

    companion object {
        private const val NOTIFICATION_GROUP_ID = "GodotDebugSessionNotificationGroupId"

        fun getInstance(project: Project): GodotEditorDebugSessionService = project.service()
    }
}

private suspend fun startGodotDebugSession(
    project: Project,
    processInfo: ProcessInfo,
    targetPlatform: PlatformArchitecture,
    port: GodotEditorDebugPort,
    sessionLifetime: LifetimeDefinition,
    onFinished: () -> Unit,
) {
    val cleanupLifetime = sessionLifetime.lifetime.createNested()
    try {
        withContext(Dispatchers.EDT) {
            val profile = GodotEditorAttachProfile(processInfo, targetPlatform, port::resume, cleanupLifetime::terminate)
            val settings = RunManager.getInstance(project).createConfiguration(profile.name, RiderAttachConfigurationType.FACTORY)
            val attachConfiguration = settings.configuration as? RiderAttachConfiguration
                ?: throw IllegalStateException("Created synthetic configuration is not ${RiderAttachConfiguration::class.java.simpleName}")
            attachConfiguration.attachProfile = profile

            val environment = ExecutionEnvironmentBuilder
                .create(DefaultDebugExecutor.getDebugExecutorInstance(), settings)
                .contentToReuse(null)
                .activeTarget()
                .build()
            environment.callback = createGodotInEditorDebugCallback(cleanupLifetime, environment.callback) {
                val cleanupFailures = listOfNotNull(
                    runCatching { port.resume() }.exceptionOrNull(),
                    runCatching { GodotRdClientService.getInstance(project).stopPlayingScene() }.exceptionOrNull(),
                    runCatching { onFinished() }.exceptionOrNull(),
                )
                cleanupFailures.forEach { rethrowControlFlowException(it) }
                cleanupFailures.firstOrNull()?.let { throw it }
            }
            ProgramRunnerUtil.executeConfigurationAsync(environment, true, true, environment.callback)
        }
    } catch (e: Exception) {
        rethrowControlFlowException(e)
        cleanupLifetime.terminate()
        throw e
    }
}
