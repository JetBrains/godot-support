package gdscript.library

import com.intellij.ide.plugins.getPluginDistDirByClass
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ModuleRootModificationUtil
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.util.io.Decompressor
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.pathString

object GdLibraryManager {

    // before 252, library name used to be "GdSdk $version"
    private const val LIBRARY_NAME = "GdSdk"

    fun registerSdkIfNeeded(path: Path, project: Project) {
        val sourceRoot = LocalFileSystem.getInstance().refreshAndFindFileByPath(path.pathString)

        if (sourceRoot == null) {
            throw Exception("Cannot find SDK at $path")
        }

        val libraryTable = LibraryTablesRegistrar.getInstance().getLibraryTable(project)
        libraryTable.getLibraryByName(LIBRARY_NAME)?.let {
            // I see this doesn't work on the dev build
            // different casing of letters
            // /Users/ivan.shakhov/Work/ultimate/out/dev-run/Rider
            // vs
            // /Users/ivan.shakhov/Work/ultimate/out/dev-run/rider
            if (it.isValid(sourceRoot.url, OrderRootType.SOURCES)
                && libraryTable.libraries.count { library -> library.name?.startsWith(LIBRARY_NAME) == true } == 1) {
                return@registerSdkIfNeeded
            }
        }

        var tableModel = libraryTable.modifiableModel
        val libraries = tableModel.libraries.filter { library -> library.name?.startsWith(LIBRARY_NAME) == true }

        val module = ModuleManager.getInstance(project).modules.first()
        if (libraries.any()) {
            for (library in libraries) {
                tableModel.removeLibrary(library)
            }

            ApplicationManager.getApplication().invokeAndWait {
                ApplicationManager.getApplication().runWriteAction(Runnable {
                    tableModel.commit()
                })
            }
        }

        tableModel = libraryTable.modifiableModel
        val library = tableModel.createLibrary(LIBRARY_NAME, GdLibraryKind)
        val libraryModel = library.modifiableModel
        libraryModel.addRoot(sourceRoot, OrderRootType.SOURCES)

        ApplicationManager.getApplication().invokeAndWait {
            ApplicationManager.getApplication().runWriteAction(Runnable {
                libraryModel.commit()
                tableModel.commit()
            })
            ModuleRootModificationUtil.addDependency(module, library)
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

    private fun findSdkVersion(extractionDir: Path, version: String): Path? {
        // Look for a directory that matches the requested version
        val versionDirs = Files.list(extractionDir)
            .filter { Files.isDirectory(it) }
            .filter { it.fileName.toString().contains(version) }
            .toList()

        // If we found an exact match, use it
        val exactMatch = versionDirs.find { it.fileName.toString() == "$LIBRARY_NAME $version" }
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
