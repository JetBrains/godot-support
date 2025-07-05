package com.jetbrains.rider.plugins.godot.run.configurations.gdscript.breakpoints

import com.intellij.icons.AllIcons
import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointType
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import javax.swing.Icon

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

    override fun getEnabledIcon(): Icon {
        return AllIcons.Debugger.Db_exception_breakpoint
    }

    override fun getDisabledIcon(): Icon {
        return AllIcons.Debugger.Db_disabled_exception_breakpoint
    }

    override fun getMutedEnabledIcon(): Icon {
        return AllIcons.Debugger.Db_exception_breakpoint
    }

    override fun getMutedDisabledIcon(): Icon {
        return AllIcons.Debugger.Db_exception_breakpoint
    }

    companion object {
        const val BASE_EXCEPTION = "GdScript Exception"
        private fun createDefaultBreakpointProperties(): GdScriptExceptionBreakpointProperties {
            return GdScriptExceptionBreakpointProperties(BASE_EXCEPTION)
        }
    }
}