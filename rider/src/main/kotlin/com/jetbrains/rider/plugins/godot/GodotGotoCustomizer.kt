package com.jetbrains.rider.plugins.godot

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.jetbrains.rider.globalNavigation.GotoCustomizer
import com.jetbrains.rider.projectView.isDirectorySolution

/**
 * Puts the GDScript and scene files of the Godot project into the "Solution" scope.
 *
 * The `.csproj` of a Godot C# project does not hold these files. [GodotFilesIndexingRuleProvider] indexes them in a file
 * set that the "Solution" scope leaves out, and no project content root holds them. `SolutionFilesChecker` asks this
 * extension point, so a file under the Godot project directory counts as a solution file.
 *
 * A directory solution does not need this check. The content root of the directory holds these files.
 *
 * The scope calls this check for each file, so it reads only the cached base path and the cached file of the VFS.
 */
class GodotGotoCustomizer : GotoCustomizer {
    override fun isGotoTargetInProject(project: Project, file: VirtualFile): Boolean {
        if (!isGodotIndexedFile(file) || project.isDirectorySolution) return false
        val basePath = GodotProjectDiscoverer.getInstance(project).mainProjectBasePathFlow.value ?: return false
        val baseDirectory = VirtualFileManager.getInstance().findFileByNioPath(basePath) ?: return false
        return VfsUtil.isAncestor(baseDirectory, file, true)
    }
}
