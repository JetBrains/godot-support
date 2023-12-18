package com.jetbrains.rider.plugins.godot

import com.intellij.execution.RunManager
import com.intellij.openapi.client.ClientProjectSession
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.rd.util.lifetime
import com.jetbrains.rd.platform.util.idea.LifetimedService
import com.jetbrains.rd.protocol.SolutionExtListener
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.IProperty
import com.jetbrains.rd.util.reactive.Property
import com.jetbrains.rd.util.reactive.adviseNotNull
import com.jetbrains.rider.model.godot.frontendBackend.GodotDescriptor
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

    val godotDescriptor : IProperty<GodotDescriptor?> = Property(null)
    private val logger = Logger.getInstance(GodotProjectDiscoverer::class.java)
    val godot3Path : IProperty<String?> = Property(null)
    val godot4Path : IProperty<String?> = Property(null)

    init {
        godotDescriptor.adviseNotNull(project.lifetime){
            logger.info("Godot godotDescriptor: $it")
            val basePath = File(it.mainProjectBasePath)
            godot3Path.set(
                MetadataMonoFileWatcher.Util.getFromMonoMetadataPath(basePath)
                    ?: MetadataMonoFileWatcher.Util.getGodotPath(basePath) ?: getGodotPathFromPlayerRunConfiguration(project))
            godot4Path.set(MetadataCoreFileWatcher.Util.getGodotPath(basePath) ?: getGodotPathFromCorePlayerRunConfiguration(project))
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
            model.godotDescriptor.adviseNotNull(lifetime){
                getInstance(session.project).godotDescriptor.set(it)
            }
        }
    }
}