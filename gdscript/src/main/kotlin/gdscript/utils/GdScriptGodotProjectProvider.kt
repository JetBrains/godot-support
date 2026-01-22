package gdscript.utils

import GdProjectService
import com.intellij.openapi.project.Project
import com.jetbrains.rider.godot.community.GodotProjectProvider
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.flow.StateFlow
import java.nio.file.Path

class GdScriptGodotProjectProvider : GodotProjectProvider {
    override fun getGodotExecutablePath(project: Project): Path? =
        GdProjectService.getInstance(project).getExecutablePath()

    override fun getGodotProjectBasePath(project: Project): Path? =
        GdProjectService.getInstance(project).projectRoot?.toNioPath()

    override fun getGodotExecutablePathFlow(project: Project): StateFlow<Path?> =
        GdProjectService.getInstance(project).executablePathFlow

    override fun getGodotProjectBasePathFlow(project: Project): StateFlow<Path?> =
        GdProjectService.getInstance(project).projectBasePathFlow

    override fun isGodotProject(project: Project): Deferred<Boolean> =
        GdProjectService.getInstance(project).isGodotProjectDeferred
}
