package gdscript.utils

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.project.Project
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import java.nio.file.Path
import java.nio.file.Paths

private const val RIDER_GODOT_PLUGIN_ID = "com.intellij.rider.godot"
fun PluginManagerCore.isRiderGodotSupportPluginInstalled(): Boolean {
    return this.plugins.any { it.pluginId.idString == RIDER_GODOT_PLUGIN_ID && it.isEnabled }
}

// todo: prop init may be delayed, we may need to somehow postpone index building
fun Project.getMainProjectBasePath(): Path? {
    return GodotCommunityUtil.getGodotProjectBasePath(this)
        ?: this.basePath?.let { Paths.get(it) }
}
