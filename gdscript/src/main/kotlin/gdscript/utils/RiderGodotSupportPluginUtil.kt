package gdscript.utils

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.project.Project
import com.jetbrains.rider.godot.community.GodotProjectProvider
import com.jetbrains.rider.godot.community.LspRunningStatusProvider
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.StateFlow
import java.nio.file.Path
import java.nio.file.Paths

// schedule to remove, on the gdscript plugin, we can directly as manager.getServersForProvider(GodotLspServerSupportProvider::class.java)
class RiderGodotSupportPluginUtil {
    companion object {
        private val LSP_STATUS_PROVIDER_EP: ExtensionPointName<LspRunningStatusProvider> =
            ExtensionPointName.create("com.intellij.rider.godot.community.lspStatusProvider")

        fun isGodotSupportLspRunning(project: Project): Boolean {
            val extList = LSP_STATUS_PROVIDER_EP.extensionList
            return extList.any { it.isLspRunning(project) }
        }
    }
}

private const val RIDER_GODOT_PLUGIN_ID = "com.intellij.rider.godot"
fun PluginManagerCore.isRiderGodotSupportPluginInstalled(): Boolean {
    return this.plugins.any { it.pluginId.idString == RIDER_GODOT_PLUGIN_ID && it.isEnabled }
}

// todo: prop init may be delayed, we may need to somehow postpone index building
fun Project.getMainProjectBasePath(): Path? {
    return GodotCommunityUtil.getGodotProjectBasePath(this)
        ?: this.basePath?.let { Paths.get(it) }
}
