package gdscript.dap.remote

import com.intellij.platform.dap.DapInlineValueContext
import com.intellij.platform.dap.DapInlineValueLocator
import com.intellij.platform.dap.DapInlineValueLookup
import com.intellij.platform.dap.DapStackFrame
import com.intellij.xdebugger.XDebuggerUtil
import com.intellij.xdebugger.XSourcePosition

/**
 * Looks up both `name` and `self.name` through [context] and returns the later position.
 * Removes a leading `<file>.gd/` segment. Returns null if the name still contains `/`.
 */
internal fun createSelfPropertyInlineContext(context: DapInlineValueContext): DapInlineValueContext = DapInlineValueContext(
    object : DapInlineValueLookup {
        override fun findVariablePosition(variableName: String): XSourcePosition? {
            val name = if (variableName.substringBefore('/').endsWith(".gd")) variableName.substringAfter('/') else variableName
            if ('/' in name) return null
            // The lookup matches text only. A local with the same name can receive the property value.
            val plain = context.lookup.findVariablePosition(name)
            val qualified = context.lookup.findVariablePosition("self.$name")
            return if (qualified != null && (plain == null || qualified.offset > plain.offset)) qualified else plain
        }
    }
)

/**
 * Creates a lookup at the source line of [frame] through [locator].
 * Returns null when the locator, source position, or lookup is unavailable.
 */
internal suspend fun createInlineValueContext(frame: DapStackFrame, locator: DapInlineValueLocator?): DapInlineValueContext? {
    locator ?: return null
    val source = frame.source ?: return null
    val framePosition = XDebuggerUtil.getInstance().createPosition(source, frame.startPosition.line - 1) ?: return null
    val lookup = locator.createLookup(framePosition) ?: return null
    return DapInlineValueContext(lookup)
}
