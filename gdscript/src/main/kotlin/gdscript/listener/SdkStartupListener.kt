package gdscript.listener

import com.intellij.openapi.project.Project
import com.intellij.openapi.project.getProjectDataPath
import com.intellij.openapi.roots.impl.libraries.LibraryEx.ModifiableModelEx
import com.intellij.openapi.startup.ProjectActivity
import gdscript.library.GdLibraryManager
import gdscript.library.GdLibraryProperties
import gdscript.utils.GdSdkUtil.versionToSdkName
import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.charset.Charset
import java.nio.file.Paths

class SdkStartupListener : ProjectActivity {

    private val VERSION_REGEX = "config/features=PackedStringArray\\(.*\"(\\d\\.\\d)\".*\\)".toRegex()

    override suspend fun execute(project: Project) {
        var version = ""
        try {
            val projectFile = "${project.basePath}${File.separator}project.godot"
            val content = FileUtils.readFileToString(File(projectFile), Charset.defaultCharset())

            version = VERSION_REGEX.find(content)?.groups?.get(1)?.value ?: return
        } catch (e: Exception) {
            return
        }

        val dirPath = project.getProjectDataPath("sdk").toString()

        val dir = File(dirPath)
        if (!dir.exists()) dir.mkdirs()

        var registered = GdLibraryManager.listRegisteredSdks(project).firstOrNull()
        val latest = GdLibraryManager.listAvailableSdks().find { it.startsWith(version) } ?: "Master"
        val sdkPath = Paths.get(dirPath, latest.versionToSdkName()).toString()

        if (registered != null) {
            val props = (registered.modifiableModel as ModifiableModelEx).properties as GdLibraryProperties

            val updated: String
            if (props.version.isNotBlank()) {
                updated = GdLibraryManager.libDate(props.version)
            } else {
                updated = "_"
            }

            if (!registered.name!!.endsWith(latest) || updated != props.version || registered.name!!.endsWith("Master")) {
                GdLibraryManager.clearSdks(project)
                registered = null
            }
        }

        if (registered == null) {
            try {
                GdLibraryManager.download(latest, dirPath)
            } catch (e: Exception) {
            }
            GdLibraryManager.registerSdk(sdkPath, project)
        }
    }

}
