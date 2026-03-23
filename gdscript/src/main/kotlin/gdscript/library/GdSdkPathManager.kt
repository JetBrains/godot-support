package gdscript.library

import com.intellij.ide.plugins.PluginManager.getPluginByClass
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Version
import com.intellij.project.stateStore
import gdscript.utils.getMainProjectBasePath
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists

object GdSdkPathManager {

    private const val DOCS_DIR_NAME = "doctool"

    fun getCoreSdkDocsRoot(): Path {
        var name = getPluginByClass(GdLibraryManager::class.java)?.name
        if (name == null) {
            thisLogger().warn("Cannot find Godot plugin ID")
            name = "GdScript"
        }
        return PathManager.getPluginsDir()
            .resolve(name)
            .resolve(DOCS_DIR_NAME)
    }

    fun getProjectExtensionsRoot(project: Project): Path? {
        return (project.stateStore.directoryStorePath ?: project.getMainProjectBasePath()?.resolve(Project.DIRECTORY_STORE_FOLDER))
            ?.resolve(DOCS_DIR_NAME)
    }


    fun getCoreSdkDir(version: Version): Path {
        return getCoreSdkDocsRoot().resolve(version.toString())
    }

    fun getCoreSdkStampFile(version: Version): Path {
        return getCoreSdkDocsRoot().resolve("stamp-$version.txt")
    }

    fun getProjectExtensionsDir(project: Project): Path? {
        return getProjectExtensionsRoot(project)?.resolve("gdextensions")
    }

    fun getProjectExtensionsStampFile(project: Project): Path? {
        return getProjectExtensionsRoot(project)?.resolve("stamp-gdext.txt")
    }

    private fun ensureDirectoryExists(path: Path){
        if (!path.exists()){
            try {
                Files.createDirectories(path)
            } catch (e: Exception) {
                thisLogger().error("Failed to create directory at $path", e)
            }
        }
    }

    fun ensureDirectoriesExist(version: Version, project: Project) {
        val coreSdkDir = getCoreSdkDir(version)
        ensureDirectoryExists(coreSdkDir)

        val projectExtensionsDir = getProjectExtensionsDir(project)
            ?: throw IllegalStateException("Project extensions directory not found for project: ${project.name}. Failed to create directory")
        ensureDirectoryExists(projectExtensionsDir)
    }
}