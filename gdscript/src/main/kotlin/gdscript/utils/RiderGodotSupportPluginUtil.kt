package gdscript.utils

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.project.Project
import com.jetbrains.rider.godot.community.LspRunningStatusProvider

class RiderGodotSupportPluginUtil {
    companion object {
        private val EP_NAME: ExtensionPointName<LspRunningStatusProvider> = ExtensionPointName.create("com.intellij.rider.godot.community.lspStatusProvider")

        fun isGodotSupportLSPRunning(project: Project): Boolean {
            return EP_NAME.extensionList.any { it.isLspRunning(project) } // list of booleans
        }
    }
}

private const val RIDER_GODOT_PLUGIN_ID = "com.intellij.rider.godot"
fun PluginManagerCore.isRiderGodotSupportPluginInstalled(): Boolean {
    return this.plugins.any { it.pluginId.idString == RIDER_GODOT_PLUGIN_ID && it.isEnabled }
}