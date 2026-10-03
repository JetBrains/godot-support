@file:Suppress("UnstableApiUsage")

package gdscript.dap

import com.intellij.execution.ExecutionResult
import com.intellij.execution.impl.EditConfigurationsDialog
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.application.EDT
import com.intellij.openapi.project.Project
import com.intellij.platform.dap.DapBreakpointsDescription
import com.intellij.platform.dap.DapCustomization
import com.intellij.platform.dap.DapDebugSession
import com.intellij.platform.dap.DapExceptionBreakpoint
import com.intellij.platform.dap.DapExceptionInfo
import com.intellij.platform.dap.DapExpressionSupport
import com.intellij.platform.dap.DapInlineValueLocator
import com.intellij.platform.dap.DapPresentationSupport
import com.intellij.platform.dap.DebugAdapterDescriptor
import com.intellij.platform.dap.DebugAdapterId
import com.intellij.platform.dap.DebugAdapterSupportProvider
import com.intellij.platform.dap.connection.DebugAdapterHandle
import com.intellij.platform.dap.connection.DebugAdapterSocketConnection
import com.intellij.platform.dap.xdebugger.DapXDebuggerPresentationFactory
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XSourcePosition
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider
import com.intellij.xdebugger.evaluation.XDebuggerEvaluator
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil
import gdscript.GdScriptBundle
import gdscript.dap.breakpoints.GdScriptDebuggerEditorsProvider
import gdscript.dap.breakpoints.GdScriptExceptionBreakpointType
import gdscript.dap.breakpoints.GdScriptLineBreakpointType
import gdscript.dap.remote.GdDapPresentationFactory
import gdscript.lsp.GodotLspRunningStatusProvider
import gdscript.lsp.RunningGodotEditorDiscovery
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object GdScriptDebugAdapter : DebugAdapterId("gdscript", GdScriptBundle.message("gdscript.debug.adapter.presentable.name"))

internal class GdScriptDebugAdapterSupportProvider : DebugAdapterSupportProvider<GdScriptDebugAdapter> {
    override val adapterId = GdScriptDebugAdapter
    override fun createDebugAdapterDescriptor(project: Project): DebugAdapterDescriptor<GdScriptDebugAdapter> =
        object : DebugAdapterDescriptor<GdScriptDebugAdapter>() {
            override val id = GdScriptDebugAdapter

            override val customization: DapCustomization = object : DapCustomization() {
                override val expressionSupport = object : DapExpressionSupport() {
                    override fun createEditorsProvider(session: XDebugSession): XDebuggerEditorsProvider = GdScriptDebuggerEditorsProvider()

                    override fun createEvaluator(
                        dapSession: DapDebugSession,
                        presentationFactory: DapXDebuggerPresentationFactory,
                    ): XDebuggerEvaluator = GdUnavailableEvaluator()
                }

                override val presentationSupport = object : DapPresentationSupport() {
                    override fun createPresentationFactory(inlineValueLocator: DapInlineValueLocator?): DapXDebuggerPresentationFactory =
                        GdDapPresentationFactory(project, inlineValueLocator)
                }
            }

            override suspend fun launchDebugAdapter(
                environment: ExecutionEnvironment,
                executionResult: ExecutionResult?,
                sessionId: String,
            ): DebugAdapterHandle {
                val config = environment.runnerAndConfigurationSettings!!.configuration as GdScriptRunConfiguration
                // If a Godot editor for this project is already running with `--dap-port`, prefer it
                val port = discoverRunningDapPort(project) ?: config.structured.debugServerPort
                try {
                    return DebugAdapterSocketConnection(
                        host = GdScriptRunFactory.DEFAULT_ADDRESS,
                        port = port, connectionAttempts = 1)
                }
                catch (e: CancellationException) {
                    throw e
                }
                catch (_: Exception) {
                    // handling of cases, if Editor is not running or the port is not matching
                    // let user fix the port or start the editor and then, we will try to connect again
                    withContext(Dispatchers.EDT) {
                        EditConfigurationsDialog(project).show()
                    }
                    
                    return DebugAdapterSocketConnection(
                        host = GdScriptRunFactory.DEFAULT_ADDRESS,
                        port = port,
                        connectionAttempts = 2)
                }
                finally {
                    // LSP is required for the hotreload, so we better additionally check that it is running
                    GodotLspRunningStatusProvider.ensureLspRunning(project)
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

/** Evaluation without a selected frame cannot check the source or the dump. */
internal class GdUnavailableEvaluator : XDebuggerEvaluator() {
    override fun evaluate(expression: String, callback: XEvaluationCallback, expressionPosition: XSourcePosition?) {
        callback.errorOccurred(GdScriptBundle.message("gdscript.debugger.error.evaluation.frame.unavailable"))
    }
}

private suspend fun discoverRunningDapPort(project: Project): Int? {
    val basePath = GodotCommunityUtil.getGodotProjectBasePath(project) ?: return null
    return RunningGodotEditorDiscovery.findRunningGodotDapPort(basePath)
}
