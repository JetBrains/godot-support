package gdscript.utils

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import org.jetbrains.annotations.TestOnly
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

/**
 * In Rider, when sln is opened - contentRoots are empty by design.
 * But in tests we need tbe workaround.
 * */
fun Project.getMainProjectRoot(): VirtualFile? {
    @Suppress("TestOnlyProblems")
    if (ApplicationManager.getApplication().isUnitTestMode) return testProjectRoot()
    return getMainProjectBasePath()?.let { VirtualFileManager.getInstance().findFileByNioPath(it) }
}

/** Light test fixtures keep their files in temp:// and have no NIO path, so use the one fixture content root. */
@TestOnly
private fun Project.testProjectRoot(): VirtualFile? =
    ProjectRootManager.getInstance(this).contentRoots.firstOrNull()
