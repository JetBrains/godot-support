package com.jetbrains.rider.plugins.godot.textMate

import com.intellij.openapi.application.PathManager
import com.jetbrains.rider.RiderEnvironment
import org.jetbrains.plugins.textmate.api.TextMateBundleProvider
import java.io.FileFilter

class GodotTextMateBundleProvider : TextMateBundleProvider {
    override fun getBundles(): List<TextMateBundleProvider.PluginBundle> {
        // for regular run
        var directories = PathManager.getPluginsDir().resolve("rider-godot").resolve("dotnet").resolve("bundles")
                              .toFile().listFiles(FileFilter { it.isDirectory })
        // fallback for run in the ultimate mono-repo
        if (directories == null || !directories.any()) {
            directories = RiderEnvironment.getBundledBinDir().resolve("bundles").listFiles(FileFilter { it.isDirectory })
                          ?: return emptyList()
        }

        val bundles = directories.map{it.resolve("extension")}.filter { it.isDirectory }
            .map { TextMateBundleProvider.PluginBundle(it.name, it.toPath()) }

        return bundles
    }
}