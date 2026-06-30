package gdscript.library

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.module.ModuleManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.LibraryOrderEntry
import com.intellij.openapi.roots.ModuleRootManager
import com.intellij.openapi.roots.OrderRootType
import com.intellij.openapi.roots.libraries.LibraryTablesRegistrar
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.util.concurrency.annotations.RequiresBackgroundThread
import java.nio.file.Path
import kotlin.io.path.createDirectories

object GdLibraryManager {

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
     * Registers a single SDK library whose source root is `<basePath>/.godot/rider/`.
     *
     * This is the only library we ever touch; everything produced by the SDK / GDExtension /
     * built-ins flows lives under that directory in dedicated subfolders, so the IDE
     * indexes them via this one root without any further coordination.
     *
     * The function is idempotent: if the library is already in the expected shape and the
     * module already depends on it, nothing is changed.
     */
    @RequiresBackgroundThread // findFile may block EDT
    fun registerSdkLibrary(project: Project, basePath: Path) {
        val riderDir = basePath.resolve(".godot").resolve("rider")
        runCatching { riderDir.createDirectories() }
            .onFailure {
                thisLogger().warn("Failed to create $riderDir: ${it.message}")
                return
            }
        val sourceRoot = VfsUtil.findFile(riderDir, true)
        if (sourceRoot == null) {
            thisLogger().warn("Cannot register GD library: $riderDir is not visible to VFS")
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
            thisLogger().trace("GD library already registered at $riderDir; nothing to do")
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
        thisLogger().info("Registered $SDK_LIBRARY_NAME at $riderDir")
    }
}
