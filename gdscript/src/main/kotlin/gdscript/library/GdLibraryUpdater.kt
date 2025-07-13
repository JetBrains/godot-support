package gdscript.library

import GdScriptBundle
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.impl.libraries.LibraryEx
import com.intellij.openapi.roots.libraries.Library
import com.intellij.platform.ide.progress.withBackgroundProgress
import common.util.GdScriptProjectLifetimeService
import kotlinx.coroutines.launch
import java.nio.charset.Charset
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readText

@Service( Service.Level.PROJECT)
class GdLibraryUpdater(private val project: Project) {

    companion object {
        fun getInstance(project: Project): GdLibraryUpdater = project.getService(GdLibraryUpdater::class.java)
    }

    private val VERSION_REGEX = "config/features=PackedStringArray\\(.*\"(\\d\\.\\d)\".*\\)".toRegex()

    fun scheduleSkdCheck(projectBasePath: Path) {
        GdScriptProjectLifetimeService.getInstance(project).scope.launch {
            withBackgroundProgress(project, GdScriptBundle.message("progress.title.check.gdsdk.for.project")) {
                checkSdk(projectBasePath)
            }
        }
    }

    private fun checkSdk(projectBasePath: Path) {
        val projectFile = projectBasePath.resolve("project.godot")
        if (!projectFile.exists()) return
        val content = projectFile.readText(Charset.defaultCharset())

        val version = VERSION_REGEX.find(content)?.groups?.get(1)?.value
                      ?: throw IllegalStateException("GdSdk version cannot be parsed from project.godot")
        if (version.startsWith("3.")) throw IllegalStateException("Godot 3.x is not supported by the plugin")

        // stop if disposed
        if (project.isDisposed) return

        val sdkPath = GdLibraryManager.extractSdkIfNeeded(version)
        GdLibraryManager.registerSdkIfNeeded(sdkPath, project)
    }
}