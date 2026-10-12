package gdscript.dap.remote

import com.intellij.openapi.project.Project
import com.intellij.platform.dap.DapInlineValueLocator
import com.intellij.platform.dap.DapScope
import com.intellij.platform.dap.DapSessionExecutor
import com.intellij.platform.dap.DapStackFrame
import com.intellij.platform.dap.DapThread
import com.intellij.platform.dap.xdebugger.DapFrameSelection
import com.intellij.platform.dap.xdebugger.DapXSuspendContext
import com.intellij.platform.dap.xdebugger.DefaultDapXDebuggerPresentationFactory
import com.intellij.platform.dap.xdebugger.DefaultDapXSuspendContext
import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.frame.XStackFrame
import com.intellij.xdebugger.frame.XValueGroup

/** Supplies scope values with watch paths and Godot presentations. Adds the self check to stack frames. */
internal class GdDapPresentationFactory(
    private val project: Project,
    private val inlineValueLocator: DapInlineValueLocator?,
    private val watchStop: GdWatchStop? = null,
) : DefaultDapXDebuggerPresentationFactory(inlineValueLocator) {

    /** Each context has its own factory. Its frames and scopes share one watch stop binding. */
    override fun createSuspendContext(
        executor: DapSessionExecutor,
        threads: List<DapThread>,
        activeThread: DapThread?,
        frameSelection: DapFrameSelection?,
    ): DapXSuspendContext {
        lateinit var context: DapXSuspendContext
        val manager = XDebuggerManager.getInstance(project)
        val stop = GdWatchStop({ context }, manager::getDebugSessions)
        val factory = GdDapPresentationFactory(project, inlineValueLocator, stop)
        context = DefaultDapXSuspendContext(factory, executor, threads, activeThread, frameSelection)
        return context
    }

    override fun createScope(executor: DapSessionExecutor, scope: DapScope, index: Int): XValueGroup =
        GdDapXScope(this, executor, scope, index, inlineValueLocator = inlineValueLocator,
                    watchFrame = GdSelfCheckedFrame(scope.frame, { frameSourceSelf(project, scope.frame) }, watchStop = watchStop))

    override fun createStackFrame(
        executor: DapSessionExecutor,
        thread: DapThread,
        frame: DapStackFrame
    ): XStackFrame {
        val checkedFrame = GdSelfCheckedFrame(frame, { frameSourceSelf(project, frame) }, watchStop = watchStop)
        return GdDapStackFrame(this, executor, thread, checkedFrame, inlineValueLocator)
    }
}
