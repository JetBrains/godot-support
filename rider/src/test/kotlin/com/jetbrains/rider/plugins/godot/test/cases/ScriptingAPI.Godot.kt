package com.jetbrains.rider.plugins.godot.test.cases

import com.intellij.execution.RunManager
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil.copyDir
import com.jetbrains.rd.util.lifetime.LifetimeDefinition
import com.jetbrains.rdclient.util.idea.waitAndPump
import com.jetbrains.rider.debugger.settings.DotNetDebuggerSettings
import com.jetbrains.rider.plugins.godot.run.GodotRunConfigurationGenerator
import com.jetbrains.rider.run.configurations.dotNetExe.DotNetExeConfiguration
import com.jetbrains.rider.test.asserts.shouldBeTrue
import com.jetbrains.rider.test.asserts.shouldNotBeNull
import com.jetbrains.rider.test.facades.environment.RiderTestExecutionTarget
import com.jetbrains.rider.test.framework.TEST_DATA_DOWNLOAD_URL
import com.jetbrains.rider.test.framework.downloadAndExtractTestToolArchiveArtifactIntoPersistentCache
import com.jetbrains.rider.test.framework.executeWithGold
import com.jetbrains.rider.test.framework.frameworkLogger
import com.jetbrains.rider.test.scriptingApi.DebugTestExecutionContext
import com.jetbrains.rider.test.scriptingApi.debugProgram
import com.jetbrains.rider.test.scriptingApi.setExecutablePermissions
import com.jetbrains.rider.test.scriptingApi.waitForDotNetDebuggerInitializedOrCanceled
import com.jetbrains.rider.utils.NullPrintStream
import java.io.File
import java.nio.file.Path
import java.time.Duration
import java.util.concurrent.TimeUnit
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists

// region Constants
const val godotNumberVersion = "4.6.2"
val godotDefaultTimeout: Duration = Duration.ofSeconds(60)
// endregion

