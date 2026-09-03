package com.jetbrains.godot.test

import com.intellij.util.system.LowLevelLocalMachineAccess
import com.intellij.util.system.OS
import com.jetbrains.rider.test.facades.environment.RiderTestExecutionTarget
import com.jetbrains.rider.test.framework.TEST_DATA_DOWNLOAD_URL
import com.jetbrains.rider.test.framework.downloadAndExtractTestToolArchiveArtifactIntoPersistentCache
import com.jetbrains.rider.test.framework.frameworkLogger
import com.jetbrains.rider.test.scriptingApi.setExecutablePermissions
import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists

const val GODOT_NUMBER_VERSION: String = "4.6.2"
const val BASIC_PROJECT_GODOT: String = """
      config_version=5
      [application]
      config/name="Tmp"
      """
const val EDITOR_LOADED_FLAG_4_5: String = "\u001B[92m[ DONE ]\u001B[39m \u001B[1mloading_editor_layout\u001B[22m"
const val EDITOR_LOADED_FLAG_PRE_4_5: String = "loading_editor_layout: end"


@OptIn(LowLevelLocalMachineAccess::class)
fun downloadAndExtractGodot(version: String = GODOT_NUMBER_VERSION, isMono: Boolean = true): Path {
    val godotZipName =
        if (isMono) {
            when (OS.CURRENT) {
                OS.Windows -> "Godot_v${version}-stable_mono_win64.zip"
                OS.macOS -> "Godot_v${version}-stable_mono_macos.universal.zip"
                OS.Linux -> "Godot_v${version}-stable_mono_linux_x86_64.zip"
                else -> error("Unsupported OS for Godot Mono")
            }
        } else {
            when (OS.CURRENT) {
                OS.Windows -> "Godot_v${version}-stable_win64.exe.zip"
                OS.macOS -> "Godot_v${version}-stable_macos.universal.zip"
                OS.Linux -> "Godot_v${version}-stable_linux.x86_64.zip"
                else -> error("Unsupported OS for Godot Mono")
            }
        }

    val extractedDir = downloadAndExtractTestToolArchiveArtifactIntoPersistentCache(
        RiderTestExecutionTarget.fromCurrentMachine(),
        "$TEST_DATA_DOWNLOAD_URL/$godotZipName"
    )
        .toAbsolutePath().normalize()

    val godotExecutable = extractedDir.resolve(
        if (isMono) {
            when (OS.CURRENT) {
                OS.Windows -> {
                    val base = "Godot_v${version}-stable_mono_win64"
                    "$base/$base.exe"
                }

                OS.Linux -> "Godot_v${version}-stable_mono_linux_x86_64/Godot_v${version}-stable_mono_linux.x86_64"
                OS.macOS -> "Godot_mono.app/Contents/MacOS/Godot"
                else -> error("Unsupported OS for Godot")
            }
        } else {
            when (OS.CURRENT) {
                OS.Windows -> {
                    "Godot_v${version}-stable_win64.exe"
                }

                OS.Linux -> "Godot_v${version}-stable_linux.x86_64"
                OS.macOS -> "Godot.app/Contents/MacOS/Godot"
                else -> error("Unsupported OS for Godot")
            }
        }
    ).apply { setExecutablePermissions() }

    if (!godotExecutable.exists()) {
        error("Godot executable not found at ${godotExecutable.absolutePathString()}")
    }
    frameworkLogger.info("Godot downloaded and extracted: ${godotExecutable.absolutePathString()}")
    return godotExecutable
}
