package model.frontendBackend

import com.jetbrains.rider.model.nova.ide.SolutionModel
import com.jetbrains.rd.generator.nova.*
import com.jetbrains.rd.generator.nova.PredefinedType.*
import com.jetbrains.rd.generator.nova.csharp.CSharp50Generator
import com.jetbrains.rd.generator.nova.kotlin.Kotlin11Generator

@Suppress("unused")
object FrontendBackendModel : Ext(SolutionModel.Solution) {

    init {
        setting(Kotlin11Generator.Namespace, "com.jetbrains.rider.model.godot.frontendBackend")
        setting(CSharp50Generator.Namespace, "JetBrains.Rider.Model.Godot.FrontendBackend")

        // Misc backend/fronted context
        property("hasGodotReference", bool).documentation = "True when the current project is a Godot project. Either full Godot project or class library"
    }
}
