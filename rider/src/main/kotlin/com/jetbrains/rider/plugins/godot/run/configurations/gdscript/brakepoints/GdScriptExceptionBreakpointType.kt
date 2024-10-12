package com.jetbrains.rider.plugins.godot.run.configurations.gdscript.brakepoints

import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointType
import com.jetbrains.rider.plugins.godot.GodotPluginBundle

internal class GdScriptExceptionBreakpointType : XBreakpointType<XBreakpoint<GdScriptExceptionBreakpointProperties>, GdScriptExceptionBreakpointProperties>
                                                     ("GDSCRIPT_EXCEPTION_BP_TYPE",
                                                      GodotPluginBundle.message("gdscript.debug.exception.breakpoint.title")) {
    override fun getDisplayText(breakpoint: XBreakpoint<GdScriptExceptionBreakpointProperties>?): String {
        val defaultTitleMessage = GodotPluginBundle.message("gdscript.debug.exception.breakpoint.title")
        if (breakpoint == null) return defaultTitleMessage
        val properties:GdScriptExceptionBreakpointProperties = breakpoint.getProperties() ?: return defaultTitleMessage
        val exception = properties.exception ?: defaultTitleMessage
        return exception
    }

    override fun createDefaultBreakpoint(creator: XBreakpointCreator<GdScriptExceptionBreakpointProperties>): XBreakpoint<GdScriptExceptionBreakpointProperties> {
        return creator.createBreakpoint(createDefaultBreakpointProperties())
    }

    companion object {
        const val BASE_EXCEPTION = "GdScript Exception"
        private fun createDefaultBreakpointProperties(): GdScriptExceptionBreakpointProperties {
            return GdScriptExceptionBreakpointProperties(BASE_EXCEPTION)
        }
    }
}