package gdscript.lsp

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.lifetime.SequentialLifetimes
import com.jetbrains.rider.godot.community.GdScriptProjectLifetimeService


@Service(Service.Level.PROJECT)
class LspNotificationLifetimeService(project: Project) {
    private val pluginLifetime = GdScriptProjectLifetimeService.getLifetime(project)
    private val nestedLifetimeDef = pluginLifetime.createNested()
    private val sequentialLifetimes = SequentialLifetimes(nestedLifetimeDef)

    var currentLifetime: Lifetime = sequentialLifetimes.next().lifetime
        private set

    fun next(): Lifetime {
        currentLifetime = sequentialLifetimes.next().lifetime
        return currentLifetime
    }

    companion object {
        fun getInstance(project: Project): LspNotificationLifetimeService = project.service()
    }
}