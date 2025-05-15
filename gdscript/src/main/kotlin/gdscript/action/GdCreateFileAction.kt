package gdscript.action

import GdScriptPluginIcons
import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import gdscript.GdFileType


class GdCreateFileAction : CreateFileFromTemplateAction, DumbAware {

    constructor() : super("GdScript", "New GdScript file", GdScriptPluginIcons.Icons.GDScript)

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle("GdScript")
        FileTemplateManager.getInstance(project)
            .allTemplates
            .forEach {
                if (it.extension == GdFileType.INSTANCE.defaultExtension) {
                    builder.addKind(it.name, GdScriptPluginIcons.Icons.GDScript, it.name)
                }
            }

        builder
            .addKind("Node default", GdScriptPluginIcons.Icons.GDScript, "Node default")
            .addKind("Empty", GdScriptPluginIcons.Icons.GDScript, "Object empty")
            .addKind("Character2D movement", GdScriptPluginIcons.Icons.GDScript, "CharacterBody2D basic_movement")
            .addKind("Character3D movement", GdScriptPluginIcons.Icons.GDScript, "CharacterBody3D basic_movement")
            .addKind("EditorScript", GdScriptPluginIcons.Icons.GDScript, "EditorScript basic_editor_script")
            .addKind("VisualShaderNodeCustom", GdScriptPluginIcons.Icons.GDScript, "VisualShaderNodeCustom basic")
            .addKind("EditorScenePostImport basic", GdScriptPluginIcons.Icons.GDScript, "EditorScenePostImport basic_import_script")
            .addKind("EditorScenePostImport no comments", GdScriptPluginIcons.Icons.GDScript, "EditorScenePostImport no_comments")
            .addKind("EditorPlugin", GdScriptPluginIcons.Icons.GDScript, "EditorPlugin plugin")
    }

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String {
        return "GdScript file"
    }

}
