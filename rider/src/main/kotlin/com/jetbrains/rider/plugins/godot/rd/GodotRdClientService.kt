package com.jetbrains.rider.plugins.godot.rd

import com.intellij.ide.impl.ProjectUtil
import com.intellij.openapi.application.EDT
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.components.serviceIfCreated
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.rd.framework.IdKind
import com.jetbrains.rd.framework.Identities
import com.jetbrains.rd.framework.Protocol
import com.jetbrains.rd.framework.Serializers
import com.jetbrains.rd.framework.SocketWire
import com.jetbrains.rd.protocol.IdeRootMarshallersProvider
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.lifetime.SequentialLifetimes
import com.jetbrains.rd.util.threading.SingleThreadScheduler
import com.jetbrains.rider.godot.community.GdProjectGodotService
import com.jetbrains.rider.godot.community.GdScriptProjectLifetimeService
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import com.jetbrains.rider.model.godot.frontendGodot.FrontendGodotModel
import com.jetbrains.rider.model.godot.frontendGodot.frontendGodotModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.nio.file.Path
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicReference


@Service(Service.Level.PROJECT)
class GodotRdClientService(val project: Project) {
    private data class Connection(val model: FrontendGodotModel, val protocol: Protocol)

    private val connection: AtomicReference<Connection?> = AtomicReference(null)
    private val clientLifetimes = SequentialLifetimes(GdScriptProjectLifetimeService.getLifetime(project))
    private val connectionInfoConnector = GodotRdConnectionInfoHandler(::connect)
    private val started = AtomicBoolean(false)

    val isConnected: Boolean
        get() = connection.get() != null

    suspend fun start() {
        check(started.compareAndSet(false, true)) { "[GODOT RD] the client is already started" }
        GodotCommunityUtil.getGodotProjectBasePathFlow(project).distinctUntilChanged().collectLatest { path ->
            updateClient(path)
        }
    }

    private suspend fun updateClient(basePath: Path?) {
        resetConnection()
        if (basePath == null) return
        val editorDir = basePath.resolve(GODOT_EDITOR_DIR)
        val portFile = editorDir.resolve(FrontendGodotModel.portFilename)
        val watcher = GodotPortFileWatcher(editorDir, FrontendGodotModel.portFilename)
        watcher.watch {
            // The VFS is not used here deliberately: events from vfs are handled on focus,
            // meaning you would have to click back to Rider to get it to connect.
            // The watcher is the only source of truth about the port file, it also signals the
            // initial state, so there is no need to look at the file before the first signal.
            resetConnection()
            val lifetime = clientLifetimes.next().lifetime
            withContext(Dispatchers.IO) {
                connectionInfoConnector.connect(portFile, lifetime)
            }
        }
    }

    private fun resetConnection() {
        connection.set(null)
        GdProjectGodotService.getInstance(project).updateCurrentScene(null)
        clientLifetimes.terminateCurrent()
    }

    private fun connect(lt: Lifetime, port: Int) {
        thisLogger().trace("[GODOT RD] going to connect to port: $port")
        val scheduler = SingleThreadScheduler(lt, CLIENT_NAME)
        val wire = SocketWire.Client(lt, scheduler, port = port, optId = CLIENT_NAME)
        val protocol = Protocol(
            CLIENT_NAME, Serializers(IdeRootMarshallersProvider),
            // rd-cpp does not have SequentialIdentities at this point, so the deprecated
            // call is necessary
            @Suppress("DEPRECATION")
            Identities(IdKind.Client), scheduler, wire, lt
        )
        val model = protocol.frontendGodotModel
        protocol.scheduler.queue {
            model.openInRider.advise(lt) {
                openInRider(it)
            }
            model.currentSceneChange.advise(lt) {
                thisLogger().trace("[GODOT RD] updating current scene to: $it")
                GdProjectGodotService.getInstance(project).updateCurrentScene(it)
            }
        }
        val newConnection = Connection(model, protocol)
        lt.onTermination {
            if (connection.compareAndSet(newConnection, null)) {
                GdProjectGodotService.getInstance(project).updateCurrentScene(null)
            }
        }
        wire.connected.advise(lt) { connected ->
            if (connected) {
                lt.executeIfAlive {
                    connection.set(newConnection)
                }
            } else {
                GdProjectGodotService.getInstance(project).updateCurrentScene(null)
                connection.compareAndSet(newConnection, null)
            }
            thisLogger().trace("[GODOT RD] wire connection status: $connected")
        }
    }

    fun openInGodot(file: VirtualFile) {
        val currentConnection = connection.get()
        if (currentConnection == null) {
            return
        }
        currentConnection.protocol.scheduler.queue {
            if (file.isInLocalFileSystem) {
                currentConnection.model.openInGodot.fire(file.toNioPath().toString())
            } else {
                thisLogger().warn("[GODOT RD] tried to open in Godot a non local file")
            }
        }
    }

    private fun focusRider() {
        ProjectUtil.focusProjectWindow(project, stealFocusIfAppInactive = true)
    }

    private fun openInRider(path: String) {
        if (!isConnected) {
            thisLogger().warn("[GODOT RD] open in rider called without being connected")
            return
        }
        GdScriptProjectLifetimeService.getScope(project).launch {
            val file = LocalFileSystem.getInstance().refreshAndFindFileByNioFile(Path.of(path)) ?: return@launch
            withContext(Dispatchers.EDT) {
                OpenFileDescriptor(project, file).navigate(true)
                focusRider()
            }
        }
    }

    companion object {

        // TODO: this could potentially brick if application/config/use_hidden_project_data_directory
        // is turned off. Since this is not the default it is not that big of an issue.
        // If we would be fixing this also fix GodotMetadataService which uses the same dir.
        private const val GODOT_EDITOR_DIR = ".godot/editor"
        private const val CLIENT_NAME: String = "GodotRdClient"

        fun getInstance(project: Project): GodotRdClientService = project.service<GodotRdClientService>()

        fun getInstanceIfCreated(project: Project): GodotRdClientService? = project.serviceIfCreated<GodotRdClientService>()
    }
}
