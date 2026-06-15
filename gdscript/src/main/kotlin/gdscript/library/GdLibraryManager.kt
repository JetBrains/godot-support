package gdscript.library

import com.intellij.ide.plugins.PluginManager.getPluginByClass
import com.intellij.ide.plugins.getPluginDistDirByClass
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.LibraryOrderEntry
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.ModuleRootModificationUtil
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import com.intellij.openapi.util.Version
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread
import com.intellij.util.io.Decompressor
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteRecursively
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlin.io.path.readText

@OptIn(ExperimentalPathApi::class)
object GdLibraryManager {

    // before 252, library name used to be "GdSdk $version"
    private const val SDK_LIBRARY_NAME = "GdSdk"
    private const val EXTENSION_STUBS_LIBRARY_NAME = "GdExtensionStubs"

    fun registerSdkIfNeeded(path: Path, project: Project) {
        registerLibraryIfNeeded(SDK_LIBRARY_NAME, path, project)
    }

    fun registerExtensionStubsIfNeeded(path: Path, project: Project) {
        registerLibraryIfNeeded(EXTENSION_STUBS_LIBRARY_NAME, path, project)
    }

    @RequiresBackgroundThread // findFile may block EDT
    private fun registerLibraryIfNeeded(libraryName: String, path: Path, project: Project) {
        val sourceRoot = VfsUtil.findFile(path, true)

        if (sourceRoot == null) {
            thisLogger().error("registerLibraryIfNeeded failed: ${libraryName} ${path}")
            return
        }

        val module = ModuleManager.getInstance(project).modules.first()

        val libraryTable = LibraryTablesRegistrar.getInstance().getLibraryTable(project)
        libraryTable.getLibraryByName(libraryName)?.let { lib->
            // I see this doesn't work on the dev build
            // different casing of letters
            // /Users/ivan.shakhov/Work/ultimate/out/dev-run/Rider
            // vs
            // /Users/ivan.shakhov/Work/ultimate/out/dev-run/rider
            if (lib.isValid(sourceRoot.url, OrderRootType.SOURCES)
                && libraryTable.libraries.count { library -> library.name?.startsWith(libraryName) == true } == 1
                && ModuleRootManager.getInstance(module)
                    .orderEntries
                    .filterIsInstance<LibraryOrderEntry>()
                    .any { it.library == lib }) { // check that module has dependency on this library
                thisLogger().trace("Library $libraryName already registered at $path; nothing to do")
                return
            }
        }

        var tableModel = libraryTable.modifiableModel
        val libraries = tableModel.libraries.filter { library -> library.name?.startsWith(libraryName) == true }

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
        val library = tableModel.createLibrary(libraryName, GdLibraryKind)
        val libraryModel = library.modifiableModel
        libraryModel.addRoot(sourceRoot, OrderRootType.SOURCES)

        ApplicationManager.getApplication().invokeAndWait {
            ApplicationManager.getApplication().runWriteAction(Runnable {
                libraryModel.commit()
                tableModel.commit()
            })
            ModuleRootModificationUtil.addDependency(module, library)
        }
        thisLogger().info("Registered library $libraryName at $path")
    }

    private fun extractSdkIfNeededInternal(version: Version?, bundledSdkPath: Path): Path {
        var name = getPluginByClass(this::class.java)?.name
        if (name == null) {
            thisLogger().error("Cannot find Godot plugin ID")
            name = "GdScript"
        }
        val extractionDir = PathManager.getPluginsDir().resolve(name).resolve("extracted")

        // Check if SDK is already extracted and has a valid stamp
        val extractionStampFile = extractionDir.resolve(SdkIntegrityValidator.STAMP_FILE_NAME)
        val needsExtraction = !extractionDir.exists() ||
                              !extractionStampFile.exists() ||
                              extractionStampFile.readText().trim() != SdkIntegrityValidator.getFilesFromFs(extractionDir).count().toString()

        // Extract SDK if needed
        if (needsExtraction) {
            if (extractionDir.exists()) {
                extractionDir.deleteRecursively()
            }
            extractionDir.createDirectories()

            // Extract the SDK
            Decompressor.Tar(bundledSdkPath).extract(extractionDir)
            SdkIntegrityValidator.writeStamp(extractionDir)
        }

        // Find the requested SDK version in the extracted directory
        val requestedSdkDir = findSdkVersion(extractionDir, version)
        return requestedSdkDir
    }

    // todo: check with OSS build
    fun extractSdkIfNeeded(version: Version?): Path {
        val bundledSdkPath = getPluginDistDirByClass(this::class.java)?.resolve("sdk/sdk.tar.xz").takeIf { it?.exists() == true } ?: error("Bundled SDK not found")
        return extractSdkIfNeededInternal(version, bundledSdkPath)
    }

    private fun findSdkVersion(extractionDir: Path, version: Version?): Path {
        if (version != null) {
            val bestMatch = extractionDir.listDirectoryEntries()
                .asSequence()
                .filter { Files.isDirectory(it) }
                .mapNotNull { dir ->
                    val parsed = Version.parseVersion(dir.name)
                    if (parsed != null) parsed to dir else null
                }
                .filter { (ver, _) -> ver.major == version.major && ver.minor == version.minor }.maxByOrNull { (semVer, _) -> semVer }?.second

            if (bestMatch != null) {
                thisLogger().info("Use $bestMatch version for $version.")
                return bestMatch
            }
        }

        // If we didn't find any match, look for a "Master" version
        val masterDir = extractionDir.resolve("Master")
        if (masterDir.isDirectory()) {
            thisLogger().info("Use $masterDir for $version.")
            return masterDir
        }

        throw Exception("Requested SDK version $version not found in $extractionDir")
    }
}
