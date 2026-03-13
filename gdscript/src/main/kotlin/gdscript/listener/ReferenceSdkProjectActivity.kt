package gdscript.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import gdscript.library.GdLibraryUpdater

class ReferenceSdkProjectActivity : ProjectActivity {

    override suspend fun execute(project: Project) {
        if (project.isDisposed) return

        val flow = GodotCommunityUtil.getGodotProjectBasePathFlow(project)
        flow.collect { basePath ->
            if (basePath == null) return@collect
            GdLibraryUpdater.getInstance(project).scheduleSkdCheck(basePath)
        }
    }
}
