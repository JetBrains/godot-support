package gdscript.dap.remote

import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.frame.XSuspendContext

/** Binds dictionary reads to the suspend context that created their rows. */
internal class GdWatchStop(
    private val context: () -> XSuspendContext,
    private val sessions: () -> Array<XDebugSession>,
) {
    /**
     * Checks the session list and the suspend context identity.
     * The platform uses a synchronized map, a state flow, and an atomic reference for these getters.
     * This check can run on any thread. It needs no EDT dispatch.
     */
    fun isCurrent(): Boolean {
        val expected = context()
        return sessions().any { it.isSuspended && it.suspendContext === expected }
    }
}
