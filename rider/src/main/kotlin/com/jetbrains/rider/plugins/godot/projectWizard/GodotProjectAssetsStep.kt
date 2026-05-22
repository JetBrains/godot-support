package com.jetbrains.rider.plugins.godot.projectWizard

import com.intellij.ide.wizard.NewProjectWizardBaseData.Companion.baseData
import com.intellij.ide.wizard.NewProjectWizardStep
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBList
import com.intellij.ui.dsl.builder.AlignX
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.MAX_LINE_LENGTH_WORD_WRAP
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.bindSelected
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.selected
import com.intellij.ui.dsl.listCellRenderer.textListCellRenderer
import com.intellij.ui.layout.and
import com.intellij.util.ui.UIUtil
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.projectView.projectTemplates.wizardTemplates.common.RiderAbstractNewProjectWizardStep
import com.jetbrains.rider.projectView.projectTemplates.wizardTemplates.common.addGitCheckboxRow
import com.jetbrains.rider.projectView.projectTemplates.wizardTemplates.common.initializeGitRepository
import org.jetbrains.annotations.Nls
import javax.swing.ListSelectionModel

enum class GodotWizardProjectType(val displayName: String, @Nls val description: String) {
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
    private val gitProperty = propertyGraph.property(true)
    private val useCMakeProperty = propertyGraph.property(false)
    private val includeGDExtensionProperty = propertyGraph.property(true)
    private val useAddonsManagerProperty = propertyGraph.property(true)
    private val extensionNameProperty = propertyGraph.property((baseData?.name ?: "") + "Ext")
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
            builder.addGitCheckboxRow(gitProperty, this@GodotProjectAssetsStep)
            row(GodotPluginBundle.message("wizard.godot.project.template")) {
                scrollCellList = scrollCell(list).align(AlignX.FILL)
                    .comment(selectedProjectTypeProperty.get().description, maxLineLength = MAX_LINE_LENGTH_WORD_WRAP)
            }

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
                            .bindText(extensionNameProperty)
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
        val initGit = gitProperty.get()
        val useCMake = useCMakeProperty.get()
        val includeGDExtension = useCMake && includeGDExtensionProperty.get()
        val useAddonsManager = useCMake && useAddonsManagerProperty.get()
        val extensionName = if (includeGDExtension) extensionNameProperty.get().ifEmpty { projectName } else projectName

        // can't use RiderGitNewProjectWizardStep, see RIDER-139050
        if (initGit && !includeGDExtension) {
            initializeGitRepository(true, project)
        }

