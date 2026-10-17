package com.jetbrains.rider.godot.community

import com.intellij.openapi.project.Project
import com.intellij.util.concurrency.annotations.RequiresReadLock

interface GodotProjectSettingsProvider {
    /**
     * Returns the enabled editor plugin paths from `project.godot`, such as `res://addons/rider-plugin/plugin.cfg`.
     * Returns an empty set if no plugins are enabled, or `null` if the `project.godot` is malformed or the parsing is not available.
     */
    @RequiresReadLock
    fun getEnabledEditorPlugins(project: Project): Set<String>?
}