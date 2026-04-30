package com.jetbrains.rider.plugins.godot.projectWizard

import com.intellij.ide.wizard.NewProjectWizardBaseData.Companion.baseData
import com.intellij.ide.wizard.NewProjectWizardStep
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VfsUtil
import com.jetbrains.rider.projectView.projectTemplates.wizardTemplates.common.RiderAbstractNewProjectWizardStep

// would likely be good to reuse AssetsNewProjectWizardStep or similar approach,
// ideally step should list the files and the shared logic should do VFS operations
class GodotEditorExtensionAssetsStep(parent: NewProjectWizardStep) : RiderAbstractNewProjectWizardStep(parent) {

  override fun setupProject(project: Project) {
    val projectName = baseData?.name ?: return
    val projectPath = baseData?.contentEntryPath ?: return

    ApplicationManager.getApplication().runWriteAction {
      val projectDir = VfsUtil.createDirectoryIfMissing(projectPath) ?: return@runWriteAction
      val addonDir = VfsUtil.createDirectoryIfMissing(projectDir, "addons/$projectName") ?: return@runWriteAction
      val binDir = VfsUtil.createDirectoryIfMissing(addonDir, "bin")
      val iconsDir = VfsUtil.createDirectoryIfMissing(addonDir, "icons")
      val cppDir = VfsUtil.createDirectoryIfMissing(addonDir, "cpp")
      val srcDir = cppDir?.let { VfsUtil.createDirectoryIfMissing(it, "src") }

      // Root files
      writeFile(projectDir, ".gitignore", "templates/godotCommon/gitignore.txt")
      writeFile(projectDir, "CMakeLists.txt", "templates/godotCommon/CMakeLists.txt.ft", projectName)
      writeFile(projectDir, "main.tscn", "templates/godotCommon/main.tscn")
      writeFile(projectDir, "project.godot", "templates/godotEditorExtension/project.godot.ft", projectName)

      // Addon files
      writeFile(addonDir, "plugin.cfg", "templates/godotEditorExtension/addon/plugin.cfg.ft", projectName)
      writeFile(addonDir, "$projectName.gd", "templates/godotEditorExtension/addon/addon.gd.ft", projectName)
      writeFile(addonDir, "README.md", "templates/godotCommon/addon/README.md.ft", projectName)
      iconsDir?.let { writeFile(it, "icon.svg", "templates/godotCommon/addon/icons/icon.svg") }
      binDir?.let { writeFile(it, "$projectName.gdextension", "templates/godotEditorExtension/addon/bin/addon.gdextension.ft", projectName) }

      // Addon/cpp files
      if (cppDir != null) {
        writeFile(cppDir, "CMakeLists.txt", "templates/godotCommon/addon/cpp/CMakeLists.txt.ft", projectName)
        writeFile(cppDir, ".gdignore", "templates/godotCommon/addon/cpp/.gdignore")
        if (srcDir != null) {
          writeFile(srcDir, "register_types.h", "templates/godotCommon/addon/cpp/src/register_types.h")
          writeFile(srcDir, "register_types.cpp", "templates/godotEditorExtension/addon/cpp/src/register_types.cpp")
          writeFile(srcDir, "example_class.h", "templates/godotCommon/addon/cpp/src/example_class.h")
          writeFile(srcDir, "example_class.cpp", "templates/godotCommon/addon/cpp/src/example_class.cpp")
        }
      }

      projectDir.refresh(false, true)
    }
  }

  private fun writeFile(dir: VirtualFile, fileName: String, resourcePath: String, projectName: String? = null) {
    val content = loadTemplate(resourcePath).let {
      if (projectName != null) it.replace("\${PROJECT_NAME}", projectName) else it
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
