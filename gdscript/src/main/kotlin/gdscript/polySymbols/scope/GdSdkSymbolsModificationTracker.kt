package gdscript.polySymbols.scope

import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SimpleModificationTracker

@Service(Service.Level.PROJECT)
class GdSdkSymbolsModificationTracker : SimpleModificationTracker() {
    companion object {
        fun getInstance(project: Project): GdSdkSymbolsModificationTracker =
            project.getService(GdSdkSymbolsModificationTracker::class.java)
    }
}