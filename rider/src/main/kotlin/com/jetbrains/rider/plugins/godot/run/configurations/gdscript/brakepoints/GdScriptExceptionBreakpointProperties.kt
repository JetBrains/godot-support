package com.jetbrains.rider.plugins.godot.run.configurations.gdscript.brakepoints

import com.intellij.util.xmlb.annotations.Attribute
import com.intellij.xdebugger.breakpoints.XBreakpointProperties

class GdScriptExceptionBreakpointProperties() : XBreakpointProperties<GdScriptExceptionBreakpointProperties>() {
    constructor(exception: String) : this() {
        this.exception = exception
    }

    @Attribute("exception")
    var exception: String? = null

    override fun getState(): GdScriptExceptionBreakpointProperties {
        return this
    }

    override fun loadState(state: GdScriptExceptionBreakpointProperties) {
        exception = state.exception
    }
}
