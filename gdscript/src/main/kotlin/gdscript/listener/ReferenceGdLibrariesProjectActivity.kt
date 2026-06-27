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
import com.jetbrains.rider.godot.community.GdProjectGodotService
import gdscript.sdk.xml.XmlToGd
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.runInterruptible
import java.nio.file.Path
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

/*
    todo: RIDER-127007 Different approach to GD sdk
 */

@OptIn(FlowPreview::class, ExperimentalPathApi::class)
class ReferenceGdLibrariesProjectActivity : ProjectActivity {

    private data class GdExtInputs(val executable: Path, val basePath: Path, val snapshot: GdExtensionWatchService.Snapshot)

    override suspend fun execute(project: Project) {
        if (project.isDisposed) return
        val scope = GdScriptProjectLifetimeService.getInstance(project).scope
        scope.launch { provisionSdk(project) }
        scope.launch { provisionGdExtension(project) }
    }

    private suspend fun provisionSdk(project: Project) {
        GdProjectGodotService.getInstance(project).projectInfoFlow.filterNotNull().collect { info ->
            withBackgroundProgress(project, GdScriptBundle.message("progress.title.check.gdsdk.for.project")) {
                if (project.isDisposed) return@withBackgroundProgress
                val sdkPath = GdLibraryManager.extractSdkIfNeeded(info.parsedVersion)
                GdLibraryManager.registerSdkIfNeeded(sdkPath, project)
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

                val staleReason = stubsStaleReason(inputs.snapshot, stubsDir)
                if (staleReason == null) {
                    thisLogger().trace("Stubs up-to-date at $stubsDir; registering library")
                    GdLibraryManager.registerExtensionStubsIfNeeded(stubsDir, project)
                    return@collectLatest
                }
                thisLogger().info("Stubs missing or stale at $stubsDir ($staleReason); regenerating")
                withBackgroundProgress(project, GdScriptBundle.message("progress.title.generate.gdextension.stubs")) {
                    runCatching { stubsDir.deleteRecursively() }
                        .onFailure { thisLogger().warn("Failed to clean stubs dir at $stubsDir: ${it.message}") }
                    stubsDir.createDirectories()
                    if (!runDoctool(exe, basePath, stubsDir)) return@withBackgroundProgress
                    val docClassesDir = stubsDir.resolve("doc_classes")
                    convertXmlToGdf(docClassesDir, stubsDir)
                    val gdfCount = currentGdfCount(stubsDir)
                    if (gdfCount == 0) {
                        thisLogger().warn("--doctool finished but no stubs were produced at $stubsDir")
                        return@withBackgroundProgress
                    }
                    GdLibraryManager.registerExtensionStubsIfNeeded(stubsDir, project)
                    writeStamp(stubsDir, computeStamp(inputs.snapshot, stubsDir))
                    thisLogger().info("Generated $gdfCount .gdf files at $stubsDir; library registered")
                    VfsUtil.markDirtyAndRefresh(true, true, true, stubsDir)
                }
            }
    }

    private fun stubsStaleReason(snapshot: GdExtensionWatchService.Snapshot, stubsDir: Path): String? {
        if (!stubsDir.exists()) return "stubs dir missing"
        val actual = readStamp(stubsDir) ?: return ".fingerprint missing"
        val expected = computeStamp(snapshot, stubsDir)
        if (actual != expected) return "stamp mismatch: existing=$actual expected=$expected"
        return null
    }

    private fun computeStamp(snapshot: GdExtensionWatchService.Snapshot, stubsDir: Path): String =
        "${snapshot.gdextensions.size}:${snapshot.fingerprint}:${currentGdfCount(stubsDir)}"

    private fun readStamp(stubsDir: Path): String? {
        val stampFile = stubsDir.resolve(STAMP_FILE_NAME)
        if (!stampFile.exists()) return null
        return runCatching { stampFile.readText().trim() }.getOrNull()
    }

    private fun writeStamp(stubsDir: Path, stamp: String) {
        runCatching {
            stubsDir.resolve(STAMP_FILE_NAME).writeText(stamp)
        }.onFailure { thisLogger().warn("Failed to write GDExtension stubs stamp: ${it.message}") }
    }

    private fun currentGdfCount(stubsDir: Path): Int {
        if (!stubsDir.exists()) return 0
        return runCatching { stubsDir.listDirectoryEntries("*.gdf").size }.getOrDefault(0)
    }

    private suspend fun runDoctool(executable: Path, basePath: Path, stubsDir: Path): Boolean {
        val commandLine = GeneralCommandLine(
            executable.pathString,
            "--path",
            basePath.pathString,
            "--headless",
            "--doctool",
            stubsDir.pathString,
            "--gdextension-docs",
        ).withWorkingDirectory(basePath)

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
                    val gdfFile = stubsDir.resolve("${xmlFile.nameWithoutExtension}.gdf")
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
    }
}
