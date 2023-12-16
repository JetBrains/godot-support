package gdscript.action

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import gdscript.GdFileType
import gdscript.GdIcon


class GdCreateFileAction : CreateFileFromTemplateAction, DumbAware {

    constructor() : super("GdScript", "New GdScript file", GdIcon.FILE)

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        builder.setTitle("GdScript")
        FileTemplateManager.getInstance(project)
            .allTemplates
            .forEach {
                if (it.extension == GdFileType.defaultExtension) {
                    builder.addKind(it.name, GdIcon.FILE, it.name)
                }
            }

        builder
            .addKind("Node default", GdIcon.FILE, "Node default")
            .addKind("Empty", GdIcon.FILE, "Object empty")
            .addKind("Character2D movement", GdIcon.FILE, "CharacterBody2D basic_movement")
            .addKind("Character3D movement", GdIcon.FILE, "CharacterBody3D basic_movement")
            .addKind("EditorScript", GdIcon.FILE, "EditorScript basic_editor_script")
            .addKind("VisualShaderNodeCustom", GdIcon.FILE, "VisualShaderNodeCustom basic")
            .addKind("EditorScenePostImport basic", GdIcon.FILE, "EditorScenePostImport basic_import_script")
            .addKind("EditorScenePostImport no comments", GdIcon.FILE, "EditorScenePostImport no_comments")
            .addKind("EditorPlugin", GdIcon.FILE, "EditorPlugin plugin")
    }

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String {
        return "GdScript file"
    }

}
