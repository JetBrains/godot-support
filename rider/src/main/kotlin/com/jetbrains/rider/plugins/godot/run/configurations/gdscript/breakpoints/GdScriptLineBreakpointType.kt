package com.jetbrains.rider.plugins.godot.run.configurations.gdscript.breakpoints

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.xdebugger.breakpoints.XLineBreakpointTypeBase
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.plugins.godot.Util

internal class GdScriptLineBreakpointType :
  XLineBreakpointTypeBase(
      "GdScriptLineBreakpointTypeId",
      GodotPluginBundle.message("gdscript.debug.line.breakpoint.title"),
      GdScriptDebuggerEditorsProvider()
  ) {
    override fun canPutAt(file: VirtualFile, line: Int, project: Project): Boolean {
        if (Util.isGdFile(file))
            return true
        return super.canPutAt(file, line, project)
    }
  }