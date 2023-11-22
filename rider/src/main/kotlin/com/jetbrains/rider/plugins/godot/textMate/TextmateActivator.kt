package com.jetbrains.rider.plugins.godot.textMate

import com.intellij.openapi.application.PathManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import org.jetbrains.plugins.textmate.TextMateService
import org.jetbrains.plugins.textmate.actions.Plugin
import org.jetbrains.plugins.textmate.configuration.TextMateUserBundlesSettings
import kotlin.io.path.pathString

class TextmateActivator : ProjectActivity {

    private val logger = Logger.getInstance(TextmateActivator::class.java)
    override suspend fun execute(project: Project) {
        val extensionDir = PathManager.getPluginsDir().resolve("rider-godot").resolve("godot-tools").resolve("extension")
        val settings = TextMateUserBundlesSettings.instance ?: return

        if (!settings.bundles.any { it.key == extensionDir.pathString }) {
            logger.info("Attempt to apply Textmate bundle $extensionDir")
            val plugin = Plugin("godot-tools", "geequlim", "")
            TextMateUserBundlesSettings.instance!!.addBundle(extensionDir.pathString, plugin.toString())
            TextMateService.getInstance().reloadEnabledBundles()
        }
    }
}