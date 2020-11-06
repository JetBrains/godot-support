package com.jetbrains.rider.plugins.godot

import com.intellij.ide.impl.ProjectUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.WindowManager
import com.intellij.util.BitUtil
import com.jetbrains.rd.platform.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rider.model.godot.frontendBackend.frontendBackendModel
import com.jetbrains.rider.projectView.solution
import java.awt.Frame

class FrontendBackendHost(project: Project) : ProtocolSubscribedProjectComponent(project) {
    val model = project.solution.frontendBackendModel

    init {
        model.activateRider.advise(projectComponentLifetime) {
            ProjectUtil.focusProjectWindow(project, true)
            val frame = WindowManager.getInstance().getFrame(project)
            if (frame != null) {
                if (BitUtil.isSet(frame.extendedState, Frame.ICONIFIED))
                    frame.extendedState = BitUtil.set(frame.extendedState, Frame.ICONIFIED, false)
            }
        }
    }

    companion object {
        fun getInstance(project: Project): FrontendBackendHost = project.getComponent(FrontendBackendHost::class.java)
    }
}