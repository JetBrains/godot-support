package gdscript.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import gdscript.library.GdLibraryUpdater
import gdscript.utils.RiderGodotSupportPluginUtil
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import java.nio.file.Paths
import kotlin.time.Duration.Companion.seconds

class ReferenceSdkProjectActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        if (project.isDisposed) return

        val isGodot = withTimeoutOrNull(30.seconds) {
            RiderGodotSupportPluginUtil.isGodotProject(project)?.await()
        } ?: return
        if (!isGodot) return

        val basePath = withTimeoutOrNull(30.seconds) {
            RiderGodotSupportPluginUtil.getGodotProjectBasePathFlow(project)
                ?.filterNotNull()
                ?.first()
        } ?: project.basePath?.let { Paths.get(it) } ?: return

        GdLibraryUpdater.getInstance(project).scheduleSkdCheck(basePath)
    }
}
