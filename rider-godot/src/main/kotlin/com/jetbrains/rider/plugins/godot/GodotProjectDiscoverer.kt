package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.rdclient.util.idea.ProtocolSubscribedProjectComponent
import com.jetbrains.rider.debugger.util.isExistingFile
import com.jetbrains.rider.model.RdExistingSolution
import com.jetbrains.rider.projectView.solutionDescription
import com.jetbrains.rider.projectView.solutionFile
import com.jetbrains.rider.util.idea.getComponent

class GodotProjectDiscoverer(project: Project) : ProtocolSubscribedProjectComponent(project) {

    private val projectGodotFile = project.projectDir.findChild("project.godot")

    // It's a Godot project, but not necessarily loaded correctly (e.g. it might be opened as folder)
    val getIsGodotProject: Boolean
        get() {
            if (projectGodotFile != null) {
                return projectGodotFile.isExistingFile() && isCorrectlyLoadedSolution(project)
            }
            return false
        }

    val port: Int = 23685 // default value, //todo: read custom value from project.godot file

    // Returns false when opening a project as a plain folder
    private fun isCorrectlyLoadedSolution(project: Project): Boolean {
        val solutionFile = project.solutionFile
        return project.solutionDescription is RdExistingSolution && solutionFile.isFile && solutionFile.extension.equals("sln", true)
    }

    companion object {
        fun getInstance(project: Project) = project.getComponent<GodotProjectDiscoverer>()
    }
}

val Project.projectDir: VirtualFile
    get() = LocalFileSystem.getInstance().findFileByPath(this.basePath!!)!!