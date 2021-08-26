package model.debuggerWorkerGodot

import com.jetbrains.rd.generator.nova.*
import com.jetbrains.rd.generator.nova.PredefinedType.*
import com.jetbrains.rd.generator.nova.csharp.CSharp50Generator
import com.jetbrains.rd.generator.nova.kotlin.Kotlin11Generator
import com.jetbrains.rider.model.nova.debugger.main.DebuggerWorkerModel

@Suppress("unused")
object GodotDebuggerWorkerModel : Ext(DebuggerWorkerModel) {

    init {
        setting(Kotlin11Generator.Namespace, "com.jetbrains.rider.plugins.godot.model.debuggerWorker")
        setting(CSharp50Generator.Namespace, "JetBrains.Debugger.Model.Plugins.Godot")

        property("showCustomRenderers", bool)
    }
}