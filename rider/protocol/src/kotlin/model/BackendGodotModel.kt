package model

import com.jetbrains.rider.model.nova.ide.SolutionModel
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

        // Actions called from Godot to the backend
        callback("openFileLineCol", RdOpenFileArgs, bool).documentation = "Called from Godot to quickly open a file in an existing Rider instance"

    }
}
