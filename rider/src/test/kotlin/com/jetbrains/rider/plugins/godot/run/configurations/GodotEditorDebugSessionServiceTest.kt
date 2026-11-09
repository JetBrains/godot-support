package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.openapi.project.Project
import com.intellij.testFramework.common.timeoutRunBlocking
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.lifetime.LifetimeDefinition
import com.jetbrains.rider.model.godot.frontendGodot.DebugSessionStatus
import com.jetbrains.rider.test.shared.constants.TeamCityTags
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.awaitCancellation
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import java.lang.reflect.Proxy
import java.util.concurrent.atomic.AtomicInteger

@Tag(TeamCityTags.Plugins.Godot.General)
class GodotEditorDebugSessionServiceTest {
    private val project = Proxy.newProxyInstance(
        Project::class.java.classLoader,
        arrayOf(Project::class.java),
    ) { proxy, method, arguments ->
        when (method.name) {
            "equals" -> proxy === arguments?.firstOrNull()
            "hashCode" -> System.identityHashCode(proxy)
            "toString" -> "test project"
            else -> null
        }
    } as Project

    @Test
    fun `second prepare is busy and launch failure releases the pending port`() = timeoutRunBlocking {
        val port = FakePort()
        val attachCount = AtomicInteger()
        val service = GodotEditorDebugSessionService(
            project,
            GodotEditorDebugPortFactory { lifetime -> port.openIn(lifetime) },
            GodotEditorDebugSessionStarter { _, _, _, _, _ -> attachCount.incrementAndGet() },
            GodotEditorGameWaiter { it.awaitGameProcessId() },
        )
        val lifetime = LifetimeDefinition()

        try {
            val ready = service.prepare(lifetime.lifetime)
            val busy = service.prepare(lifetime.lifetime)

            assertEquals(DebugSessionStatus.Ready, ready.status)
            assertEquals("session-1", ready.sessionId)
            assertEquals(DebugSessionStatus.Busy, busy.status)

            service.onLaunchFailed(ready.sessionId)

            assertEquals(1, port.releaseCount.get())
            assertEquals(0, attachCount.get())
        }
        finally {
            lifetime.terminate()
        }
    }

    @Test
    fun `terminated call lifetime releases the session`() = timeoutRunBlocking {
        val firstPort = FakePort("session-1")
        val secondPort = FakePort("session-2")
        val ports = mutableListOf(firstPort, secondPort)
        val service = GodotEditorDebugSessionService(
            project,
            GodotEditorDebugPortFactory { lifetime -> ports.removeAt(0).openIn(lifetime) },
            GodotEditorDebugSessionStarter { _, _, _, _, _ -> },
            GodotEditorGameWaiter { it.awaitGameProcessId() },
        )
        val lifetime = LifetimeDefinition()
        val callLifetime = LifetimeDefinition().apply { terminate() }

        try {
            val failed = service.prepare(lifetime.lifetime, callLifetime.lifetime)
            val ready = service.prepare(lifetime.lifetime)

            assertEquals(DebugSessionStatus.Failed, failed.status)
            assertEquals(1, firstPort.releaseCount.get())
            assertEquals(DebugSessionStatus.Ready, ready.status)
        }
        finally {
            lifetime.terminate()
        }
    }

    @Test
    fun `cancelling the game wait releases the session`() = timeoutRunBlocking {
        val firstPort = FakePort("session-1")
        val secondPort = FakePort("session-2")
        val ports = mutableListOf(firstPort, secondPort)
        val service = GodotEditorDebugSessionService(
            project,
            GodotEditorDebugPortFactory { lifetime -> ports.removeAt(0).openIn(lifetime) },
            GodotEditorDebugSessionStarter { _, _, _, _, _ -> },
            GodotEditorGameWaiter { null },
        )
        val lifetime = LifetimeDefinition()

        try {
            val ready = service.prepare(lifetime.lifetime)
            firstPort.released.await()

            assertEquals(DebugSessionStatus.Ready, ready.status)
            assertEquals(1, firstPort.releaseCount.get())
            assertEquals(DebugSessionStatus.Ready, service.prepare(lifetime.lifetime).status)
        }
        finally {
            lifetime.terminate()
        }
    }

    @Test
    fun `scene stop before connection releases the session`() = timeoutRunBlocking {
        val firstPort = FakePort("session-1")
        val ports = mutableListOf(firstPort, FakePort("session-2"))
        val service = GodotEditorDebugSessionService(
            project,
            GodotEditorDebugPortFactory { lifetime -> ports.removeAt(0).openIn(lifetime) },
            GodotEditorDebugSessionStarter { _, _, _, _, _ -> },
            GodotEditorGameWaiter { it.awaitGameProcessId() },
        )
        val lifetime = LifetimeDefinition()

        try {
            val ready = service.prepare(lifetime.lifetime)
            service.onSceneStopped(ready.sessionId)

            assertEquals(1, firstPort.releaseCount.get())
            assertEquals(DebugSessionStatus.Ready, service.prepare(lifetime.lifetime).status)
        }
        finally {
            lifetime.terminate()
        }
    }

    @Test
    fun `scene stop after connection leaves the session active`() = timeoutRunBlocking {
        val port = FakePort()
        val service = GodotEditorDebugSessionService(
            project,
            GodotEditorDebugPortFactory { lifetime -> port.openIn(lifetime) },
            GodotEditorDebugSessionStarter { _, _, _, _, _ -> },
            GodotEditorGameWaiter { it.awaitGameProcessId() },
        )
        val lifetime = LifetimeDefinition()

        try {
            val ready = service.prepare(lifetime.lifetime)
            service.onGameConnected(ready.sessionId)
            service.onSceneStopped(ready.sessionId)

            assertEquals(0, port.releaseCount.get())
            assertEquals(DebugSessionStatus.Busy, service.prepare(lifetime.lifetime).status)
        }
        finally {
            lifetime.terminate()
        }
    }

    private class FakePort(override val sessionId: String = "session-1") : GodotEditorDebugPort {
        override val gameEnvironment: Map<String, String> = mapOf("DOTNET_MODIFIABLE_ASSEMBLIES" to "debug")
        val releaseCount = AtomicInteger()
        val released = CompletableDeferred<Unit>()

        fun openIn(lifetime: Lifetime): FakePort {
            lifetime.onTermination {
                releaseCount.incrementAndGet()
                released.complete(Unit)
            }
            return this
        }

        override suspend fun awaitGameProcessId(): Int = awaitCancellation()

        override suspend fun resume() = Unit
    }
}