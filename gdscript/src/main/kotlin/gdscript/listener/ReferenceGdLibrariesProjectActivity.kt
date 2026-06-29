package gdscript.listener

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.CapturingProcessHandler
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.util.registry.RegistryManager
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.platform.ide.progress.withBackgroundProgress
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import com.jetbrains.rider.godot.community.GdScriptProjectLifetimeService
import gdscript.GdScriptBundle
import gdscript.library.GdExtensionWatchService
import gdscript.library.GdLibraryManager
import gdscript.sdk.xml.GdNameSanitizer
import gdscript.sdk.xml.XmlToGd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runInterruptible
import java.io.IOException
import java.net.URI
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.nio.file.attribute.BasicFileAttributes
import kotlin.io.path.ExperimentalPathApi
import kotlin.io.path.createDirectories
import kotlin.io.path.deleteRecursively
import kotlin.io.path.exists
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.pathString
import kotlin.io.path.readText
import kotlin.io.path.writeText
import kotlin.time.Duration.Companion.milliseconds

@OptIn(FlowPreview::class, ExperimentalPathApi::class)
class ReferenceGdLibrariesProjectActivity : ProjectActivity {

    private data class SdkInputs(val executable: Path, val basePath: Path)
    private data class GdExtInputs(val executable: Path, val basePath: Path, val snapshot: GdExtensionWatchService.Snapshot)

    override suspend fun execute(project: Project) {
        if (project.isDisposed) return
        val scope = GdScriptProjectLifetimeService.getInstance(project).scope
        scope.launch {
            registerLibrary(project)
        }
        scope.launch {
            provisionSdk(project)
        }
        scope.launch {
            provisionGdExtension(project)
        }
        scope.launch {
            downloadGdScriptXml(project)
        }
    }

    /**
     * Registers `<basePath>/.godot/rider/` as the single GD library root as soon as the
     * project's base path is known. The directory is created on demand so the library is
     * stable even before any stubs are produced; provisioners (SDK / GDExtension / built-ins)
     * just drop their `.gdf` files into dedicated subdirectories and the IDE picks them up.
     */
    private suspend fun registerLibrary(project: Project) {
        val basePath = GodotCommunityUtil.getGodotProjectBasePathFlow(project)
            .filterNotNull()
            .first()
        if (project.isDisposed) return
        runInterruptible(Dispatchers.IO) {
            GdLibraryManager.registerSdkLibrary(project, basePath)
        }
    }

    private suspend fun downloadGdScriptXml(project: Project) {
        val basePath = GodotCommunityUtil.getGodotProjectBasePathFlow(project)
            .filterNotNull()
            .first()
        val targetDir = basePath.resolve(".godot").resolve("rider").resolve("builtins")
        val targetFile = targetDir.resolve("@GDScript.xml")
        try {
            runInterruptible(Dispatchers.IO) {
                targetDir.createDirectories()
                val url = URI(GDSCRIPT_XML_URL).toURL()
                url.openStream().use { input ->
                    Files.copy(input, targetFile, StandardCopyOption.REPLACE_EXISTING)
                }
            }
            thisLogger().info("Downloaded @GDScript.xml to $targetFile")
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            thisLogger().warn("Failed to download @GDScript.xml: ${e.message}")
        }

        // Convert the downloaded XML to a .gdf stub next to it; if the file is missing
        // (download failed and no previous copy exists), we silently skip registration.
        if (!targetFile.exists()) {
            thisLogger().warn("@GDScript.xml not available at $targetFile; skipping built-ins registration")
            return
        }
        val produced = runCatching {
            runInterruptible(Dispatchers.IO) {
                convertXmlToGdf(targetDir, targetDir)
                currentGdfCount(targetDir) > 0
            }
        }.getOrElse {
            thisLogger().warn("Failed to convert @GDScript.xml to .gdf: ${it.message}")
            false
        }
        if (!produced) {
            thisLogger().warn("No .gdf produced from @GDScript.xml at $targetDir; skipping built-ins registration")
            return
        }
        VfsUtil.markDirtyAndRefresh(true, true, true, targetDir)
    }

