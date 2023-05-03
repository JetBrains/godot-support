package gdscript.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

@State(name = "GdProjectSettingsState", storages = [Storage("gdProjectSettings.xml")])
class GdProjectSettingsState : PersistentStateComponent<GdProjectState> {

    var gdProjectState = GdProjectState()

    companion object {
        fun getInstance(element: PsiElement): GdProjectSettingsState {
            return getInstance(element.project)
        }

        fun getInstance(project: Project): GdProjectSettingsState {
            return project.getService(GdProjectSettingsState::class.java)!!
        }
    }

    override fun getState(): GdProjectState {
        return gdProjectState
    }

    override fun loadState(state: GdProjectState) {
        gdProjectState = state
    }

}
