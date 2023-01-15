package gdscript.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@State(name = "GdSettingsState", storages = [Storage("gdSettings.xml")])
class GdSettingsState : PersistentStateComponent<GdState> {

    var gdState = GdState();

    companion object {
        fun getInstance(): GdSettingsState {
            return ApplicationManager.getApplication().getService(GdSettingsState::class.java)!!
        }
    }

    override fun getState(): GdState {
        return gdState;
    }

    override fun loadState(state: GdState) {
        gdState = state;
    }

}
