package gdscript.listener

import common.util.GdScriptProjectLifetimeService
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.jetbrains.rd.util.reactive.adviseNotNull
import gdscript.library.GdLibraryUpdater
import gdscript.utils.RiderGodotSupportPluginUtil
import java.nio.file.Path

class ReferenceSdkProjectActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        if (project.isDisposed) return
        val prop = RiderGodotSupportPluginUtil.getMainProjectBasePathProperty(project)?: return
        prop.adviseNotNull(GdScriptProjectLifetimeService.getLifetime(project)){
            GdLibraryUpdater.getInstance(project).scheduleSkdCheck(Path.of(it))
        }

    }
}
