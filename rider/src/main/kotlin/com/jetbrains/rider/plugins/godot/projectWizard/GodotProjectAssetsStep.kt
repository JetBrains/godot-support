package com.jetbrains.rider.plugins.godot.projectWizard

import com.intellij.ide.wizard.NewProjectWizardBaseData.Companion.baseData
import com.intellij.ide.wizard.NewProjectWizardStep
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.dsl.builder.*
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.projectView.projectTemplates.wizardTemplates.common.RiderAbstractNewProjectWizardStep

enum class GodotWizardProjectType {
    GAME, EDITOR
}

// would likely be good to reuse AssetsNewProjectWizardStep or similar approach,
// ideally step should just list files, the shared logic should do VFS operations
class GodotProjectAssetsStep(parent: NewProjectWizardStep) : RiderAbstractNewProjectWizardStep(parent) {

    private val selectedProjectTypeProperty = propertyGraph.property(GodotWizardProjectType.GAME)
    private val includeGDExtensionProperty = propertyGraph.property(false)
    private val gdExtensionNameProperty = propertyGraph.property(baseData?.name ?: "")
        .apply {
            val nameProperty = baseData?.nameProperty
            if (nameProperty != null) {
                dependsOn(nameProperty) { nameProperty.get() }
            }
        }

    override fun setupUI(builder: Panel) {
        with(builder) {
            row(GodotPluginBundle.message("wizard.godot.project.type")) {
                segmentedButton(listOf(GodotWizardProjectType.GAME, GodotWizardProjectType.EDITOR)) {
                    text = when (it) {
                        GodotWizardProjectType.GAME -> GodotPluginBundle.message("wizard.godot.project.type.game")
                        GodotWizardProjectType.EDITOR -> GodotPluginBundle.message("wizard.godot.project.type.editor")
                    }
                }.bind(selectedProjectTypeProperty)
            }.bottomGap(BottomGap.SMALL)
            row("") {
                checkBox(GodotPluginBundle.message("wizard.godot.project.include.gdextension"))
                    .bindSelected(includeGDExtensionProperty)
            }
            row(GodotPluginBundle.message("wizard.godot.project.gdextension.name")) {
                textField()
                    .bindText(gdExtensionNameProperty)
            }.visibleIf(includeGDExtensionProperty)
        }
    }

    override fun setupProject(project: Project) {
        val projectName = baseData?.name ?: return
        val projectPath = baseData?.contentEntryPath ?: return
        val selectedProjectType: GodotWizardProjectType = selectedProjectTypeProperty.get()
        val includeGDExtension = includeGDExtensionProperty.get()
        val gdExtensionName = if (includeGDExtension) gdExtensionNameProperty.get().ifEmpty { projectName } else projectName

        ApplicationManager.getApplication().runWriteAction {
            val projectDir = VfsUtil.createDirectoryIfMissing(projectPath) ?: return@runWriteAction
            val addonName = if (includeGDExtension) gdExtensionName else projectName
            val addonDir = VfsUtil.createDirectoryIfMissing(projectDir, "addons/$addonName") ?: return@runWriteAction
            val iconsDir = VfsUtil.createDirectoryIfMissing(addonDir, "icons")
            
            val binDir = if (includeGDExtension) VfsUtil.createDirectoryIfMissing(addonDir, "bin") else null
            val cppDir = if (includeGDExtension) VfsUtil.createDirectoryIfMissing(addonDir, "cpp") else null
            val srcDir = cppDir?.let { VfsUtil.createDirectoryIfMissing(it, "src") }

            // Root files
            writeFile(projectDir, ".gitignore", "templates/godotCommon/gitignore.txt")
            if (includeGDExtension) {
                writeFile(projectDir, "CMakeLists.txt", "templates/godotCommon/CMakeLists.txt.ft", projectName, gdExtensionName)
            }
            writeFile(projectDir, "main.tscn", "templates/godotCommon/main.tscn")
            
            val projectGodotTemplate = when (selectedProjectType) {
                GodotWizardProjectType.GAME -> "templates/godotGameExtension/project.godot.ft"
                GodotWizardProjectType.EDITOR -> "templates/godotEditorExtension/project.godot.ft"
            }
            writeFile(projectDir, "project.godot", projectGodotTemplate, projectName, gdExtensionName)

            // Addon files
            if (selectedProjectType == GodotWizardProjectType.EDITOR) {
                writeFile(addonDir, "plugin.cfg", "templates/godotEditorExtension/addon/plugin.cfg.ft", projectName, gdExtensionName)
                writeFile(addonDir, "$addonName.gd", "templates/godotEditorExtension/addon/addon.gd.ft", projectName, gdExtensionName)
            }
            writeFile(addonDir, "README.md", "templates/godotCommon/addon/README.md.ft", projectName, gdExtensionName)
            iconsDir?.let { writeFile(it, "icon.svg", "templates/godotCommon/addon/icons/icon.svg") }
            
            if (includeGDExtension) {
                val gdextensionSourcePath = when (selectedProjectType) {
                    GodotWizardProjectType.GAME -> "templates/godotGameExtension/addon/bin/addon.gdextension.ft"
                    GodotWizardProjectType.EDITOR -> "templates/godotEditorExtension/addon/bin/addon.gdextension.ft"
                }
                binDir?.let { writeFile(it, "$gdExtensionName.gdextension", gdextensionSourcePath, projectName, gdExtensionName) }

                // Addon/cpp files
                if (cppDir != null) {
                    writeFile(cppDir, "CMakeLists.txt", "templates/godotCommon/addon/cpp/CMakeLists.txt.ft", projectName, gdExtensionName)
                    writeFile(cppDir, ".gdignore", "templates/godotCommon/addon/cpp/.gdignore")
                    if (srcDir != null) {
                        writeFile(srcDir, "register_types.h", "templates/godotCommon/addon/cpp/src/register_types.h")
                        val registerTypesCppTemplate = when (selectedProjectType) {
                            GodotWizardProjectType.GAME -> "templates/godotGameExtension/addon/cpp/src/register_types.cpp"
                            GodotWizardProjectType.EDITOR -> "templates/godotEditorExtension/addon/cpp/src/register_types.cpp"
                        }
                        writeFile(srcDir, "register_types.cpp", registerTypesCppTemplate)
                        writeFile(srcDir, "example_class.h", "templates/godotCommon/addon/cpp/src/example_class.h")
                        writeFile(srcDir, "example_class.cpp", "templates/godotCommon/addon/cpp/src/example_class.cpp")
                    }
                }
            }

            projectDir.refresh(false, true)
        }
    }

    private fun writeFile(dir: VirtualFile, fileName: String, resourcePath: String, projectName: String? = null, gdExtensionName: String? = null) {
        var content = loadTemplate(resourcePath)
        if (projectName != null) {
            content = content.replace("\${PROJECT_NAME}", projectName)
        }
        if (gdExtensionName != null) {
            content = content.replace("\${GDEXTENSION_NAME}", gdExtensionName)
        }
        val file = dir.createChildData(this, fileName)
        VfsUtil.saveText(file, content)
    }

    private fun loadTemplate(resourcePath: String): String {
        return javaClass.classLoader.getResourceAsStream(resourcePath)
            ?.bufferedReader()?.use { it.readText() }
            ?: throw IllegalStateException("Template not found: $resourcePath")
    }
}
