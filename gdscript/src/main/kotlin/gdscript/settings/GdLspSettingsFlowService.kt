package gdscript.settings

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// TODO: Although the settings are moved between the plugins, no migration is done -> Rider users lose their settings
@Service(Service.Level.PROJECT)
class GdLspSettingsFlowService(private val project: Project) {

    private val state get() = GdProjectSettingsState.getInstance(project).state

    private val _lspConnectionMode = MutableStateFlow(readLspConnectionMode())
    private val _remoteHostPort = MutableStateFlow<Int?>(state.lspRemoteHostPort)
    private val _useDynamicPort = MutableStateFlow<Boolean?>(state.lspUseDynamicPort)

    val lspConnectionMode: StateFlow<GdLspConnectionMode?> = _lspConnectionMode.asStateFlow()
    val remoteHostPort: StateFlow<Int?> = _remoteHostPort.asStateFlow()
    val useDynamicPort: StateFlow<Boolean?> = _useDynamicPort.asStateFlow()

    /**
     * Call this after settings are applied (e.g. from [GdSettingsConfigurable.apply])
     * to push updated values into the flows.
     */
    fun settingsChanged() {
        _lspConnectionMode.value = readLspConnectionMode()
        _remoteHostPort.value = state.lspRemoteHostPort
        _useDynamicPort.value = state.lspUseDynamicPort
    }

    fun setLspConnectionMode(mode: GdLspConnectionMode) {
        GdProjectSettingsState.getInstance(project).state.lspConnectionMode = mode.name
        _lspConnectionMode.value = mode
    }

    private fun readLspConnectionMode(): GdLspConnectionMode? {
        return try {
            GdLspConnectionMode.valueOf(state.lspConnectionMode)
        } catch (_: IllegalArgumentException) {
            null
        }
    }

    companion object {
        fun getInstance(project: Project): GdLspSettingsFlowService = project.service()
    }
}