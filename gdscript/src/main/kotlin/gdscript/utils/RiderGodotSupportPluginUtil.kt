package gdscript.utils

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.project.Project
import com.jetbrains.rider.godot.community.GodotProjectProvider
import com.jetbrains.rider.godot.community.LspRunningStatusProvider
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.StateFlow
import java.nio.file.Path
import java.nio.file.Paths

class RiderGodotSupportPluginUtil {
    companion object {
        private val LSP_STATUS_PROVIDER_EP: ExtensionPointName<LspRunningStatusProvider> =
            ExtensionPointName.create("com.intellij.rider.godot.community.lspStatusProvider")
        private val GODOT_PROJECT_PROVIDER_EP: ExtensionPointName<GodotProjectProvider> =
            ExtensionPointName.create("com.intellij.rider.godot.community.godotProjectProvider")

        fun isGodotSupportLspRunning(project: Project): Boolean {
            val extList = LSP_STATUS_PROVIDER_EP.extensionList
            return extList.any { it.isLspRunning(project) }
        }

        fun isGodotProject(project: Project): Deferred<Boolean>? {
            val extList = GODOT_PROJECT_PROVIDER_EP.extensionList
            return extList.firstOrNull()?.isGodotProject(project)
        }

        fun getMainProjectBasePath(project: Project): Path? {
            val extList = GODOT_PROJECT_PROVIDER_EP.extensionList
            return extList.firstOrNull()?.getGodotProjectBasePath(project)
        }

        fun getGodotProjectBasePathFlow(project: Project): StateFlow<Path?>? {
            val extList = GODOT_PROJECT_PROVIDER_EP.extensionList
            return extList.firstOrNull()?.getGodotProjectBasePathFlow(project)
        }
    }
}

private const val RIDER_GODOT_PLUGIN_ID = "com.intellij.rider.godot"
fun PluginManagerCore.isRiderGodotSupportPluginInstalled(): Boolean {
    return this.plugins.any { it.pluginId.idString == RIDER_GODOT_PLUGIN_ID && it.isEnabled }
}

// todo: prop init may be delayed, we may need to somehow postpone index building
fun Project.getMainProjectBasePath(): Path? {
    return RiderGodotSupportPluginUtil.getMainProjectBasePath(this)
        ?: this.basePath?.let { Paths.get(it) }
}
