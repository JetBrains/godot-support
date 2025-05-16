package gdscript.action

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import gdscript.GdFileType
import gdscript.annotator.isGodotSupportInstalled
import gdscript.utils.RiderGodotSupportPluginUtil


class GdCreateFileAction : CreateFileFromTemplateAction(), DumbAware {
    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle("GdScript")
        FileTemplateManager.getInstance(project)
            .allTemplates
            .forEach {
                if (it.extension == GdFileType.defaultExtension) {
                    builder.addKind(it.name, GdFileType.icon, it.name)
                }
            }

        builder
            .addKind("Node default", GdFileType.icon, "Node default")
            .addKind("Empty", GdFileType.icon, "Object empty")
            .addKind("Character2D movement", GdFileType.icon, "CharacterBody2D basic_movement")
            .addKind("Character3D movement", GdFileType.icon, "CharacterBody3D basic_movement")
            .addKind("EditorScript", GdFileType.icon, "EditorScript basic_editor_script")
            .addKind("VisualShaderNodeCustom", GdFileType.icon, "VisualShaderNodeCustom basic")
            .addKind("EditorScenePostImport basic", GdFileType.icon, "EditorScenePostImport basic_import_script")
            .addKind("EditorScenePostImport no comments", GdFileType.icon, "EditorScenePostImport no_comments")
            .addKind("EditorPlugin", GdFileType.icon, "EditorPlugin plugin")
    }

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String {
        return "GdScript file"
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        if (project == null) {
            e.presentation.isVisible = false
            return
        }

        e.presentation.setEnabledAndVisible(isGodotSupportInstalled && RiderGodotSupportPluginUtil.isGodotProject(project))
    }
}
