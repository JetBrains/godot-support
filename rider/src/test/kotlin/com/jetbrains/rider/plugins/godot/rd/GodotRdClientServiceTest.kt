package com.jetbrains.rider.plugins.godot.rd

import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.lifetime.LifetimeDefinition
import com.jetbrains.rd.util.lifetime.SequentialLifetimes
import com.jetbrains.rider.test.shared.constants.TeamCityTags
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Timeout
import java.nio.file.Path
import java.util.Properties
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger

@Tag(TeamCityTags.Plugins.Godot.General)
@Timeout(30)
class GodotRdClientServiceTest {
    @Test
    fun `handshake connects only with a valid port and matching model hash`() {
        val connectedPorts = mutableListOf<Int>()

        fun connectWith(properties: Properties?) {
            GodotRdConnectionInfoHandler(
                connect = { _, port -> connectedPorts.add(port) },
                expectedModelHash = MODEL_HASH,
                loadProperties = { properties }
            ).connect(PORT_FILE, Lifetime.Eternal)
        }

        connectWith(null)
        connectWith(properties())
        connectWith(properties(port = "not-a-port"))
        connectWith(properties(port = "0"))
        connectWith(properties(port = "-1"))
        connectWith(properties(port = "65536"))
        connectWith(properties(port = VALID_PORT.toString()))
        connectWith(properties(port = VALID_PORT.toString(), modelHash = "not-a-hash"))
        connectWith(
            properties(port = VALID_PORT.toString(), modelHash = (MODEL_HASH - 1).toString())
        )

        assertTrue(connectedPorts.isEmpty())

        connectWith(
            properties(port = VALID_PORT.toString(), modelHash = MODEL_HASH.toString())
        )

        assertEquals(listOf(VALID_PORT), connectedPorts)
    }

    @Test
    fun `check that terminated lifetime doesn't call connect`() {
        val connectedPorts = CopyOnWriteArrayList<Int>()
        val firstReadStarted = CountDownLatch(1)
        val finishFirstRead = CountDownLatch(1)
        val readCount = AtomicInteger()
        val connector = GodotRdConnectionInfoHandler(
            connect = { _, port -> connectedPorts.add(port) },
            expectedModelHash = MODEL_HASH,
            loadProperties = {
                if (readCount.getAndIncrement() == 0) {
                    firstReadStarted.countDown()
                    assertTrue(finishFirstRead.await(10, TimeUnit.SECONDS))
                    properties(FIRST_PORT.toString(), MODEL_HASH.toString())
                } else {
                    properties(LATEST_PORT.toString(), MODEL_HASH.toString())
                }
            }
        )
        val lifetimeDefinition = LifetimeDefinition()
        val lifetimes = SequentialLifetimes(lifetimeDefinition.lifetime)
        val executor = Executors.newSingleThreadExecutor()

        try {
            val firstAttempt = executor.submit {
                connector.connect(PORT_FILE, lifetimes.next().lifetime)
            }
            assertTrue(firstReadStarted.await(10, TimeUnit.SECONDS))

            connector.connect(PORT_FILE, lifetimes.next().lifetime)
            finishFirstRead.countDown()
            firstAttempt.get(10, TimeUnit.SECONDS)

            assertEquals(listOf(LATEST_PORT), connectedPorts)
        } finally {
            finishFirstRead.countDown()
            lifetimeDefinition.terminate()
            executor.shutdownNow()
        }
    }

    private fun properties(port: String? = null, modelHash: String? = null) = Properties().apply {
        port?.let { setProperty("port", it) }
        modelHash?.let { setProperty("modelHash", it) }
    }

    companion object {
        private val PORT_FILE = Path.of("rider_ide_server.cfg")
        private const val VALID_PORT = 63342
        private const val FIRST_PORT = 50001
        private const val LATEST_PORT = 50002
        private const val MODEL_HASH = 123456789L
    }
}
