package gdscript.library

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.ide.plugins.PluginManager.getPluginByClass
import com.intellij.ide.plugins.getPluginDistDirByClass
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.LibraryOrderEntry
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import com.intellij.openapi.util.Version
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.project.stateStore
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread
import com.intellij.util.io.Decompressor
import gdscript.polySymbols.scope.GdSdkSymbolsModificationTracker
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteRecursively
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.name
import kotlin.io.path.readText

object GdLibraryManager {

    /*
     * Old Sdk
     */

    // before 252, library name used to be "GdSdk $version"
    private const val SDK_LIBRARY_NAME = "GdSdk"

    /**
     * Library names this manager owns; matched by exact name OR `startsWith` (legacy names).
     *
     * The library used to be split across three roots (`GdSdk`, `GdExtensionStubs`,
     * `GdScriptBuiltins`) during the migration, plus pre-252 versioned names like
     * `"GdSdk 4.2.1"`. We register a single library now, so any of these older entries
     * found on disk should be cleaned up on first run.
     */
    private val LEGACY_LIBRARY_PREFIXES = listOf("GdSdk", "GdExtensionStubs", "GdScriptBuiltins")

    /**
     * Registers a single SDK library whose source root is [stubsRoot] (`<basePath>/.godot/rider/`).
     *
     * This is the only library we ever touch; everything produced by the SDK / GDExtension /
     * built-ins flows lives under that directory in dedicated subfolders, so the IDE
     * indexes them via this one root without any further coordination.
     *
     * The function is idempotent: if the library is already in the expected shape and the
     * module already depends on it, nothing is changed.
     */
    @RequiresBackgroundThread // findFile may block EDT
    fun registerSdkLibrary(project: Project, stubsRoot: Path) {
        runCatching { stubsRoot.createDirectories() }
            .onFailure {
                thisLogger().warn("Failed to create $stubsRoot: ${it.message}")
                return
            }
        val sourceRoot = VfsUtil.findFile(stubsRoot, true)
        if (sourceRoot == null) {
            thisLogger().warn("Cannot register GD library: $stubsRoot is not visible to VFS")
            return
        }

        val module = ModuleManager.getInstance(project).modules.firstOrNull()
        if (module == null) {
            thisLogger().warn("Cannot register GD library: project has no modules yet")
            return
        }
        val libraryTable = LibraryTablesRegistrar.getInstance().getLibraryTable(project)

        val matchingByPrefix = libraryTable.libraries.filter { lib ->
            LEGACY_LIBRARY_PREFIXES.any { lib.name?.startsWith(it) == true }
        }
        val moduleDeps = ModuleRootManager.getInstance(module)
            .orderEntries
            .filterIsInstance<LibraryOrderEntry>()
            .mapNotNull { it.library }
            .toSet()
        val exactSdk = matchingByPrefix.singleOrNull { it.name == SDK_LIBRARY_NAME }
        if (matchingByPrefix.size == 1 &&
            exactSdk != null &&
            exactSdk.isValid(sourceRoot.url, OrderRootType.SOURCES) &&
            exactSdk in moduleDeps
        ) {
            thisLogger().trace("GD library already registered at $stubsRoot; nothing to do")
            return
        }

        ApplicationManager.getApplication().invokeAndWait {
            ApplicationManager.getApplication().runWriteAction(Runnable {
                // 1. Library table: drop anything that matches our known names, then (re)create the SDK library.
                val tableModel = libraryTable.modifiableModel
                tableModel.libraries
                    .filter { lib -> LEGACY_LIBRARY_PREFIXES.any { lib.name?.startsWith(it) == true } }
                    .forEach { tableModel.removeLibrary(it) }
                val library = tableModel.createLibrary(SDK_LIBRARY_NAME, GdLibraryKind)
                val libraryModel = library.modifiableModel
                libraryModel.addRoot(sourceRoot, OrderRootType.SOURCES)
                libraryModel.commit()
                tableModel.commit()

                // 2. Module model: drop any stale entries and (re)attach the new library.
                val rootModel = ModuleRootManager.getInstance(module).modifiableModel
                var committed = false
                try {
                    rootModel.orderEntries
                        .filterIsInstance<LibraryOrderEntry>()
                        .filter { entry ->
                            val libName = entry.libraryName ?: return@filter false
                            LEGACY_LIBRARY_PREFIXES.any { libName.startsWith(it) }
                        }
                        .forEach { rootModel.removeOrderEntry(it) }
                    rootModel.addLibraryEntry(library)
                    rootModel.commit()
                    committed = true
                } finally {
                    if (!committed) rootModel.dispose()
                }
            })
        }
        thisLogger().info("Registered $SDK_LIBRARY_NAME at $stubsRoot")
    }

