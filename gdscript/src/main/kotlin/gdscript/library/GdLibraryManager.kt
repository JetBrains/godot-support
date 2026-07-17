package gdscript.library

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.process.OSProcessHandler
import com.intellij.execution.process.ProcessEvent
import com.intellij.execution.process.ProcessListener
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Version
import com.intellij.project.stateStore
import gdscript.polySymbols.scope.GdSdkSymbolsModificationTracker
import java.nio.file.Path
import kotlin.io.path.absolutePathString

object GdLibraryManager {

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
