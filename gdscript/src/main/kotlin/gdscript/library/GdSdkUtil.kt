package gdscript.library

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Version
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readText

// TODO Delete after migration of GdLibraryUpdater to ReferenceGdLibrariesProjectActivity
object GdSdkUtil {

    val VERSION_REGEX = "config/features=PackedStringArray\\(.*\"(\\d\\.\\d)\".*\\)".toRegex()

    private const val DEFAULT_SDK_VERSION = "4.5"

    fun getGodotVersion(project: Project): Version {
        val projectGodot = GodotCommunityUtil.getGodotProjectBasePath(project)
            ?.resolve("project.godot")?.takeIf { it.exists() }
        return projectGodot?.let { getGodotVersion(it) } ?: Version.parseVersion(DEFAULT_SDK_VERSION)!!
    }

    fun getGodotVersion(projectFile: Path): Version? {
        try {
            val content = projectFile.readText()

            // todo: get full version from the FileVersionInfo on Windows, Contents/Info.plist on Mac, parse file name on Linux
            // also possible to run `godot --version` and parse the output
            val versionString = VERSION_REGEX.find(content)?.groups?.get(1)?.value
                ?: throw IllegalStateException("GdSdk version cannot be parsed from project.godot")
            val version = Version.parseVersion(versionString)
            if (version?.major == 3) throw IllegalStateException("Godot 3.x is not supported by the plugin")
            return version
        } catch (_: Exception) {
            thisLogger().warn("Failed to get Godot version from project.godot file: ${projectFile}")
            return Version.parseVersion(DEFAULT_SDK_VERSION)
        }
    }
}