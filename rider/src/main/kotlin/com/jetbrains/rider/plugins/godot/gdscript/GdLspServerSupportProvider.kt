package com.jetbrains.rider.plugins.godot.gdscript

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lsp.api.LspServerSupportProvider
import com.intellij.platform.lsp.api.ProjectWideLspServerDescriptor


class GdLspServerSupportProvider : LspServerSupportProvider {
    override fun fileOpened(project: Project, file: VirtualFile, serverStarter: LspServerSupportProvider.LspServerStarter) {
        if (file.extension == "gd") {
            serverStarter.ensureServerStarted(GdLspServerDescriptor(project))
        }
    }
}
private class GdLspServerDescriptor(project: Project) : ProjectWideLspServerDescriptor(project, "Gd") {
    override fun isSupportedFile(file: VirtualFile) = file.extension == "gd"
    val port = "6008"

    override fun createCommandLine(): GeneralCommandLine {
        return GeneralCommandLine("C:\\Downloads\\Godot_v4.1.1-stable_mono_win64\\Godot_v4.1.1-stable_mono_win64.exe", "--path", "C:/Work/godot-demo-projects/mono/dodge_the_creeps/projects/MainProject", "--editor")
    }
}