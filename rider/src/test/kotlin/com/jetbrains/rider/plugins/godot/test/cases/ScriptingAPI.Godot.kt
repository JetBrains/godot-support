package com.jetbrains.rider.plugins.godot.test.cases

import com.intellij.execution.RunManager
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil.*
import com.jetbrains.rdclient.util.idea.waitAndPump
import com.jetbrains.rider.plugins.godot.run.GodotRunConfigurationGenerator
import com.jetbrains.rider.test.asserts.shouldBeTrue
import com.jetbrains.rider.test.asserts.shouldNotBeNull
import com.jetbrains.rider.test.framework.*
import com.jetbrains.rider.test.scriptingApi.DebugTestExecutionContext
import com.jetbrains.rider.test.scriptingApi.debugProgram
import com.jetbrains.rider.test.scriptingApi.waitForDotNetDebuggerInitializedOrCanceled
import com.jetbrains.rider.utils.NullPrintStream
import java.io.File
import java.time.Duration

// region Constants
const val godotNumberVersion = "4.4.1"
val godotDefaultTimeout: Duration = Duration.ofSeconds(60)
// endregion

// region Download Godot
fun downloadAndExtractGodot(version: String): File {
    val godotZipName = when {
        SystemInfo.isWindows -> "Godot_v${version}-stable_mono_win64.zip"
        SystemInfo.isMac -> "Godot_v${version}-stable_mono_macos.universal.zip"
        SystemInfo.isLinux -> "Godot_v${version}-stable_mono_linux_x86_64.zip"
        else -> error("Unsupported OS for Godot Mono")
    }

    val extractedDir = downloadAndExtractTestToolArchiveArtifactIntoPersistentCache("$TEST_DATA_DOWNLOAD_URL/$godotZipName").canonicalFile

    val godotExecutable = extractedDir.resolve(when {
                                                   SystemInfo.isWindows -> "Godot_v${version}-stable_mono_win64.exe"
                                                   SystemInfo.isLinux -> "Godot_v${version}-stable_mono_linux_x86_64"
                                                   SystemInfo.isMac -> "Godot_mono.app/Contents/MacOS/Godot"
                                                   else -> error("Unsupported OS for Godot Mono")
                                               }).apply { setExecutable(true) }
    if (SystemInfo.isLinux) {
        Runtime.getRuntime().exec(arrayOf("chmod", "+x", godotExecutable.absolutePath)).waitFor()
    }
    if (!godotExecutable.exists()) {
        error("Godot Mono executable not found at ${godotExecutable.absolutePath}")
    }
    frameworkLogger.info("Godot Mono downloaded and extracted: ${godotExecutable.absolutePath}")
    return godotExecutable
}
// endregion

// region Godot Project Setup
fun putGodotProjectToTempTestDir(
    projectName: String,
    testWorkDirectory: File,
    solutionSourceRootDirectory: File,
): File {
    val workDirectory = File(testWorkDirectory, projectName)
    val sourceDirectory = File(solutionSourceRootDirectory, projectName)
    copyDir(sourceDirectory, workDirectory)
    workDirectory.isDirectory.shouldBeTrue("Expected '${workDirectory.absolutePath}' to be a directory")
    return workDirectory
}
// endregion

// region Godot Execution
fun startGodot(godotExecutable: File, projectPath: String, logPath: File): Process {
    val logFile = File(logPath.toString(), "Godot_${System.currentTimeMillis()}.log")

    val command = mutableListOf(
        godotExecutable.absolutePath,
        "--verbose",
        "--headless",
        "--editor",
        "--path", projectPath, // Starts the editor, waits for any resources to be imported, and then quits.
    )
    val process = ProcessBuilder(command)
        .directory(File(projectPath))
        .redirectErrorStream(true)
        .redirectOutput(logFile)
        .start()

    frameworkLogger.info("Godot process started (pid=${process.pid()})")
    return process
}

fun startGodotWithProject(
    godotVersion: String = godotNumberVersion,
    projectName: String,
    testWorkDirectory: File,
    solutionSourceRootDirectory: File,
    logPath: File
): Process {
    val godotExecutable = downloadAndExtractGodot(godotVersion)
    val projectDir = putGodotProjectToTempTestDir(projectName, testWorkDirectory, solutionSourceRootDirectory)
    val process = startGodot(godotExecutable, projectDir.absolutePath, logPath)
    waitForGodotFolderWithFiles(projectDir)
    return process
}

fun waitForGodotFolderWithFiles(
    projectDir: File,
    folderName: String = ".godot",
    minFileCount: Int = 3
) {
    val targetFolder = File(projectDir, folderName)
    while (!targetFolder.exists() || (targetFolder.listFiles()?.size ?: 0) < minFileCount) {
        frameworkLogger.info("Waiting files in .godot folder")
        Thread.sleep(500)
    }
}

fun stopGodotProcess(process: Process?) {
    if (process!!.isAlive) {
        try {
            process.destroy()
            if (!process.waitFor(10, java.util.concurrent.TimeUnit.SECONDS)) {
                process.destroyForcibly()
            }
        } catch (e: Exception) {
            if (process.isAlive) process.destroyForcibly()
        }
    }
}

fun waitForGodotRunConfigurations(project: Project) {
    val runManager = RunManager.getInstance(project)
    waitAndPump(godotDefaultTimeout, { runManager.allConfigurationsList.size >= 2 }) {
        "Godot run configurations didn't appeared, " +
        "current: ${runManager.allConfigurationsList.joinToString(", ", "[", "]")}"
    }
}

fun attachDebuggerToGodotEditor(
    project: Project,
    beforeRun: ExecutionEnvironment.() -> Unit = {},
    test: DebugTestExecutionContext.() -> Unit,
    goldFile: File? = null,
    customSuffixes: List<String> = emptyList()
) {
    waitForGodotRunConfigurations(project)
    val runConfigName = GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME
    selectRunConfiguration(project, runConfigName)

    val waitAndTest: DebugTestExecutionContext.() -> Unit = {
        waitForDotNetDebuggerInitializedOrCanceled()
        test()
    }

    if (goldFile != null) {
        executeWithGold(goldFile, customSuffixes) {
            debugProgram(project, it, beforeRun, waitAndTest, {}, true)
        }
    } else {
        debugProgram(project, NullPrintStream, beforeRun, waitAndTest, {}, true)
    }
}

private fun selectRunConfiguration(project: Project, name: String) {
    val runManager = RunManager.getInstance(project)
    val runConfigurationToSelect = runManager.allConfigurationsList.firstOrNull {
        it.name == name
    }.shouldNotBeNull("There are no run configuration with name '$name', " +
                      "current: ${runManager.allConfigurationsList.joinToString(", ", "[", "]")}")

    frameworkLogger.info("Selecting run configuration '$name'")
    runManager.selectedConfiguration = runManager.findSettings(runConfigurationToSelect)
}
// endregion
