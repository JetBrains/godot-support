package com.jetbrains.rider.plugins.godot.run.configurations.gdscript.brakepoints

import com.intellij.xdebugger.breakpoints.XBreakpoint
import com.intellij.xdebugger.breakpoints.XBreakpointProperties
import com.intellij.xdebugger.breakpoints.XBreakpointType
import com.jetbrains.rider.plugins.godot.GodotPluginBundle

internal class GdScriptExceptionBreakpointType : XBreakpointType<XBreakpoint<XBreakpointProperties<Any>>, XBreakpointProperties<Any>>("GDSCRIPT_EXCEPTION_BP_TYPE", GodotPluginBundle.message("gdscript.debug.exception.breakpoint.title")) {
    override fun getDisplayText(breakpoint: XBreakpoint<XBreakpointProperties<Any>>?): String = GodotPluginBundle.message("gdscript.debug.exception.breakpoint.title")
}