// region Download Godot
fun downloadAndExtractGodot(version: String): Path {
    val godotZipName = when {
        SystemInfo.isWindows -> "Godot_v${version}-stable_mono_win64.zip"
        SystemInfo.isMac -> "Godot_v${version}-stable_mono_macos.universal.zip"
        SystemInfo.isLinux -> "Godot_v${version}-stable_mono_linux_x86_64.zip"
        else -> error("Unsupported OS for Godot Mono")
    }

    val extractedDir = downloadAndExtractTestToolArchiveArtifactIntoPersistentCache(
        RiderTestExecutionTarget.fromCurrentMachine(),
        "$TEST_DATA_DOWNLOAD_URL/$godotZipName")
        .toAbsolutePath().normalize()

    val godotExecutable = extractedDir.resolve(when {
        SystemInfo.isWindows -> {
            val base = "Godot_v${version}-stable_mono_win64"
            "$base/$base.exe"
        }
        SystemInfo.isLinux -> "Godot_v${version}-stable_mono_linux_x86_64"
        SystemInfo.isMac -> "Godot_mono.app/Contents/MacOS/Godot"
        else -> error("Unsupported OS for Godot")
    }).apply { setExecutablePermissions() }

    if (!godotExecutable.exists()) {
        error("Godot executable not found at ${godotExecutable.absolutePathString()}")
    }
    frameworkLogger.info("Godot downloaded and extracted: ${godotExecutable.absolutePathString()}")
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

fun startGodot(godotExecutable: Path, projectPath: String, logPath: Path, dotnetSdk: String, timeoutMinutes: Long = 3): Process {
    val logFile = File(logPath.toString(), "Godot_${System.currentTimeMillis()}.log")
    
    val command = mutableListOf(
        godotExecutable.absolutePathString(),
        "--verbose",
        "--headless",
        "--editor",
        "--quit",
        "--path", projectPath,
    )

    val processBuilder = ProcessBuilder(command)
        .directory(File(projectPath))
        .redirectErrorStream(true)
        .redirectOutput(logFile)
    processBuilder.environment()["DOTNET_ROOT"] = dotnetSdk
    processBuilder.environment()["PATH"] = "$dotnetSdk:${processBuilder.environment()["PATH"]}"
    processBuilder.environment()["DOTNET_MSBUILD_SDK_RESOLVER_SDKS_DIR"] = "$dotnetSdk/sdk"
    processBuilder.environment()["DOTNET_MSBUILD_SDK_RESOLVER_SDKS_VER"] =
        File("$dotnetSdk/sdk")
            .listFiles()
            ?.map { it.name }
            ?.sortedDescending()
            ?.firstOrNull() ?: ""

    val process = processBuilder.start()
    frameworkLogger.info("Godot process started (pid=${process.pid()})")

    val exitCode = process.waitFor(timeoutMinutes, TimeUnit.MINUTES)
    frameworkLogger.info("Godot process finished, exit code = $exitCode")
    return process
}

fun startGodotWithProject(
    godotVersion: String = godotNumberVersion,
    projectName: String,
    testWorkDirectory: File,
    solutionSourceRootDirectory: File,
    logPath: Path,
    dotnetSdk: String,
): Process {
    val godotExecutable = downloadAndExtractGodot(godotVersion)
    val projectDir = putGodotProjectToTempTestDir(projectName, testWorkDirectory, solutionSourceRootDirectory)
    frameworkLogger.info("Starting Godot with project: ${projectDir.absolutePath}")
    val process = startGodot(godotExecutable, projectDir.absolutePath, logPath, dotnetSdk)
    return process
}

fun waitForGodotRunConfigurations(project: Project) {
    val runManager = RunManager.getInstance(project)
    waitAndPump(godotDefaultTimeout, { runManager.allConfigurationsList.size >= 2 }) {
        "Godot run configurations didn't appeared, " +
        "current: ${runManager.allConfigurationsList.joinToString(", ", "[", "]")}"
    }
}

fun DebugGodotPlayer(
    project: Project,
    beforeRun: ExecutionEnvironment.() -> Unit = {},
    test: DebugTestExecutionContext.() -> Unit,
    goldFile: Path? = null,
    customSuffixes: List<String> = emptyList()
) {
    waitForGodotRunConfigurations(project)
    val runConfigName = GodotRunConfigurationGenerator.PLAYER_CONFIGURATION_NAME
    frameworkLogger.info("Starting Godot player with configuration: $runConfigName")
    val config = selectRunConfiguration(project, runConfigName) as? DotNetExeConfiguration
    val originalParams = config?.parameters?.programParameters
    config?.parameters?.programParameters = "${originalParams ?: ""} --headless"

    val waitAndTest: DebugTestExecutionContext.() -> Unit = {
        waitForDotNetDebuggerInitializedOrCanceled()
        test()
    }

    try {
        if (goldFile != null) {
            executeWithGold(goldFile, customSuffixes) {
                debugProgram(project, it, beforeRun, waitAndTest, {}, true)
            }
        } else {
            debugProgram(project, NullPrintStream, beforeRun, waitAndTest, {}, true)
        }
    } finally {
        frameworkLogger.info("Restoring Player run config programParameters to: '$originalParams'")
        config?.parameters?.programParameters = originalParams ?: ""
    }
}

private fun selectRunConfiguration(project: Project, name: String): RunConfiguration {
    val runManager = RunManager.getInstance(project)
    val runConfigurationToSelect = runManager.allConfigurationsList.firstOrNull {
        it.name == name
    }.shouldNotBeNull(
        "There are no run configuration with name '$name', " +
        "current: ${runManager.allConfigurationsList.joinToString(", ", "[", "]")}"
    )

    frameworkLogger.info("Selecting run configuration '$name'")
    runManager.selectedConfiguration = runManager.findSettings(runConfigurationToSelect)
    return runConfigurationToSelect
}

fun disableDFA(lifetime: LifetimeDefinition) {
    val previousValue = DotNetDebuggerSettings.instance.debuggerDFAEnable
    lifetime.bracketIfAlive(
        { DotNetDebuggerSettings.instance.debuggerDFAEnable = false },
        { DotNetDebuggerSettings.instance.debuggerDFAEnable = previousValue })
}
// endregion

// region additional ctart of Godot in case if we want to test only attach to Godot Game
fun startGodotLikeDebugRun(
    godotExecutable: File,
    projectPath: String,
    logPath: Path,
    dotnetSdk: String
): Process {
    val logFile = File(logPath.toFile(), "Godot_debug_like_${System.currentTimeMillis()}.log")

    val command = mutableListOf(
        godotExecutable.absolutePath,
        "--path", "./",
        "--verbose",
        "--debug"
    )

    val processBuilder = ProcessBuilder(command)
        .directory(File(projectPath))
        .redirectErrorStream(true)
        .redirectOutput(logFile)

    val env = processBuilder.environment()

    env["DOTNET_ROOT"] = dotnetSdk
    env["PATH"] = "$dotnetSdk:${env["PATH"]}"
    env["DOTNET_MSBUILD_SDK_RESOLVER_SDKS_DIR"] = "$dotnetSdk/sdk"
    env["DOTNET_MSBUILD_SDK_RESOLVER_SDKS_VER"] =
        File("$dotnetSdk/sdk")
            .listFiles()
            ?.map { it.name }
            ?.sortedDescending()
            ?.firstOrNull() ?: ""
    env["DOTNET_MULTILEVEL_LOOKUP"] = "0"

    frameworkLogger.info("Starting Godot (debug-like) with command: ${command.joinToString(" ")}")
    val process = processBuilder.start()
    frameworkLogger.info("Godot (debug-like) started (pid=${process.pid()})")

    frameworkLogger.info("Check process is alive:${process.isAlive}")
    val exit = process.waitFor(5, TimeUnit.SECONDS)
    frameworkLogger.info("Exited? $exit")

    process.waitFor(20, TimeUnit.SECONDS)
    if (process.isAlive) {
        frameworkLogger.info("Killing Godot process (pid=${process.pid()}) after timeout")
        Thread.sleep(20000)
        process.destroy()
        process.waitFor(5, TimeUnit.SECONDS)
    }
    return process
}
// endregion