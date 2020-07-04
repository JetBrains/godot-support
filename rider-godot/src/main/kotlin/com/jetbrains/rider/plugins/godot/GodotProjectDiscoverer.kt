package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.rd.platform.util.idea.LifetimedProjectService
import com.jetbrains.rd.util.reactive.IProperty
import com.jetbrains.rd.util.reactive.Property
import com.jetbrains.rd.util.reactive.whenTrue
import com.jetbrains.rider.debugger.util.isExistingFile
import com.jetbrains.rider.model.RdExistingSolution
import com.jetbrains.rider.projectView.solutionDescription
import com.jetbrains.rider.projectView.solutionFile
import com.jetbrains.rider.util.idea.getService

class GodotProjectDiscoverer(project: Project) : LifetimedProjectService(project) {
    private val projectGodotFile = project.projectDir.findChild("project.godot")
    val isGodotProject: IProperty<Boolean> = Property(false)
    val godotPath : IProperty<String?> = Property(null)

    init {
        val isGodot = getIsGodotProject()
        if (isGodot)
            logger.info("Project was identified as Godot.")
        isGodotProject.set(isGodot)
        isGodotProject.whenTrue(projectServiceLifetime){
            godotPath.set(GodotServer.getGodotPath(project))
        }
    }

    // It's a Godot project, but not necessarily loaded correctly (e.g. it might be opened as folder)
    private fun getIsGodotProject() : Boolean
    {
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
        fun getInstance(project: Project) = project.getService<GodotProjectDiscoverer>()
        private val logger = Logger.getInstance(GodotProjectDiscoverer::class.java)
    }
}

val Project.projectDir: VirtualFile
    get() = LocalFileSystem.getInstance().findFileByPath(this.basePath!!)!!