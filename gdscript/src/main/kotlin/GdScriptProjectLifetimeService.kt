package common.util

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.lifetime.LifetimeDefinition
import kotlinx.coroutines.CoroutineScope

abstract class LifetimedService : Disposable {
    private val lifetimeDefinition = LifetimeDefinition()
    protected val serviceLifetime: Lifetime get() = lifetimeDefinition.lifetime

    override fun dispose() {
        lifetimeDefinition.terminate()
    }
}

@Service(Service.Level.PROJECT)
class GdScriptProjectLifetimeService(val scope: CoroutineScope): LifetimedService() {
    companion object {
        fun getInstance(project: Project): GdScriptProjectLifetimeService = project.service()
        fun getLifetime(project: Project): Lifetime = getInstance(project).serviceLifetime
        fun getScope(project: Project): CoroutineScope = project.service<GdScriptProjectLifetimeService>().scope
    }
}