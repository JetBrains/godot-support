package com.jetbrains.rider.plugins.godot.run.configurations.gdscript

import com.intellij.execution.ExecutionResult
import com.intellij.execution.impl.EditConfigurationsDialog
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.application.EDT
import com.intellij.openapi.project.Project
import com.intellij.platform.dap.DapBreakpointsDescription
import com.intellij.platform.dap.DapExceptionBreakpoint
import com.intellij.platform.dap.DapExceptionInfo
import com.intellij.platform.dap.DebugAdapterDescriptor
import com.intellij.platform.dap.DebugAdapterId
import com.intellij.platform.dap.DebugAdapterSupportProvider
import com.intellij.platform.dap.connection.DebugAdapterHandle
import com.intellij.platform.dap.connection.DebugAdapterSocketConnection
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.plugins.godot.lang.service.GodotLspProjectService
import com.jetbrains.rider.plugins.godot.lang.service.isGodotLspRunning
import com.jetbrains.rider.plugins.godot.run.configurations.gdscript.breakpoints.GdScriptExceptionBreakpointType
import com.jetbrains.rider.plugins.godot.run.configurations.gdscript.breakpoints.GdScriptLineBreakpointType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GdScriptDebugAdapter : DebugAdapterId("gdscript", GodotPluginBundle.message("gdscript.debug.adapter.presentable.name"))

private class GdScriptDebugAdapterSupportProvider : DebugAdapterSupportProvider<GdScriptDebugAdapter> {
    override val adapterId = GdScriptDebugAdapter
    override fun createDebugAdapterDescriptor(project: Project): DebugAdapterDescriptor<GdScriptDebugAdapter> =
        object : DebugAdapterDescriptor<GdScriptDebugAdapter>() {
            override val id = GdScriptDebugAdapter

            override suspend fun launchDebugAdapter(
                environment: ExecutionEnvironment,
                executionResult: ExecutionResult?,
                sessionId: String,
            ): DebugAdapterHandle {
                val config = environment.runnerAndConfigurationSettings!!.configuration as GdScriptRunConfiguration
                try {
                    return DebugAdapterSocketConnection(
                        host = GdScriptRunFactory.DEFAULT_ADDRESS,
                        port = config.structured.debugServerPort, connectionAttempts = 1)
                }
                catch (e: Exception) {
                    // handling of cases, if Editor is not running or the port is not matching
                    // let user fix the port or start the editor and then, we will try to connect again
                    withContext(Dispatchers.EDT) {
                        EditConfigurationsDialog(project).show()
                    }

                    return DebugAdapterSocketConnection(
                        host = GdScriptRunFactory.DEFAULT_ADDRESS,
                        port = config.structured.debugServerPort,
                        connectionAttempts = 2)
                }
                finally {
                    // LSP is required for the hotreload, so we better additionally check that it is running
                    if (!isGodotLspRunning(project)) {
                        GodotLspProjectService.getInstance(project).restartServer()
                    }
                }
            }

            override val breakpointsDescription: DapBreakpointsDescription = object : DapBreakpointsDescription(
                sourceBreakpointType = GdScriptLineBreakpointType::class.java,
                exceptionBreakpointType = GdScriptExceptionBreakpointType::class.java
            ) {
                override fun doesExceptionMatchBreakpoint(exceptionInfo: DapExceptionInfo, breakpoint: DapExceptionBreakpoint): Boolean {
                    val ideBreakpoint = breakpoint.ideBreakpoint
                    return ideBreakpoint.type is GdScriptExceptionBreakpointType
                }
            }
        }
}