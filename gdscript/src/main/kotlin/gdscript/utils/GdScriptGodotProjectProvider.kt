package gdscript.utils

import GdProjectService
import com.intellij.openapi.project.Project
import com.jetbrains.rider.godot.community.GodotProjectProvider
import kotlinx.coroutines.flow.StateFlow
import java.nio.file.Path

class GdScriptGodotProjectProvider : GodotProjectProvider {
    override fun getGodotExecutablePathFlow(project: Project): StateFlow<Path?> =
        GdProjectService.getInstance(project).executablePathFlow

    override fun getGodotProjectBasePathFlow(project: Project): StateFlow<Path?> =
        GdProjectService.getInstance(project).projectBasePathFlow

    override fun isPureGdScriptProjectFlow(project: Project): StateFlow<Boolean?> =
        GdProjectService.getInstance(project).isPureGdScriptProjectFlow
}
