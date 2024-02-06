//package com.jetbrains.rider.plugins.godot.textMate
//
//import com.intellij.openapi.application.PathManager
//import com.intellij.openapi.project.Project
//import com.intellij.openapi.startup.ProjectActivity
//import org.jetbrains.plugins.textmate.TextMateService
//import org.jetbrains.plugins.textmate.actions.Plugin
//import org.jetbrains.plugins.textmate.configuration.TextMateUserBundlesSettings
//import java.io.FileFilter
//
//class TextmateActivator : ProjectActivity {
//    override suspend fun execute(project: Project) {
//        val settings = TextMateUserBundlesSettings.instance ?: return
//        val directories = PathManager.getPluginsDir().resolve("rider-godot").resolve("bundles").toFile()
//            .listFiles(FileFilter { it.isDirectory })?.toSortedSet()?.sorted()?: return
//        directories.forEach {
//            if (it != null && it.isDirectory) {
//                val extensionDir = it.resolve("extension")
//                if (extensionDir.exists() && !settings.bundles.any { bundle-> bundle.key == extensionDir.path }) {
//                    val plugin = Plugin(it.name, "", "")
//
//                    // workaround for IDEA-342823 Apply 2 textmate bundles
//                    // ".gdshader", ".gdshaderinc", "*.gdshader", "*.gdshaderinc"
//                    if (it.name == "godot-tools") {
//                        val packageJson = extensionDir.resolve("package.json")
//                        if (packageJson.exists()) {
//                            val text = packageJson.readText()
//                                .replace("\".gdshader\"", "\".gdshader2\"")
//                                .replace("\".gdshaderinc\"", "\".gdshaderinc2\"")
//                                .replace("\"*.gdshader\"", "\"*.gdshader2\"")
//                                .replace("\"*.gdshader\"", "\"*.gdshader2\"")
//
//                            packageJson.writeText(text)
//                        }
//                    }
//                    // end workaround
//
//                    TextMateUserBundlesSettings.instance!!.addBundle(extensionDir.path, plugin.toString())
//                    TextMateService.getInstance().reloadEnabledBundles()
//                }
//            }
//        }
//    }
//}