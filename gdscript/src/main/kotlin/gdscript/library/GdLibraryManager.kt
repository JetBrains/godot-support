package gdscript.library

import com.intellij.ide.plugins.getPluginDistDirByClass
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootModificationUtil
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.util.io.Decompressor
import gdscript.utils.GdSdkUtil.versionToSdkName
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.pathString

object GdLibraryManager {

    val LIBRARY_NAME = "GdSdk"

    // alternative to registerSdk
    //fun addBCLApiSources(path: Path, project: Project) {
    //    val sourceRoot = LocalFileSystem.getInstance().refreshAndFindFileByPath(path.pathString)
    //
    //    ApplicationManager.getApplication().invokeLater {
    //        ApplicationManager.getApplication().runWriteAction(Runnable {
    //            val module = ModuleManager.getInstance(project).modules.first()
    //            val rootManager = ModuleRootManager.getInstance(module)
    //            val model = rootManager.getModifiableModel()
    //            try {
    //                if (!model.contentRoots.any { it == sourceRoot }) {
    //                    if (sourceRoot != null) {
    //                        model.addContentEntry(sourceRoot, object : ProjectModelExternalSource {
    //                            override fun getDisplayName(): String =
    //                                "gdscript bcl"
    //
    //                            override fun getId(): String =
    //                                "gdscriptBcl"
    //                        })
    //                    }
    //                    model.commit()
    //                }
    //                else {
    //                    model.dispose()
    //                }
    //            }
    //            catch (e: IOException) {
    //                e.printStackTrace()
    //            }
    //            finally {
    //                if (model.isWritable()) {
    //                    model.dispose()
    //                }
    //            }
    //        })
    //    }
    //}

    fun registerSdkIfNeeded(path: Path, project: Project) {
        val sourceRoot = LocalFileSystem.getInstance().refreshAndFindFileByPath(path.pathString)

        if (sourceRoot == null) {
            throw Exception("Cannot find SDK at $path")
        }

        val libraryTable = LibraryTablesRegistrar.getInstance().getLibraryTable(project)
        val tableModel = libraryTable.modifiableModel

        // not sure, at times during development, I had to remove occasional libs
        // libraryTable.libraries.forEach { libraryTable.removeLibrary(it) }

        var library = tableModel.getLibraryByName(LIBRARY_NAME)
        val module = ModuleManager.getInstance(project).modules.first()
        if (library == null) {
            library = tableModel.createLibrary(LIBRARY_NAME, GdLibraryKind)
        }
        val libraryModel = library.modifiableModel
        library.rootProvider.getUrls(OrderRootType.SOURCES)
            .forEach { libraryModel.removeRoot(it, OrderRootType.SOURCES) }
        libraryModel.addRoot(sourceRoot, OrderRootType.SOURCES)
        ApplicationManager.getApplication().invokeAndWait {
            ApplicationManager.getApplication().runWriteAction(Runnable {
                tableModel.commit()
                libraryModel.commit()
                ModuleRootModificationUtil.addDependency(module, library)
            })
        }
    }

    private fun extractSdkIfNeededInternal(version: String, bundledSdkPath: Path): Path {
        val extractionDir = bundledSdkPath.parent.resolve("extracted")

        // Check if SDK is already extracted and has a valid stamp
        val validator = SdkIntegrityValidator()
        val extractionStampFile = extractionDir.resolve(SdkIntegrityValidator.STAMP_FILE_NAME).toFile()
        val needsExtraction = !extractionDir.toFile().exists() ||
                              !extractionStampFile.exists() ||
                              extractionStampFile.readText().trim() != validator.getFilesFromFs(extractionDir).count().toString()

        // Extract SDK if needed
        if (needsExtraction) {
            if (extractionDir.toFile().exists()) {
                extractionDir.toFile().deleteRecursively()
            }
            Files.createDirectories(extractionDir)

            // Extract the SDK
            Decompressor.Tar(bundledSdkPath).extract(extractionDir)
            validator.writeStamp(extractionDir.toFile())
        }

        // Find the requested SDK version in the extracted directory
        val requestedSdkDir = findSdkVersion(extractionDir, version)
        if (requestedSdkDir == null) {
            throw Exception("Requested SDK version $version not found in $extractionDir")
        }

        return requestedSdkDir
    }

    // todo: check with OSS build
    fun extractSdkIfNeeded(version: String): Path {
        val bundledSdkPath = getPluginDistDirByClass(this::class.java)?.resolve("sdk/sdk.tar.xz").takeIf { it?.exists() == true } ?: error("Bundled SDK not found")
        return extractSdkIfNeededInternal(version, bundledSdkPath)
    }

    // old approach
    fun extractSdkIfNeeded2(version: String): Path {
        val pluginDirs = arrayListOf(
            Path(PathManager.getPluginsPath()),
            Path(PathManager.getPreInstalledPluginsPath()))

        val potentialSdkPaths = pluginDirs.map { dir ->
            dir.resolve("rider-gdscript/sdk/sdk.tar.xz") }
        val bundledSdkPath = potentialSdkPaths.singleOrNull { it.exists() }

        if (bundledSdkPath == null) {
            throw Exception("Bundled SDK not found at $potentialSdkPaths")
        }
        return extractSdkIfNeededInternal(version, bundledSdkPath)
    }

    private fun findSdkVersion(extractionDir: Path, version: String): Path? {
        // Look for a directory that matches the requested version
        val versionDirs = Files.list(extractionDir)
            .filter { Files.isDirectory(it) }
            .filter { it.fileName.toString().contains(version) }
            .toList()

        // If we found an exact match, use it
        val exactMatch = versionDirs.find { it.fileName.toString() == version.versionToSdkName() }
        if (exactMatch != null) {
            thisLogger().info("Use $exactMatch for $version.")
            return exactMatch
        }

        // If we found a partial match (e.g. "4.0" matches "4.0.1"), use the first one
        if (versionDirs.isNotEmpty()) {
            val dir = versionDirs[0]
            thisLogger().info("Use $dir for $version.")
            return dir
        }

        // If we didn't find any match, look for a "master" version
        val masterDir = Files.list(extractionDir)
            .filter { Files.isDirectory(it) }
            .filter { it.fileName.toString().equals("master", ignoreCase = true) }
            .findFirst()

        thisLogger().info("Use $masterDir for $version.")
        return masterDir.orElse(null)
    }
}
