package gdscript.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import gdscript.library.GdLibraryUpdater

class SdkStartupListener : ProjectActivity {

    override suspend fun execute(project: Project) {
        if (project.isDisposed) return
        GdLibraryUpdater.scheduleSkdCheck(project)
    }

}
