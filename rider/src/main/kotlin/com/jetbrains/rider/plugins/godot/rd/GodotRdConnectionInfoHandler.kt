package com.jetbrains.rider.plugins.godot.rd

import com.intellij.openapi.diagnostic.thisLogger
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rider.model.godot.frontendGodot.FrontendGodotModel
import org.jetbrains.annotations.ApiStatus
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.util.Properties

enum class ConnectionResult {
    NOT_CONNECTED_PORT,
    NOT_CONNECTED_MODEL,
    CONNECTED,
}

@ApiStatus.Internal
class GodotRdConnectionInfoHandler(
    private val connect: (Lifetime, Int) -> Unit,
    private val expectedModelHash: Long = FrontendGodotModel.serializationHash,
    loadProperties: ((Path) -> Properties?)? = null
) {
    private val propertiesLoader = loadProperties ?: ::loadPropertiesFromFile

    fun connect(portFile: Path, lifetime: Lifetime): ConnectionResult {
        val properties = propertiesLoader(portFile) ?: return ConnectionResult.NOT_CONNECTED_PORT
        val port = properties.getProperty(FrontendGodotModel.portKey)?.toIntOrNull()
        if (port == null || port !in 1..65535) {
            thisLogger().warn("[GODOT RD] Found invalid port $port in $portFile")
            return ConnectionResult.NOT_CONNECTED_PORT
        }
        val addonModelHash = properties.getProperty(FrontendGodotModel.modelHashKey)?.toLongOrNull()
        // Connecting to a server with the wrong hash causes Godot to crash on an RD assert.
        if (addonModelHash != expectedModelHash) {
            // TODO: maybe should be a popup, or add it as a diagnostic somewhere?
            // since this can reasonably happen if the user has multiple rider installs.
            thisLogger().warn(
                "[GODOT RD] not connecting to Godot: the model hash of the addon ($addonModelHash) does not match " +
                    "the model hash of Rider ($expectedModelHash), " +
                    "the Rider addon in Godot is not compatible with this version of Rider"
            )
            return ConnectionResult.NOT_CONNECTED_MODEL
        }
        var connected = ConnectionResult.NOT_CONNECTED_PORT
        lifetime.executeIfAlive {
            connect(lifetime, port)
            connected = ConnectionResult.CONNECTED
        }
        return connected
    }

    private fun loadPropertiesFromFile(portFile: Path): Properties? {
        val properties = Properties()
        try {
            Files.newBufferedReader(portFile).use(properties::load)
        } catch (e: IOException) {
            thisLogger().info("[GODOT RD] failed to read the connection info: $e")
            return null
        }
        return properties
    }
}
