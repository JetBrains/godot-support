package model.backendGodot

import com.jetbrains.rd.generator.nova.*
import com.jetbrains.rd.generator.nova.PredefinedType.*
import com.jetbrains.rd.generator.nova.csharp.CSharp50Generator
import com.jetbrains.rd.generator.nova.kotlin.Kotlin11Generator

@Suppress("unused")
object BackendGodotModel : Root()  {

    private var RdOpenFileArgs = structdef {
        field("path", string)
        field("line", int)
        field("col", int)
    }

    init {
        setting(Kotlin11Generator.Namespace, "JetBrains.ReSharper.Plugins.Godot.Protocol")
        setting(CSharp50Generator.Namespace, "JetBrains.ReSharper.Plugins.Godot.Protocol")

        // Actions called from Godot to the backend
        callback("openFileLineCol", RdOpenFileArgs, bool).documentation = "Called from Godot to quickly open a file in an existing Rider instance"
    }
}
