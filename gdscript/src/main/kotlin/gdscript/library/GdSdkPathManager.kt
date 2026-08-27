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
        // todo: test with .sln
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

    /** The generated singleton docs get their own folder: the gdextensions one is owned end to end by the doctool. */
    fun getProjectSingletonsDocDir(project: Project): Path? {
        return getProjectExtensionsRoot(project)?.resolve("singletons")?.resolve("doc_classes")
    }

    /** The project-specific directories the generated documentation XMLs are written to. */
    fun getProjectDocDirs(project: Project): List<Path> {
        return listOfNotNull(getProjectExtensionsDir(project), getProjectSingletonsDocDir(project))
    }

    fun getProjectSingletonsDocFile(project: Project): Path? {
        return getProjectSingletonsDocDir(project)?.resolve(GdGlobalSingletonsDocWriter.FILE_NAME)
    }

    fun getProjectSingletonsStampFile(project: Project): Path? {
        return getProjectExtensionsRoot(project)?.resolve("stamp-singletons.txt")
    }

    /**
     * The dump script lives inside the plugin jar, so Godot cannot read it directly. `--script` accepts an absolute path
     * outside the project root, so it is materialized under a fixed name in the IDE's temp directory and simply
     * overwritten on every generation - no temporary file has to be tracked and cleaned up.
     */
    fun getSingletonsScriptFile(): Path {
        return PathManager.getTempDir()
            .resolve(DOCS_DIR_NAME)
            .resolve("dump_singletons.gd")
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