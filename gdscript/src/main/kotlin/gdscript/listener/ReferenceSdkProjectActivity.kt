package gdscript.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import gdscript.library.GdLibraryUpdater
import gdscript.utils.RiderGodotSupportPluginUtil

class ReferenceSdkProjectActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        if (project.isDisposed) return
        val prop = RiderGodotSupportPluginUtil.getMainProjectBasePathProperty(project) ?: return
        val path = prop.await()
        GdLibraryUpdater.getInstance(project).scheduleSkdCheck(path)
    }
}
