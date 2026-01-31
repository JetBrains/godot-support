package com.jetbrains.rider.plugins.godot

import com.intellij.execution.RunManager
import com.intellij.openapi.application.EDT
import com.intellij.openapi.client.ClientProjectSession
import com.intellij.openapi.components.Service
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.jetbrains.rd.protocol.SolutionExtListener
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.IOptProperty
import com.jetbrains.rd.util.reactive.IProperty
import com.jetbrains.rd.util.reactive.ISignal
import com.jetbrains.rd.util.reactive.OptProperty
import com.jetbrains.rd.util.reactive.Property
import com.jetbrains.rd.util.reactive.Signal
import com.jetbrains.rd.util.reactive.adviseNotNull
import com.jetbrains.rd.util.threading.coroutines.launch
import com.jetbrains.rider.model.godot.frontendBackend.GodotDescriptor
import com.jetbrains.rider.model.godot.frontendBackend.GodotFrontendBackendModel
import com.jetbrains.rider.model.godot.frontendBackend.LanguageServerConnectionMode
import com.jetbrains.rider.plugins.godot.run.GodotRunConfigurationGenerator
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfiguration
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfigurationType
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfiguration
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfigurationType
import com.jetbrains.rider.util.idea.getService
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists

@Service(Service.Level.PROJECT)
class GodotProjectDiscoverer(project: Project) {

    val godotDescriptor : IOptProperty<GodotDescriptor> = OptProperty()
    val lspConnectionMode: IProperty<LanguageServerConnectionMode?> = Property(null)
    val remoteHostPort: IProperty<Int?> = Property(null)
    val useDynamicPort: IProperty<Boolean?> = Property(null)
    val godot3Path : IProperty<String?> = Property(null)
    val godot4Path : IProperty<String?> = Property(null)
    val godotPath : IOptProperty<String> = OptProperty()

    val mainProjectBasePath: CompletableDeferred<Path> = CompletableDeferred()
    val isGodotProject : CompletableDeferred<Boolean> = CompletableDeferred()

    val projectMetadataModificationSignal: ISignal<Unit> = Signal()

    init {
        val lifetime = GodotProjectLifetimeService.getLifetime(project)
        godot3Path.adviseNotNull(lifetime){
            godotPath.set(it)
        }
        godot4Path.adviseNotNull(lifetime){
            godotPath.set(it)
        }

        godotDescriptor.adviseNotNull(lifetime){
            thisLogger().info("Godot godotDescriptor: $it")
            val basePath = Path(it.mainProjectBasePath)
            lifetime.launch(Dispatchers.IO) {
                val g3path = GodotMetadataFileWatcherUtil.getFromMonoMetadataPath(basePath)
                             ?: GodotMetadataFileWatcherUtil.getGodot3Path(basePath) ?: getGodotPathFromPlayerRunConfiguration(project)
                val g4path = GodotMetadataFileWatcherUtil.getGodot4Path(basePath) ?: getGodotPathFromCorePlayerRunConfiguration(project)
                withContext(Dispatchers.EDT) {
                    godot3Path.set(g3path)
                    godot4Path.set(g4path)
                }
            }
        }
    }

    private fun getGodotPathFromCorePlayerRunConfiguration(project: Project):String? {
        val runManager = RunManager.getInstance(project)
        val playerSettings = runManager.allSettings.firstOrNull { it.type is DotNetExeConfigurationType && it.name == GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME }
        if (playerSettings != null) {
            val config = playerSettings.configuration as DotNetExeConfiguration
            val path = config.parameters.exePath
            if (path.isNotEmpty() && Path(path).exists()) {
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
            if (path.isNotEmpty() && Path(path).exists()) {
                return path
            }
        }
        return null
    }

    val port: Int = 23685 // default value, //todo: read custom value from project.godot file

    companion object {
        fun getInstance(project: Project): GodotProjectDiscoverer = project.getService<GodotProjectDiscoverer>()
    }


    class ProtocolListener : SolutionExtListener<GodotFrontendBackendModel> {
        override fun extensionCreated(
            lifetime: Lifetime,
            session: ClientProjectSession,
            model: GodotFrontendBackendModel
        ) {
            model.isGodotProject.advise(lifetime) {getInstance(session.project).isGodotProject.complete(it) }
            model.godotDescriptor.advise(lifetime){
                getInstance(session.project).godotDescriptor.set(it)
                getInstance(session.project).mainProjectBasePath.complete(Path(it.mainProjectBasePath))
            }
            model.backendSettings.lspConnectionMode.adviseNotNull(lifetime){ getInstance(session.project).lspConnectionMode.set(it) }
            model.backendSettings.remoteHostPort.adviseNotNull(lifetime) { getInstance(session.project).remoteHostPort.set(it) }
            model.backendSettings.useDynamicPort.adviseNotNull(lifetime) { getInstance(session.project).useDynamicPort.set(it) }
        }
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
fun <T> Deferred<T>?.valueOrDefault(defaultValue: T): T {
    if (this == null) return defaultValue

    return if (this.isCompleted) {
        try {
            this.getCompleted()
        } catch (_: Exception) {
            defaultValue
        }
    } else {
        defaultValue
    }
}
