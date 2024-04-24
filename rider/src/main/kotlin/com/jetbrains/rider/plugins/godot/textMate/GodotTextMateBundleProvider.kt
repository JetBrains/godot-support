package com.jetbrains.rider.plugins.godot.textMate

import com.intellij.openapi.application.PathManager
import com.jetbrains.rider.RiderEnvironment
import org.jetbrains.plugins.textmate.api.TextMateBundleProvider
import java.io.File
import java.io.FileFilter

class GodotTextMateBundleProvider : TextMateBundleProvider {
    override fun getBundles(): List<TextMateBundleProvider.PluginBundle> {
        // regular run (bundled or non-bundled plugin)
        val pluginDirs = arrayListOf(
            File(PathManager.getPluginsPath()),
            File(PathManager.getPreInstalledPluginsPath()))

        var directories = pluginDirs.flatMap { dir ->
            dir.resolve("rider-godot").resolve("dotnet").resolve("bundles")
            .listFiles(FileFilter { it.isDirectory }).orEmpty().toList()}.filterNotNull()

        // fallback for run in the ultimate mono-repo
        if (!directories.any()) {
            directories = RiderEnvironment.getBundledBinDir().resolve("bundles").listFiles(FileFilter { it.isDirectory })
                .orEmpty().toList()}

        val bundles = directories.map{it.resolve("extension")}.filter { it.isDirectory }
            .map { TextMateBundleProvider.PluginBundle(it.name, it.toPath()) }

        return bundles
    }
}