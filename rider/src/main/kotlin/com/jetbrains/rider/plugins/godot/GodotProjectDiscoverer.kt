package com.jetbrains.rider.plugins.godot

import com.intellij.execution.RunManager
import com.intellij.openapi.client.ClientProjectSession
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.jetbrains.rd.platform.util.idea.LifetimedService
import com.intellij.openapi.rd.util.lifetime
import com.jetbrains.rd.protocol.SolutionExtListener
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.IProperty
import com.jetbrains.rd.util.reactive.Property
import com.jetbrains.rd.util.reactive.adviseNotNull
import com.jetbrains.rider.model.godot.frontendBackend.GodotFrontendBackendModel
import com.jetbrains.rider.plugins.godot.run.GodotRunConfigurationGenerator
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfiguration
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfigurationType
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfiguration
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationType
import com.jetbrains.rider.util.idea.getService
import java.io.File

@Service(Service.Level.PROJECT)
class GodotProjectDiscoverer(project: Project) : LifetimedService() {

    val mainProjectBasePath : IProperty<String?> = Property(null)
    private val logger = Logger.getInstance(GodotProjectDiscoverer::class.java)
    val godotMonoPath : IProperty<String?> = Property(null)
    val godotCorePath : IProperty<String?> = Property(null)

    init {
        mainProjectBasePath.adviseNotNull(project.lifetime){
            logger.info("Godot mainProjectBasePath: $it")
            godotMonoPath.set(MetadataMonoFileWatcher.getFromMonoMetadataPath(it) ?: MetadataMonoFileWatcher.getGodotPath(it) ?: getGodotPathFromPlayerRunConfiguration(project))
            godotCorePath.set(MetadataCoreFileWatcher.getGodotPath(it) ?: getGodotPathFromCorePlayerRunConfiguration(project))
        }
    }

    private fun getGodotPathFromCorePlayerRunConfiguration(project: Project):String? {
        val runManager = RunManager.getInstance(project)
        val playerSettings = runManager.allSettings.firstOrNull { it.type is DotNetExeConfigurationType && it.name == GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME }
        if (playerSettings != null) {
            val config = playerSettings.configuration as DotNetExeConfiguration
            val path = config.parameters.exePath
            if (path.isNotEmpty() && File(path).exists()) {
                return path
            }
        }
        return null
    }

    private fun getGodotPathFromPlayerRunConfiguration(project: Project):String? {
        val runManager = RunManager.getInstance(project)
        val playerSettings = runManager.allSettings.firstOrNull { it.type is GodotDebugRunConfigurationType && it.name == GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME }
        if (playerSettings != null) {
            val config = playerSettings.configuration as GodotDebugRunConfiguration
            val path = config.parameters.exePath
            if (path.isNotEmpty() && File(path).exists()) {
                return path
            }
        }
        return null
    }

    val port: Int = 23685 // default value, //todo: read custom value from project.godot file

    companion object {
        fun getInstance(project: Project) = project.getService<GodotProjectDiscoverer>()
    }


    class ProtocolListener : SolutionExtListener<GodotFrontendBackendModel> {
        override fun extensionCreated(
            lifetime: Lifetime,
            session: ClientProjectSession,
            model: GodotFrontendBackendModel
        ) {
            model.mainProjectBasePath.adviseNotNull(lifetime) {
                getInstance(session.project).mainProjectBasePath.set(it)
            }
        }
    }
}