package com.jetbrains.rider.plugins.godot.run.configurations.gdscript

import com.intellij.openapi.project.Project
import com.jetbrains.rd.util.lifetime.Lifetime
import com.jetbrains.rd.util.reactive.Property

class GdScriptViewModel(val lifetime: Lifetime, val project: Project) {
    val port: Property<Int> = Property(6006)
}