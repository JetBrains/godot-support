package com.jetbrains.rider.plugins.godot.rd

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.BaseProjectDirectories
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.project.ProjectCoreUtil
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil

class OpenInGodotAction : DumbAwareAction() {
    private val hidePrefixSet = setOf(
        "sln", "slnx", "slnf", "csproj", "fsproj", "vcxproj",
        "uid", "import", "remap", "old"
    )

    fun fileShouldBeShown(f: VirtualFile, baseDirectory: VirtualFile): Boolean {
        val isUnderDotIdea = ProjectCoreUtil.isProjectOrWorkspaceFile(f)
        val isHidden = generateSequence(f) { it.parent }
            .takeWhile { it != baseDirectory }
            .any { it.name.startsWith(".") }

        return !hidePrefixSet.contains(f.extension) &&
            !isHidden &&
            !isUnderDotIdea
    }

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT
    override fun update(e: AnActionEvent) {
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE)
        val project = e.project
        val baseDirectory = if (file != null && project != null) {
            BaseProjectDirectories.getInstance(project).getBaseDirectoryFor(file)
        } else null
        if (file == null || project == null || baseDirectory == null || !fileShouldBeShown(file, baseDirectory)) {
            e.presentation.isEnabledAndVisible = false
            return
        }
        e.presentation.isVisible = GodotCommunityUtil.isGodotProject(project)
        e.presentation.isEnabled = GodotRdClientService.getInstanceIfCreated(project)?.isConnected ?: false
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val file = e.getData(CommonDataKeys.VIRTUAL_FILE) ?: return
        GodotRdClientService.getInstanceIfCreated(project)?.openInGodot(file)
    }
}
