package gdscript.utils

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.extensions.ExtensionPointName
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.reactive.IOptProperty
import com.jetbrains.rider.godot.community.LspRunningStatusProvider
import com.jetbrains.rider.godot.community.ProjectInfoProvider
import kotlinx.coroutines.Deferred

class RiderGodotSupportPluginUtil {
    companion object {
        private val EP_NAME: ExtensionPointName<LspRunningStatusProvider> = ExtensionPointName.create("com.intellij.rider.godot.community.lspStatusProvider")
        private val EP_NAME2: ExtensionPointName<ProjectInfoProvider> = ExtensionPointName.create("com.intellij.rider.godot.community.projectInfoProvider")

        fun isGodotSupportLSPRunning(project: Project): Boolean {
            return EP_NAME.extensionList.any { it.isLspRunning(project) } // list of booleans
        }

        fun isGodotProject(project: Project): Deferred<Boolean>? {
            return EP_NAME2.extensionList.firstOrNull()?.isGodotProject(project)
        }

        fun getMainProjectBasePathProperty(project: Project): IOptProperty<String>? =
            EP_NAME2.extensionList.firstOrNull()?.getMainProjectBasePathProperty(project)
    }
}

private const val RIDER_GODOT_PLUGIN_ID = "com.intellij.rider.godot"
fun PluginManagerCore.isRiderGodotSupportPluginInstalled(): Boolean {
    return this.plugins.any { it.pluginId.idString == RIDER_GODOT_PLUGIN_ID && it.isEnabled }
}

fun Deferred<Boolean>?.hasCompletedTrue(): Boolean {
    if (this == null) return false

    return if (this.isCompleted) {
        this.getCompleted()
    } else {
        false
    }
}
