package gdscript.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

@State(name = "gdscript.settings.GdSettingsState", storages = [Storage("gdscript.GdScriptSettingsPlugin.xml")])
object GdSettingsState : PersistentStateComponent<GdSettingsState> {

    var hidePrivate = true;
    var sdkPath: String? = null;

    override fun getState(): GdSettingsState {
        return this;
    }

    override fun loadState(state: GdSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

}
