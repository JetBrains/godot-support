package gdscript.dap

import com.intellij.execution.configurations.ConfigurationTypeBase
import com.intellij.openapi.project.DumbAware
import gdscript.GdScriptBundle

class GdScriptConfigurationType : ConfigurationTypeBase(id,
    GdScriptBundle.message("gdscript.start.and.debug"),
    GdScriptBundle.message("start.godot.and.attach.debugger"),
    GdScriptPluginIcons.Icons.GodotLogo), DumbAware {

    val factory: GdScriptRunFactory = GdScriptRunFactory(this)

    init {
        addFactory(factory)
    }

    // todo: add link https://jb.gg/godot-dap to documentation page
    override fun getHelpTopic(): String = "reference.dialogs.rundebug.gdscript"

    companion object {
        const val id: String = "GDSCRIPT_DEBUG_RUN_CONFIGURATION"
    }
}
