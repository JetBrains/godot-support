package gdscript.library

import PluginConstants
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task.Backgroundable
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.getProjectDataPath
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.impl.libraries.LibraryEx
import com.intellij.openapi.roots.libraries.Library
import com.intellij.util.io.createDirectories
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.LinkOption
import java.nio.file.Paths
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readText


class GdLibraryUpdater {

    companion object{
        fun scheduleSkdCheck(project: Project) {
            ProgressManager.getInstance().run(GdCheckSdkTask(project))
        }
    }

    private class GdCheckSdkTask(project: Project) : Backgroundable(project, "Check GdSdk for project") {

        private val VERSION_REGEX = "config/features=PackedStringArray\\(.*\"(\\d\\.\\d)\".*\\)".toRegex()

        override fun run(progressIndicator: ProgressIndicator) {
            try {
                checkSdk(progressIndicator)
            } catch (exception: IOException) {
                // ignore non-projects
            } catch (exception: Exception) {
                NotificationGroupManager.getInstance()
                        .getNotificationGroup(PluginConstants.SDK_NOTIFICATION_GROUP_ID)
                        .createNotification("Failed to verify project SDK with error '${exception.message}'", NotificationType.ERROR)
                        .notify(project)
            }
        }

        fun checkSdk(progressIndicator: ProgressIndicator) {
            progressIndicator.isIndeterminate = false
            val projectFile = Path.of(project.basePath!!, "project.godot")
            val content = projectFile.readText(Charset.defaultCharset())

            val version = VERSION_REGEX.find(content)?.groups?.get(1)?.value
                    ?: throw IllegalStateException("GdSdk version cannot be parsed from project.godot")
            if (version.startsWith("3.")) throw IllegalStateException("Godot 3.x is not supported by the plugin")
            progressIndicator.fraction = 0.33

            val registered = GdLibraryManager.getRegisteredSdk(project)
            progressIndicator.fraction = 0.66

            val latest = GdLibraryManager.listAvailableSdks().find { it.startsWith(version) } ?: "Master"
            progressIndicator.fraction = 0.99

            // stop if disposed
            if (project.isDisposed) return

            if (registered == null) {
                ProgressManager.getInstance().run(GdUpdateSdkTask(project, latest, false))
            } else if (refreshNeeded(registered, latest)) {
                ProgressManager.getInstance().run(GdUpdateSdkTask(project, latest, true))
            } else {
                thisLogger().info("Continue using $registered gdscript sdk.")
            }
        }

        private fun refreshNeeded(library: Library, latestVersion: String): Boolean {
            // no sources found
            if (library.rootProvider.getFiles(OrderRootType.SOURCES).isEmpty()) return true
            // no version or outdated version
            val props = (library.modifiableModel as LibraryEx.ModifiableModelEx).properties as GdLibraryProperties

            if (props.version.isBlank()) return true

            if (props.version.isBlank() || GdLibraryManager.libDate(props.version) != props.date) return true
            // is it correct version?
            if (!library.name!!.endsWith(latestVersion) || library.name!!.endsWith("Master"))
                return true

            // the sdk folder may not be present on the disk
            return library.rootProvider.getFiles(OrderRootType.SOURCES).any { !SdkIntegrityValidator().hasValidStamp(it) }
        }
    }

    private class GdUpdateSdkTask(project: Project, val version: String, val requiresReset: Boolean) : Backgroundable(project, "Update GdSdk for project") {

        override fun run(progressIndicator: ProgressIndicator) {
            try {
                val dirPath = project.getProjectDataPath("sdk")
                thisLogger().info("update gdscript sdk $dirPath")
                if (!dirPath.exists(LinkOption.NOFOLLOW_LINKS)) dirPath.createDirectories()

                if (requiresReset) GdLibraryManager.clearSdks(project)

                val sdkPath = GdLibraryManager.download(progressIndicator, version, dirPath)
                GdLibraryManager.registerSdk(sdkPath.toString(), project)
            } catch (exception: Exception) {
                NotificationGroupManager.getInstance()
                        .getNotificationGroup(PluginConstants.SDK_NOTIFICATION_GROUP_ID)
                        .createNotification("Failed to download project SDK with error '${exception.message}'", NotificationType.ERROR)
                        .notify(project)
            }
        }
    }
}
