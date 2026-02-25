package gdscript.dap.breakpoints

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.xdebugger.breakpoints.XLineBreakpointTypeBase
import gdscript.GdScriptBundle
import com.jetbrains.rider.godot.community.utils.GodotFileUtil

internal class GdScriptLineBreakpointType :
  XLineBreakpointTypeBase(
      "GdScriptLineBreakpointTypeId",
      GdScriptBundle.message("gdscript.debug.line.breakpoint.title"),
      GdScriptDebuggerEditorsProvider()
  ) {
    override fun canPutAt(file: VirtualFile, line: Int, project: Project): Boolean {
        if (GodotFileUtil.isGdFile(file))
            return true
        return super.canPutAt(file, line, project)
    }
  }
