package gdscript.library

import GdScriptBundle
import GdscriptProjectService
import com.intellij.openapi.components.Service
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.impl.libraries.LibraryEx
import com.intellij.openapi.roots.libraries.Library
import com.intellij.platform.ide.progress.withBackgroundProgress
import kotlinx.coroutines.launch
import java.nio.charset.Charset
import java.nio.file.Path
import kotlin.io.path.readText

@Service( Service.Level.PROJECT)
class GdLibraryUpdater(private val project: Project) {

    companion object {
        fun getInstance(project: Project): GdLibraryUpdater = project.getService(GdLibraryUpdater::class.java)
    }

    private val VERSION_REGEX = "config/features=PackedStringArray\\(.*\"(\\d\\.\\d)\".*\\)".toRegex()

    fun scheduleSkdCheck() {
        GdscriptProjectService.getInstance(project).scope.launch {
            withBackgroundProgress(project, GdScriptBundle.message("progress.title.check.gdsdk.for.project")) {
                checkSdk()
            }
        }
    }

    private fun checkSdk() {
        val projectFile = Path.of(project.basePath!!, "project.godot")
        val content = projectFile.readText(Charset.defaultCharset())

        val version = VERSION_REGEX.find(content)?.groups?.get(1)?.value
                      ?: throw IllegalStateException("GdSdk version cannot be parsed from project.godot")
        if (version.startsWith("3.")) throw IllegalStateException("Godot 3.x is not supported by the plugin")

        // stop if disposed
        if (project.isDisposed) return

        val sdkPath = GdLibraryManager.extractSdkIfNeeded(version)
        GdLibraryManager.registerSdkIfNeeded(sdkPath, project)
    }

    // todo: can be removed
    private fun refreshNeeded(library: Library, latestVersion: String): Boolean {
        // no sources found
        if (library.rootProvider.getFiles(OrderRootType.SOURCES).isEmpty()) return true
        // no version or outdated version
        val props = (library.modifiableModel as LibraryEx.ModifiableModelEx).properties as GdLibraryProperties

        if (props.version.isBlank()) return true

        // Skip version date check since we're using bundled SDK
        // if (props.version.isBlank() || GdLibraryManager.libDate(props.version) != props.date) return true

        // is it correct version?
        if (!library.name!!.endsWith(latestVersion) || library.name!!.endsWith("Master"))
            return true

        // the sdk folder may not be present on the disk or stamp is invalid
        return library.rootProvider.getFiles(OrderRootType.SOURCES).any { !SdkIntegrityValidator().hasValidStamp(it) }
    }
}