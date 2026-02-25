package gdscript.dap

import com.intellij.execution.impl.CheckableRunConfigurationEditor
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.lifetime.LifetimeDefinition
import common.util.GdScriptProjectLifetimeService
import javax.swing.JPanel

class GdScriptRunConfigurationSettingsEditor(project: Project) : SettingsEditor<GdScriptRunConfiguration>(),
                                                                 CheckableRunConfigurationEditor<GdScriptRunConfiguration> {

    private val lifetimeDefinition: LifetimeDefinition = GdScriptProjectLifetimeService.getLifetime(project).createNested()
    private val form: GdScriptEditorForm = GdScriptEditorForm(lifetimeDefinition, project)

    override fun checkEditorData(configuration: GdScriptRunConfiguration) {
        form.getData(configuration)
    }

    override fun resetEditorFrom(configuration: GdScriptRunConfiguration) {
        form.setData(configuration.json)
    }

    override fun applyEditorTo(configuration: GdScriptRunConfiguration) {
        checkEditorData(configuration)
    }

    override fun createEditor(): JPanel {
        return form.panel
    }

    override fun disposeEditor() {
        lifetimeDefinition.terminate()
    }
}
