package com.jetbrains.rider.plugins.godot.textMate

import com.intellij.openapi.application.PathManager
import org.jetbrains.plugins.textmate.api.TextMateBundleProvider
import java.io.FileFilter

class GodotTextMateBundleProvider : TextMateBundleProvider {
    override fun getBundles(): List<TextMateBundleProvider.PluginBundle> {
        val directories = PathManager.getPluginsDir().resolve("rider-godot").resolve("bundles").toFile()
            .listFiles(FileFilter { it.isDirectory }) ?: return emptyList()

        val bundles = directories.map{it.resolve("extension")}.filter { it.isDirectory }
            .map { TextMateBundleProvider.PluginBundle(it.name, it.toPath()) }

        return bundles
    }
}