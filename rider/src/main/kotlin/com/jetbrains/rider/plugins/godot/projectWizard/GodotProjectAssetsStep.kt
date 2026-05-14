package com.jetbrains.rider.plugins.godot.projectWizard

import com.intellij.ide.wizard.NewProjectWizardBaseData.Companion.baseData
import com.intellij.ide.wizard.NewProjectWizardStep
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBList
import com.intellij.ui.dsl.listCellRenderer.textListCellRenderer
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.AlignY
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.MAX_LINE_LENGTH_WORD_WRAP
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.RowLayout
import com.intellij.ui.dsl.builder.TopGap
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.selected
import com.intellij.ui.layout.and
import com.intellij.util.ui.UIUtil
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.projectView.projectTemplates.wizardTemplates.common.RiderAbstractNewProjectWizardStep
import javax.swing.ListSelectionModel

enum class GodotWizardProjectType(val displayName: String, val description: String) {
    GAME(
        GodotPluginBundle.message("wizard.godot.project.type.game"),
        GodotPluginBundle.message("wizard.godot.project.type.game.description")
    ),
    GAME_ASSET(
        GodotPluginBundle.message("wizard.godot.project.type.game.asset"),
        GodotPluginBundle.message("wizard.godot.project.type.game.asset.description")
    ),
    EDITOR_PLUGIN(
        GodotPluginBundle.message("wizard.godot.project.type.editor"),
        GodotPluginBundle.message("wizard.godot.project.type.editor.description")
    )
}

// would likely be good to reuse AssetsNewProjectWizardStep or similar approach,
// ideally step should just list files, the shared logic should do VFS operations
class GodotProjectAssetsStep(parent: NewProjectWizardStep) : RiderAbstractNewProjectWizardStep(parent) {
    private val selectedProjectTypeProperty = propertyGraph.property(GodotWizardProjectType.GAME)
    private val listModel = JBList.createDefaultListModel(GodotWizardProjectType.GAME, GodotWizardProjectType.GAME_ASSET, GodotWizardProjectType.EDITOR_PLUGIN)
    private val list = JBList(listModel).apply {
        background = UIUtil.SIDE_PANEL_BACKGROUND
        selectionMode = ListSelectionModel.SINGLE_SELECTION
        selectedIndex = 0
        cellRenderer = textListCellRenderer { it?.displayName ?: "" }
    }
    private var scrollCellList: Cell<JBList<GodotWizardProjectType>>? = null
    private val useCMakeProperty = propertyGraph.property(false)
    private val includeGDExtensionProperty = propertyGraph.property(true)
    private val useAddonsManagerProperty = propertyGraph.property(true)
    private val gdExtensionNameProperty = propertyGraph.property((baseData?.name ?: "") + "Ext")
        .apply {
            val nameProperty = baseData?.nameProperty
            if (nameProperty != null) {
                dependsOn(nameProperty) { nameProperty.get() + "Ext" }
            }
        }

    init {
        list.addListSelectionListener { event ->
            if (!event.valueIsAdjusting) {
                val selected = list.selectedValue ?: return@addListSelectionListener
                selectedProjectTypeProperty.set(selected)
                scrollCellList?.comment?.text = selected.description
            }
        }
    }

    override fun setupUI(builder: Panel) {
        with(builder) {
            row {
                panel {
                    row(GodotPluginBundle.message("wizard.godot.project.template")) {}.topGap(TopGap.SMALL)
                }.align(AlignY.TOP)
                panel {
                    row {
                        scrollCellList = scrollCell(list).align(AlignX.FILL)
                            .comment(selectedProjectTypeProperty.get().description, maxLineLength = MAX_LINE_LENGTH_WORD_WRAP)
                    }
                }
            }.layout(RowLayout.LABEL_ALIGNED)

            group(GodotPluginBundle.message("wizard.godot.additional.features")) {
                lateinit var useCmakeCb: Cell<JBCheckBox>
                row {
                    useCmakeCb = checkBox(GodotPluginBundle.message("wizard.godot.use.cmake"))
                        .bindSelected(useCMakeProperty)
                }
                indent {
                    row {
                        val gdExtCb = checkBox(GodotPluginBundle.message("wizard.godot.project.include.gdextension"))
                            .bindSelected(includeGDExtensionProperty)
                            .enabledIf(useCmakeCb.selected)
                            .comment(GodotPluginBundle.message("text.automatically.creates.c.boilerplate.gdextension.file"))
                        textField()
                            .label(GodotPluginBundle.message("wizard.godot.project.gdextension.name"))
                            .bindText(gdExtensionNameProperty)
                            .enabledIf(useCmakeCb.selected and gdExtCb.selected)
                    }
                    row {
                        checkBox(GodotPluginBundle.message("wizard.godot.addons.use.manager"))
                            .bindSelected(useAddonsManagerProperty)
                            .enabledIf(useCmakeCb.selected)
                            .comment(GodotPluginBundle.message("wizard.godot.addons.use.manager.note"))
                    }
                }
            }
            separator()
            row {
                comment(GodotPluginBundle.message("wizard.godot.project.template.source"))
            }
        }
    }

