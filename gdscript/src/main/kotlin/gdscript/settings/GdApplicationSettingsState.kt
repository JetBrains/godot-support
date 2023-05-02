package gdscript.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

// TODO REMOVE
@State(name = "GdApplicationSettingsState", storages = [Storage("gdApplicationSettings.xml")])
class GdApplicationSettingsState : PersistentStateComponent<GdApplicationState> {

    var gdApplicationState = GdApplicationState()

    companion object {
        fun getInstance(): GdApplicationSettingsState {
            return ApplicationManager.getApplication().getService(GdApplicationSettingsState::class.java)!!
        }
    }

    override fun getState(): GdApplicationState {
        return gdApplicationState
    }

    override fun loadState(state: GdApplicationState) {
        gdApplicationState = state
    }

}
