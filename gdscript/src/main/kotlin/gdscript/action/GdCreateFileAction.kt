package gdscript.action

import com.intellij.ide.actions.CreateFileFromTemplateAction
import com.intellij.ide.actions.CreateFileFromTemplateDialog
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import gdscript.GdIcon


class GdCreateFileAction : CreateFileFromTemplateAction, DumbAware {

    constructor() : super("GdScript", "New GdScript file", GdIcon.FILE)

    override fun buildDialog(project: Project, directory: PsiDirectory, builder: CreateFileFromTemplateDialog.Builder) {
        // TODO ii PyBundle.messagePointer("action.create.python.file.title")
        // PyBundle.message("create.python.file.action.new.python.file")
        // https://github.com/JetBrains/intellij-community/blob/master/python/src/com/jetbrains/python/actions/CreatePythonFileAction.java
        builder
            .setTitle("GdScript")
            .addKind("Node default", GdIcon.FILE, "Node default")
            .addKind("Empty", GdIcon.FILE, "Object empty")
            .addKind("Character2D movement", GdIcon.FILE, "CharacterBody2D basic_movement")
            .addKind("Character3D movement", GdIcon.FILE, "CharacterBody3D basic_movement")
            .addKind("EditorScript", GdIcon.FILE, "EditorScript basic_editor_script")
            .addKind("VisualShaderNodeCustom", GdIcon.FILE, "VisualShaderNodeCustom basic")
            .addKind("EditorScenePostImport basic", GdIcon.FILE, "EditorScenePostImport basic_import_script")
            .addKind("EditorScenePostImport no comments", GdIcon.FILE, "EditorScenePostImport no_comments")
            .addKind("EditorPlugin", GdIcon.FILE, "EditorPlugin plugin")
//            .addKind(
//                PyBundle.message("create.python.file.action.python.unit.test"),
//                PythonFileType.INSTANCE.getIcon(),
//                "Python Unit Test"
//            )
//            .addKind(
//                PyBundle.message("create.python.file.action.python.stub"),
//                PyiFileType.INSTANCE.getIcon(),
//                "Python Stub"
//            )
    }

    override fun getActionName(directory: PsiDirectory?, newName: String, templateName: String?): String {
        return "Losos: " + newName;
    }

//    fun CreatePythonFileAction() {
//        super(
//            PyBundle.messagePointer("action.create.python.file.title"),
//            PyBundle.messagePointer("action.create.python.file.description"),
//            PythonFileType.INSTANCE.getIcon()
//        )
//    }
//
//    protected fun buildDialog(
//        project: Project?,
//        directory: PsiDirectory?,
//        builder: CreateFileFromTemplateDialog.Builder,
//    ) {
//        builder
//            .setTitle(PyBundle.message("create.python.file.action.new.python.file"))
//            .addKind(
//                PyBundle.message("create.python.file.action.python.file"),
//                PythonFileType.INSTANCE.getIcon(),
//                "Python Script"
//            )
//            .addKind(
//                PyBundle.message("create.python.file.action.python.unit.test"),
//                PythonFileType.INSTANCE.getIcon(),
//                "Python Unit Test"
//            )
//            .addKind(
//                PyBundle.message("create.python.file.action.python.stub"),
//                PyiFileType.INSTANCE.getIcon(),
//                "Python Stub"
//            )
//    }
//
//    protected fun getActionName(directory: PsiDirectory?, @NotNull newName: String?, templateName: String?): String? {
//        return PyBundle.message("create.python.file.script.action", newName)
//    }

}