    private suspend fun provisionSdk(project: Project) {
        combine(
            GodotCommunityUtil.getGodotExecutablePathFlow(project),
            GodotCommunityUtil.getGodotProjectBasePathFlow(project),
        ) { exe, basePath ->
            if (exe != null && basePath != null) SdkInputs(exe, basePath) else null
        }
            .filterNotNull()
            .debounce(500.milliseconds)
            .collectLatest { inputs ->
                if (project.isDisposed) return@collectLatest
                val exe = inputs.executable
                val basePath = inputs.basePath
                val stubsDir = basePath.resolve(".godot").resolve("rider").resolve("sdk")
                val exeFingerprint = computeFileFingerprint(exe)
                thisLogger().trace(
                    "SDK provision tick: exe=$exe base=$basePath fingerprint=$exeFingerprint"
                )

                val expectedStamp = sdkStamp(exeFingerprint, stubsDir)
                val staleReason = stubsStaleReason(stubsDir, expectedStamp)
                if (staleReason == null) {
                    thisLogger().trace("SDK stubs up-to-date at $stubsDir")
                    return@collectLatest
                }
                thisLogger().info("SDK stubs missing or stale at $stubsDir ($staleReason); regenerating")
                withBackgroundProgress(project, GdScriptBundle.message("progress.title.check.gdsdk.for.project")) {
                    runCatching { stubsDir.deleteRecursively() }
                        .onFailure { thisLogger().warn("Failed to clean SDK stubs dir at $stubsDir: ${it.message}") }
                    stubsDir.createDirectories()
                    if (!runDoctool(exe, basePath, stubsDir, gdextensions = false)) return@withBackgroundProgress
                    val docClassesDir = stubsDir.resolve("doc/classes")
                    convertXmlToGdf(docClassesDir, stubsDir)
                    val gdfCount = currentGdfCount(stubsDir)
                    if (gdfCount == 0) {
                        thisLogger().warn("--doctool finished but no SDK stubs were produced at $stubsDir")
                        return@withBackgroundProgress
                    }
                    writeStamp(stubsDir, sdkStamp(exeFingerprint, stubsDir))
                    thisLogger().info("Generated $gdfCount SDK .gdf files at $stubsDir")
                    VfsUtil.markDirtyAndRefresh(true, true, true, stubsDir)
                }
            }
    }

    private suspend fun provisionGdExtension(project: Project) {
        combine(
            GodotCommunityUtil.getGodotExecutablePathFlow(project),
            GodotCommunityUtil.getGodotProjectBasePathFlow(project),
            GdExtensionWatchService.getInstance(project).extensionsFlow,
        ) { exe, basePath, snapshot ->
            if (exe != null && basePath != null) GdExtInputs(exe, basePath, snapshot) else null
        }
            .filterNotNull()
            .debounce(500.milliseconds)
            .collectLatest { inputs ->
                if (project.isDisposed) return@collectLatest
                val exe = inputs.executable
                val basePath = inputs.basePath
                val stubsDir = basePath.resolve(".godot").resolve("rider").resolve("gdextension")
                thisLogger().trace(
                    "GDExtension provision tick: exe=$exe base=$basePath " +
                    "gdextensions=${inputs.snapshot.gdextensions.size} " +
                    "libraries=${inputs.snapshot.libraries.size} " +
                    "fingerprint=${inputs.snapshot.fingerprint}"
                )

                if (inputs.snapshot.gdextensions.isEmpty()) {
                    thisLogger().trace("No .gdextension files; clean up the stubsDir")
                    runCatching { stubsDir.deleteRecursively() }
                        .onFailure { thisLogger().warn("Failed to clean stubs dir at $stubsDir: ${it.message}") }
                    VfsUtil.markDirtyAndRefresh(true, true, true, stubsDir)
                    return@collectLatest
                }

                val expectedStamp = gdExtensionStamp(inputs.snapshot, stubsDir)
                val staleReason = stubsStaleReason(stubsDir, expectedStamp)
                if (staleReason == null) {
                    thisLogger().trace("Stubs up-to-date at $stubsDir")
                    return@collectLatest
                }
                thisLogger().info("Stubs missing or stale at $stubsDir ($staleReason); regenerating")
                withBackgroundProgress(project, GdScriptBundle.message("progress.title.generate.gdextension.stubs")) {
                    runCatching { stubsDir.deleteRecursively() }
                        .onFailure { thisLogger().warn("Failed to clean stubs dir at $stubsDir: ${it.message}") }
                    stubsDir.createDirectories()
                    if (!runDoctool(exe, basePath, stubsDir, gdextensions = true)) return@withBackgroundProgress
                    val docClassesDir = stubsDir.resolve("doc_classes")
                    convertXmlToGdf(docClassesDir, stubsDir)
                    val gdfCount = currentGdfCount(stubsDir)
                    if (gdfCount == 0) {
                        thisLogger().warn("--doctool finished but no stubs were produced at $stubsDir")
                        return@withBackgroundProgress
                    }
                    writeStamp(stubsDir, gdExtensionStamp(inputs.snapshot, stubsDir))
                    thisLogger().info("Generated $gdfCount .gdf files at $stubsDir")
                    VfsUtil.markDirtyAndRefresh(true, true, true, stubsDir)
                }
            }
    }

