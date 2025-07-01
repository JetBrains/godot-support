package gdscript.listener

import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import gdscript.library.GdLibraryUpdater
import gdscript.utils.RiderGodotSupportPluginUtil
import gdscript.utils.getMainProjectBasePath
import gdscript.utils.isRiderGodotSupportPluginInstalled

class ReferenceSdkProjectActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        if (project.isDisposed) return

        if (!PluginManagerCore.isRiderGodotSupportPluginInstalled()){
            val basePath = project.getMainProjectBasePath() ?: return
            GdLibraryUpdater.getInstance(project).scheduleSkdCheck(basePath)
        }

        val prop = RiderGodotSupportPluginUtil.getMainProjectBasePathProperty(project) ?: return
        val path = prop.await()
        GdLibraryUpdater.getInstance(project).scheduleSkdCheck(path)
    }
}
