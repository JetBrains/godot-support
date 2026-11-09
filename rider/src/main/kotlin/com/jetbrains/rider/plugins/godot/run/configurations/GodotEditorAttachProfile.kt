package com.jetbrains.rider.plugins.godot.run.configurations

import com.intellij.execution.Executor
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.process.ProcessInfo
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.icons.AllIcons
import com.jetbrains.rider.debugger.IRiderDebuggable
import com.jetbrains.rider.model.debuggerHelper.PlatformArchitecture
import com.jetbrains.rider.run.IDotNetRunProfile
import com.jetbrains.rider.utils.parseExecutablePathFromCommandLine
import javax.swing.Icon
import kotlin.io.path.nameWithoutExtension

class GodotEditorAttachProfile(
    private val processInfo: ProcessInfo,
    private val targetPlatform: PlatformArchitecture,
    private val resumeGame: suspend () -> Unit,
    private val terminateCleanup: () -> Unit,
) : IDotNetRunProfile, IRiderDebuggable {

    private val executableName = parseExecutablePathFromCommandLine(processInfo.commandLine)?.nameWithoutExtension
                                 ?: processInfo.executableDisplayName

    override fun getState(executor: Executor, environment: ExecutionEnvironment): RunProfileState =
        GodotInEditorAttachProfileState(processInfo, environment, targetPlatform, resumeGame, terminateCleanup)

    override fun getName(): String = "${processInfo.pid}:$executableName"

    override fun getIcon(): Icon = AllIcons.Actions.StartDebugger
}
