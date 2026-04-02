package gdscript.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.platform.ide.progress.withBackgroundProgress
import common.util.GdScriptProjectLifetimeService
import gdscript.GdScriptBundle
import gdscript.library.GdLibraryManager
import gdscript.library.GdProjectGodotService
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ReferenceSdkProjectActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        if (project.isDisposed) return
        GdScriptProjectLifetimeService.getInstance(project).scope.launch {
            GdProjectGodotService.getInstance(project).projectInfoFlow.filterNotNull().collect { info ->
                GdScriptProjectLifetimeService.getInstance(project).scope.launch {
                    withBackgroundProgress(project, GdScriptBundle.message("progress.title.check.gdsdk.for.project")) {
                        if (project.isDisposed) return@withBackgroundProgress

                        val sdkPath = GdLibraryManager.extractSdkIfNeeded(info.version)
                        GdLibraryManager.registerSdkIfNeeded(sdkPath, project)
                    }
                }
            }
        }
    }
}