    /*
     * New sdk
     */
    private fun getGodotDoctoolCommand(godotPath: String, workingDirectory: Path, outputDir: String, gdextension: Boolean = false): GeneralCommandLine{
        val commandLine = GeneralCommandLine(godotPath)
            .withWorkingDirectory(workingDirectory)
            .withParameters("--doctool", outputDir)

        if (gdextension) {
            commandLine.addParameter("--gdextension-docs")
        }

        return commandLine
    }

    /**
     * Run Godot CLI with the doctool flag to generate SDK docs
     * If gdextension is true, it will generate GDExtensions docs only, else it will generate the core SDK docs
     */
    private fun runGodotDoctool(
        project: Project,
        version: Version,
        godotPath: String,
        workingDirectory: Path,
        outputDir: String,
        directoryStampFile: Path?,
        gdextension: Boolean = false,
    ) {
        try {
            val commandLine = getGodotDoctoolCommand(godotPath, workingDirectory, outputDir, gdextension)
            val processHandler = OSProcessHandler(commandLine)
            processHandler.addProcessListener(object : ProcessListener {
                override fun processTerminated(event: ProcessEvent) {
                    if (event.exitCode != 0) {
                        thisLogger().warn("Godot doctool exited with code ${event.exitCode} (gdextension=$gdextension)")
                        return
                    }

                    directoryStampFile?.let { GdSdkIntegrityValidator.writeStamp(it, version) }
                    GdSdkSymbolsModificationTracker.getInstance(project).incModificationCount()
                }
            })
            processHandler.startNotify()
        } catch (e: Exception) {
            thisLogger().error("Failed to run Godot doctool", e)
        }
    }

    fun generateSdkIfNeeded(version: Version, project: Project, godotPathString: String) {
        val projectBasePath = project.stateStore.projectBasePath

        GdSdkPathManager.ensureDirectoriesExist(version, project)

        // 1. Generate Core SDK in a centralized location (project directory)
        val coreSdkDir = GdSdkPathManager.getCoreSdkDir(version)
        val coreSdkStampFile = GdSdkPathManager.getCoreSdkStampFile(version)

        if (!GdSdkIntegrityValidator.hasValidStamp(coreSdkStampFile, version)) {
            thisLogger().info("Generating core SDK for Godot $version in plugin directory")
            runGodotDoctool(project, version, godotPathString, projectBasePath, coreSdkDir.absolutePathString(), coreSdkStampFile)
        }

        // 2. Generate GDExtensions in a project-specific location
        val extensionsDir = GdSdkPathManager.getProjectExtensionsDir(project)
        val extensionsStampFile = GdSdkPathManager.getProjectExtensionsStampFile(project)

        if (extensionsDir != null && extensionsStampFile != null) {
            if (!GdSdkIntegrityValidator.hasValidStamp(extensionsStampFile, version)) {
                thisLogger().info("Generating GDExtensions for project")
                runGodotDoctool(project, version, godotPathString, projectBasePath, extensionsDir.absolutePathString(), extensionsStampFile, gdextension = true)
            }
        }
    }

}
