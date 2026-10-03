package gdscript.dap.remote

import com.intellij.openapi.project.Project
import com.intellij.platform.dap.DapInlineValueLocator
import com.intellij.platform.dap.DapScope
import com.intellij.platform.dap.DapSessionExecutor
import com.intellij.platform.dap.DapStackFrame
import com.intellij.platform.dap.DapThread
import com.intellij.platform.dap.xdebugger.DefaultDapXDebuggerPresentationFactory
import com.intellij.xdebugger.frame.XStackFrame
import com.intellij.xdebugger.frame.XValueGroup

/** Supplies completion for scope groups and the self check for stack frames. Plain scopes keep the platform presentation. */
internal class GdDapPresentationFactory(
    private val project: Project,
    private val inlineValueLocator: DapInlineValueLocator?,
) : DefaultDapXDebuggerPresentationFactory(inlineValueLocator) {

    override fun createScope(executor: DapSessionExecutor, scope: DapScope, index: Int): XValueGroup =
        GdDapXScope(this, executor, scope, index, inlineValueLocator = inlineValueLocator)

    override fun createStackFrame(
        executor: DapSessionExecutor,
        thread: DapThread,
        frame: DapStackFrame
    ): XStackFrame {
        val checkedFrame = GdSelfCheckedFrame(frame, { frameSourceSelf(project, frame) })
        return GdDapStackFrame(this, executor, thread, checkedFrame, inlineValueLocator)
    }
}