    override fun setupProject(project: Project) {
        val projectName = baseData?.name ?: return
        val projectPath = baseData?.contentEntryPath ?: return
        val selectedProjectType: GodotWizardProjectType = selectedProjectTypeProperty.get()
        val useCMake = useCMakeProperty.get()
        val includeGDExtension = useCMake && includeGDExtensionProperty.get()
        val useAddonsManager = useCMake && useAddonsManagerProperty.get()
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
            if (useCMake) {
                writeFile(projectDir, "CMakeLists.txt", "templates/godotCommon/CMakeLists.txt.ft", projectName, gdExtensionName)
                if (useAddonsManager) {
                    VfsUtil.createDirectoryIfMissing(projectDir, "cmake")?.let {
                        writeFile(it, "GodotAddonsManager.cmake", "templates/godotCommon/cmake/GodotAddonsManager.cmake")
                    }
                }
            }

            writeFile(projectDir, "main.tscn", "templates/godotCommon/main.tscn")

            val projectGodotTemplate = when (selectedProjectType) {
                GodotWizardProjectType.GAME_ASSET -> "templates/godotGameExtension/project.godot.ft"
                GodotWizardProjectType.EDITOR_PLUGIN -> "templates/godotEditorExtension/project.godot.ft"
                GodotWizardProjectType.GAME -> "templates/godotGameExtension/project.godot.ft"
            }
            writeFile(projectDir, "project.godot", projectGodotTemplate, projectName, gdExtensionName)

            // Addon files
            if (selectedProjectType == GodotWizardProjectType.EDITOR_PLUGIN) {
                writeFile(addonDir, "plugin.cfg", "templates/godotEditorExtension/addon/plugin.cfg.ft", projectName, gdExtensionName)
                writeFile(addonDir, "$addonName.gd", "templates/godotEditorExtension/addon/addon.gd.ft", projectName, gdExtensionName)
            }
            writeFile(addonDir, "README.md", "templates/godotCommon/addon/README.md.ft", projectName, gdExtensionName)
            iconsDir?.let { writeFile(it, "icon.svg", "templates/godotCommon/addon/icons/icon.svg") }

            if (includeGDExtension) {
                val gdextensionSourcePath = "templates/godotCommon/addon/bin/addon.gdextension.ft"
                binDir?.let { writeFile(it, "$gdExtensionName.gdextension", gdextensionSourcePath, projectName, gdExtensionName) }

                // Addon/cpp files
                if (cppDir != null) {
                    writeFile(cppDir, "CMakeLists.txt", "templates/godotCommon/addon/cpp/CMakeLists.txt.ft", projectName, gdExtensionName)
                    writeFile(cppDir, ".gdignore", "templates/godotCommon/addon/cpp/.gdignore")
                    if (srcDir != null) {
                        writeFile(srcDir, "register_types.h", "templates/godotCommon/addon/cpp/src/register_types.h")
                        val registerTypesCppTemplate = when (selectedProjectType) {
                            GodotWizardProjectType.EDITOR_PLUGIN -> "templates/godotEditorExtension/addon/cpp/src/register_types.cpp"
                            GodotWizardProjectType.GAME, GodotWizardProjectType.GAME_ASSET -> "templates/godotGameExtension/addon/cpp/src/register_types.cpp"
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
            content = content.replace($$"${PROJECT_NAME}", projectName)
        }
        if (gdExtensionName != null) {
            content = content.replace($$"${GDEXTENSION_NAME}", gdExtensionName)
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