    private fun stubsStaleReason(stubsDir: Path, expectedStamp: String): String? {
        if (!stubsDir.exists()) return "stubs dir missing"
        val actual = readStamp(stubsDir) ?: return ".fingerprint missing"
        if (actual != expectedStamp) return "stamp mismatch: existing=$actual expected=$expectedStamp"
        return null
    }

    private fun sdkStamp(exeFingerprint: Long, stubsDir: Path): String =
        "sdk:$exeFingerprint:${currentGdfCount(stubsDir)}"

    private fun gdExtensionStamp(snapshot: GdExtensionWatchService.Snapshot, stubsDir: Path): String =
        "${snapshot.gdextensions.size}:${snapshot.fingerprint}:${currentGdfCount(stubsDir)}"

    private fun computeFileFingerprint(path: Path): Long {
        return try {
            val attrs = Files.readAttributes(path, BasicFileAttributes::class.java)
            (attrs.size() * 1_000_003L) xor attrs.lastModifiedTime().toMillis()
        } catch (_: IOException) {
            -1L
        }
    }

    private fun readStamp(stubsDir: Path): String? {
        val stampFile = stubsDir.resolve(STAMP_FILE_NAME)
        if (!stampFile.exists()) return null
        return runCatching { stampFile.readText().trim() }.getOrNull()
    }

    private fun writeStamp(stubsDir: Path, stamp: String) {
        runCatching {
            stubsDir.resolve(STAMP_FILE_NAME).writeText(stamp)
        }.onFailure { thisLogger().warn("Failed to write stubs stamp: ${it.message}") }
    }

    private fun currentGdfCount(stubsDir: Path): Int {
        if (!stubsDir.exists()) return 0
        return runCatching { stubsDir.listDirectoryEntries("*.gdf").size }.getOrDefault(0)
    }

    private suspend fun runDoctool(executable: Path, basePath: Path, stubsDir: Path, gdextensions: Boolean): Boolean {
        val args = mutableListOf(
            executable.pathString,
            "--path",
            basePath.pathString,
            "--headless",
            "--doctool",
            stubsDir.pathString,
        )
        if (gdextensions) args.add("--gdextension-docs")
        val commandLine = GeneralCommandLine(args).withWorkingDirectory(basePath)

        val timeoutMs = RegistryManager.getInstanceAsync().intValue(GENERATOR_TIMEOUT_REGISTRY_KEY)
        thisLogger().trace("Running --doctool: ${commandLine.commandLineString}")
        val handler = CapturingProcessHandler(commandLine)
        val output = try {
            runInterruptible(Dispatchers.IO) {
                handler.runProcess(timeoutMs)
            }
        }
        finally {
            if (handler.process.isAlive) {
                thisLogger().info("Destroying --doctool process")
                handler.destroyProcess()
            }
        }
        if (output.isTimeout) {
            thisLogger().warn("--doctool timed out after ${timeoutMs}ms")
            return false
        }
        if (output.exitCode != 0) {
            thisLogger().warn("--doctool exited with code ${output.exitCode}. stderr=${output.stderr}")
            return false
        }
        thisLogger().trace("--doctool finished; \n${output}")
        return true
    }

    private fun convertXmlToGdf(xmlDir: Path, stubsDir: Path) {
        val xmlFiles = runCatching { xmlDir.listDirectoryEntries("*.xml") }.getOrElse {
            thisLogger().warn("Failed to list XML files in $xmlDir: ${it.message}")
            return
        }
        thisLogger().trace("Converting ${xmlFiles.size} XML files from $xmlDir to .gdf in $stubsDir")
        val converter = XmlToGd()
        for (xmlFile in xmlFiles) {
            runCatching {
                val content = converter.convert(xmlFile)
                if (content.isNotEmpty()) {
                    val newName = GdNameSanitizer.sanitizeClassName(xmlFile.nameWithoutExtension)
                    val gdfFile = stubsDir.resolve("${newName}.gdf")
                    gdfFile.writeText(content)
                }
            }.onFailure {
                thisLogger().warn("Failed to convert ${xmlFile.fileName} to .gdf: ${it.message}")
            }
        }
    }

    companion object {
        private const val GENERATOR_TIMEOUT_REGISTRY_KEY = "gdscript.gdextension.api.dumper.timeout.ms"
        private const val STAMP_FILE_NAME = ".fingerprint"
        private const val GDSCRIPT_XML_URL =
            "https://raw.githubusercontent.com/godotengine/godot/master/modules/gdscript/doc_classes/@GDScript.xml"
    }
}
