package com.jetbrains.rider.plugins.godot.lang.service

import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.reactive.IProperty
import com.jetbrains.rd.util.reactive.Property
import com.jetbrains.rd.util.reactive.adviseNotNull
import com.jetbrains.rider.godot.community.ProjectInfoProvider
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.plugins.godot.GodotProjectLifetimeService

class GodotProjectInfoProvider : ProjectInfoProvider {
    override fun isGodotProject(project: Project): Boolean {
        return GodotProjectDiscoverer.Companion.getInstance(project).godotPath.value != null
    }

    override fun getMainProjectBasePathProperty(project: Project): IProperty<String?> {
        val mainProjectPathProperty = Property<String?>(null)
        val lifetime = GodotProjectLifetimeService.getLifetime(project)
        GodotProjectDiscoverer.Companion.getInstance(project).godotDescriptor.adviseNotNull(lifetime){ mainProjectPathProperty.set(it.mainProjectBasePath) }
        return mainProjectPathProperty
    }
}