        ApplicationManager.getApplication().runWriteAction {
            val projectDir = VfsUtil.createDirectoryIfMissing(projectPath) ?: return@runWriteAction
            val addonDir = VfsUtil.createDirectoryIfMissing(projectDir, "addons/$extensionName") ?: return@runWriteAction
            val iconsDir = VfsUtil.createDirectoryIfMissing(addonDir, "icons")

            val binDir = if (includeGDExtension) VfsUtil.createDirectoryIfMissing(addonDir, "bin") else null
            val nativeDir = if (includeGDExtension) VfsUtil.createDirectoryIfMissing(projectDir, "native/$extensionName") else null
            val srcDir = nativeDir?.let { VfsUtil.createDirectoryIfMissing(it, "src") }

            // Root files
            val enabledPlugins = mutableListOf<String>()
            writeFile(projectDir, ".gitignore", "templates/godotCommon/gitignore.txt")
            if (useCMake) {
                writeFile(projectDir, "CMakeLists.txt", "templates/godotCommon/CMakeLists.txt.ft", projectName, extensionName)
                if (useAddonsManager || includeGDExtension) {
                    val cmakeDir = VfsUtil.createDirectoryIfMissing(projectDir, "cmake")
                    if (cmakeDir == null) {
                        thisLogger().error("Failed to create cmake directory in ${projectDir.path}")
                    } else {
                        if (useAddonsManager) {
                            writeFile(cmakeDir, "GodotAddonsManager.cmake", "templates/godotCommon/cmake/GodotAddonsManager.cmake")
                            // addon-manager is pre-configured to download Rider plugin, so we enable it
                            enabledPlugins.add("\"res://addons/rider-plugin/plugin.cfg\"")
                        }
                        if (includeGDExtension) {
                            val setupResource = if (initGit)
                                "templates/godotCommon/cmake/GodotCppSetup.git.cmake"
                            else
                                "templates/godotCommon/cmake/GodotCppSetup.fetchcontent.cmake"
                            writeFile(cmakeDir, "GodotCppSetup.cmake", setupResource, extensionName = extensionName)
                        }
                    }
                }
            }

            copyFile(projectDir, "main.tscn", "templates/godotCommon/main.tscn")
            if (useCMake && includeGDExtension) {
                // we need the readme only in the includeGDExtension case, otherwise there is nothing to explain
                writeFile(projectDir, "README.md", "templates/godotCommon/README.md.ft", projectName, extensionName)
            }

            // Addon files
            if (selectedProjectType == GodotWizardProjectType.EDITOR_PLUGIN) {
                writeFile(addonDir, "plugin.cfg", "templates/godotEditorExtension/addons/addon/plugin.cfg.ft", projectName, extensionName)
                writeFile(addonDir, "$extensionName.gd", "templates/godotEditorExtension/addons/addon/addon.gd.ft", projectName, extensionName)
                enabledPlugins.add("\"res://addons/$extensionName/plugin.cfg\"")
            }
            writeFile(addonDir, "README.md", "templates/godotCommon/addons/addon/README.md.ft", projectName, extensionName)
            iconsDir?.let { writeFile(it, "icon.svg", "templates/godotCommon/addons/addon/icons/icon.svg") }

            // enable plugins
            val editorPluginsSection = if (enabledPlugins.isEmpty()) "" else
                "\n[editor_plugins]\n\nenabled=PackedStringArray(${enabledPlugins.joinToString(", ")})\n"
            writeFile(projectDir, "project.godot", "templates/godotCommon/project.godot.ft", projectName, extensionName,
                mapOf("EDITOR_PLUGINS_SECTION" to editorPluginsSection))

            if (includeGDExtension) {
                val gdextensionSourcePath = "templates/godotCommon/addons/addon/bin/addon.gdextension.ft"
                binDir?.let { writeFile(it, "$extensionName.gdextension", gdextensionSourcePath, projectName, extensionName) }

                // Native C++ files
                if (nativeDir != null) {
                    writeFile(nativeDir, "CMakeLists.txt", "templates/godotCommon/native/rider-plugin/CMakeLists.txt.ft", projectName, extensionName)
                    writeFile(nativeDir, ".gdignore", "templates/godotCommon/native/rider-plugin/.gdignore")
                    if (srcDir != null) {
                        writeFile(srcDir, "register_types.h", "templates/godotCommon/native/rider-plugin/src/register_types.h")
                        val registerTypesCppTemplate = when (selectedProjectType) {
                            GodotWizardProjectType.EDITOR_PLUGIN -> "templates/godotEditorExtension/native/rider-plugin/src/register_types.cpp"
                            GodotWizardProjectType.GAME, GodotWizardProjectType.GAME_ASSET -> "templates/godotGameExtension/native/rider-plugin/src/register_types.cpp"
                        }
                        writeFile(srcDir, "register_types.cpp", registerTypesCppTemplate)
                        writeFile(srcDir, "example_class.h", "templates/godotCommon/native/rider-plugin/src/example_class.h")
                        writeFile(srcDir, "example_class.cpp", "templates/godotCommon/native/rider-plugin/src/example_class.cpp")
                    }
                }
            }

            projectDir.refresh(false, true)
        }
    }

    private fun writeFile(dir: VirtualFile, fileName: String, resourcePath: String, projectName: String? = null, extensionName: String? = null, extraSubstitutions: Map<String, String> = emptyMap()) {
        var content = loadTemplate(resourcePath)
        if (projectName != null) {
            content = content.replace($$"${PROJECT_NAME}", projectName)
        }
        if (extensionName != null) {
            content = content.replace($$"${EXTENSION_NAME}", extensionName)
        }
        for ((key, value) in extraSubstitutions) {
            content = content.replace("\${$key}", value)
        }
        val file = dir.createChildData(this, fileName)
        VfsUtil.saveText(file, content)
    }

    // would not change the BOM/no-BOM, see RIDER-138957
    @Suppress("SameParameterValue")
    private fun copyFile(dir: VirtualFile, fileName: String, resourcePath: String) {
        val bytes = javaClass.classLoader.getResourceAsStream(resourcePath)
            ?.use { it.readBytes() }
            ?: throw IllegalStateException("Template not found: $resourcePath")
        val file = dir.createChildData(this, fileName)
        file.setBinaryContent(bytes)
    }

    private fun loadTemplate(resourcePath: String): String {
        return javaClass.classLoader.getResourceAsStream(resourcePath)
            ?.bufferedReader()?.use { it.readText() }
            ?: throw IllegalStateException("Template not found: $resourcePath")
    }
}
