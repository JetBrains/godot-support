package gdscript.action

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.jetbrains.rider.godot.community.gdscript.GdFileType
import gdscript.GdScriptBundle
import gdscript.annotator.isGodotSupportInstalled
import gdscript.utils.RiderGodotSupportPluginUtil
import gdscript.utils.hasCompletedTrue


class GdCreateFileAction : CreateFileFromTemplateAction(), DumbAware {
    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle(GdScriptBundle.message("action.create.file.title"))
        FileTemplateManager.getInstance(project)
            .allTemplates
            .forEach {
                if (it.extension == GdFileType.defaultExtension) {
                    builder.addKind(it.name, GdFileType.icon, it.name)
                }
            }

        builder
            .addKind(GdScriptBundle.message("template.kind.node.default"), GdFileType.icon, "Node default")
            .addKind(GdScriptBundle.message("template.kind.empty"), GdFileType.icon, "Object empty")
            .addKind(GdScriptBundle.message("template.kind.character2d.movement"), GdFileType.icon, "CharacterBody2D basic_movement")
            .addKind(GdScriptBundle.message("template.kind.character3d.movement"), GdFileType.icon, "CharacterBody3D basic_movement")
            .addKind(GdScriptBundle.message("template.kind.editorscript"), GdFileType.icon, "EditorScript basic_editor_script")
            .addKind(GdScriptBundle.message("template.kind.visualshadernodecustom"), GdFileType.icon, "VisualShaderNodeCustom basic")
            .addKind(GdScriptBundle.message("template.kind.editorscenepostimport.basic"), GdFileType.icon, "EditorScenePostImport basic_import_script")
            .addKind(GdScriptBundle.message("template.kind.editorscenepostimport.no.comments"), GdFileType.icon, "EditorScenePostImport no_comments")
            .addKind(GdScriptBundle.message("template.kind.editorplugin"), GdFileType.icon, "EditorPlugin plugin")
    }

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String {
        return GdScriptBundle.message("action.create.file.get.action.name")
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        if (project == null) {
            e.presentation.isVisible = false
            return
        }

        e.presentation.setEnabledAndVisible(isGodotSupportInstalled && RiderGodotSupportPluginUtil.isGodotProject(project).hasCompletedTrue())
    }
}
