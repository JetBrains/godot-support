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
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.util.io.Decompressor
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.*

object GdLibraryManager {

    // before 252, library name used to be "GdSdk $version"
    private const val LIBRARY_NAME = "GdSdk"

    fun registerSdkIfNeeded(path: Path, project: Project) {
        val sourceRoot = LocalFileSystem.getInstance().refreshAndFindFileByPath(path.pathString)

        if (sourceRoot == null) {
            throw Exception("Cannot find SDK at $path")
        }

        val module = ModuleManager.getInstance(project).modules.first()

        val libraryTable = LibraryTablesRegistrar.getInstance().getLibraryTable(project)
        libraryTable.getLibraryByName(LIBRARY_NAME)?.let { lib->
            // I see this doesn't work on the dev build
            // different casing of letters
            // /Users/ivan.shakhov/Work/ultimate/out/dev-run/Rider
            // vs
            // /Users/ivan.shakhov/Work/ultimate/out/dev-run/rider
            if (lib.isValid(sourceRoot.url, OrderRootType.SOURCES)
                && libraryTable.libraries.count { library -> library.name?.startsWith(LIBRARY_NAME) == true } == 1
                && ModuleRootManager.getInstance(module)
                    .orderEntries
                    .filterIsInstance<LibraryOrderEntry>()
                    .any { it.library == lib }) { // check that module has dependency on this library
                return@registerSdkIfNeeded
            }
        }

        var tableModel = libraryTable.modifiableModel
        val libraries = tableModel.libraries.filter { library -> library.name?.startsWith(LIBRARY_NAME) == true }

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
        var name = getPluginByClass(GdLibraryManager::class.java)?.name
        if (name == null) {
            thisLogger().error("Cannot find Godot plugin ID")
            name = "GdScript"
        }
        val extractionDir = PathManager.getPluginsDir().resolve(name).resolve("extracted")

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
        return requestedSdkDir
    }

    // todo: check with OSS build
    fun extractSdkIfNeeded(version: String): Path {
        val bundledSdkPath = getPluginDistDirByClass(this::class.java)?.resolve("sdk/sdk.tar.xz").takeIf { it?.exists() == true } ?: error("Bundled SDK not found")
        return extractSdkIfNeededInternal(version, bundledSdkPath)
    }

    private fun findSdkVersion(extractionDir: Path, version: String): Path {
        // version from the project.godot is always "major.minor", lets always use the highest patch version available
        val prefix = Version.parseVersion(version)

        if (prefix != null) {
            val bestMatch = extractionDir.listDirectoryEntries()
                .asSequence()
                .filter { Files.isDirectory(it) }
                .mapNotNull { dir ->
                    val parsed = Version.parseVersion(dir.name)
                    if (parsed != null) parsed to dir else null
                }
                .filter { (ver, _) -> ver.major == prefix.major && ver.minor == prefix.minor }
                .sortedByDescending { (semVer, _) -> semVer }
                .firstOrNull()?.second

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
