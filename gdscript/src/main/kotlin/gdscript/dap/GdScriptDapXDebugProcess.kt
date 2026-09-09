@file:Suppress("UnstableApiUsage")

package gdscript.dap

import com.intellij.execution.ExecutionResult
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.platform.dap.DapDebugSession
import com.intellij.platform.dap.DapStartRequest
import com.intellij.platform.dap.DebugAdapterDescriptor
import com.intellij.platform.dap.xdebugger.DapXDebugProcess
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.evaluation.XDebuggerEditorsProvider
import gdscript.dap.breakpoints.GdScriptDebuggerEditorsProvider
import kotlinx.coroutines.CoroutineScope

class GdScriptDapXDebugProcess(
    session: XDebugSession,
    dapDebugSession: DapDebugSession,
    xDebugProcessScope: CoroutineScope,
    globalScope: CoroutineScope,
    debugAdapterDescriptor: DebugAdapterDescriptor<*>,
    executionEnvironment: ExecutionEnvironment,
    executionResult: ExecutionResult?,
    startRequestType: DapStartRequest,
    startRequestArguments: Map<String, Any?>,
) : DapXDebugProcess(
    session = session,
    dapDebugSession = dapDebugSession,
    xDebugProcessScope = xDebugProcessScope,
    globalScope = globalScope,
    debugAdapterDescriptor = debugAdapterDescriptor,
    executionEnvironment = executionEnvironment,
    executionResult = executionResult,
    startRequestType = startRequestType,
    startRequestArguments = startRequestArguments,
) {
    private val editorsProvider = GdScriptDebuggerEditorsProvider()

    override fun getEditorsProvider(): XDebuggerEditorsProvider = editorsProvider
}
