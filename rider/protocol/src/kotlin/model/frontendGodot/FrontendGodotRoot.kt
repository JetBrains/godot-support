package model.frontendGodot

import com.jetbrains.rd.generator.nova.Ext
import com.jetbrains.rd.generator.nova.PredefinedType
import com.jetbrains.rd.generator.nova.Root
import com.jetbrains.rd.generator.nova.callback
import com.jetbrains.rd.generator.nova.const
import com.jetbrains.rd.generator.nova.cpp.Cpp17Generator
import com.jetbrains.rd.generator.nova.doc
import com.jetbrains.rd.generator.nova.kotlin.Kotlin11Generator
import com.jetbrains.rd.generator.nova.setting
import com.jetbrains.rd.generator.nova.sink
import com.jetbrains.rd.generator.nova.source

@Suppress("unused")
object FrontendGodotRoot : Root() {
    init {
        sourceFileAndLine = null
        setting(Kotlin11Generator.Namespace, "com.jetbrains.rider.model.godot.frontendGodot")
        setting(Cpp17Generator.Namespace, "JetBrains::GodotPlugin")
        setting(Cpp17Generator.GeneratePrecompiledHeaders, false)
        setting(Cpp17Generator.UsePrecompiledHeaders, false)
    }
}

/**
 * Note: Godot is the server and Rider is the client.
 */
@Suppress("unused")
object FrontendGodotModel : Ext(FrontendGodotRoot) {
    init {
        sourceFileAndLine = null
        sink("openInGodot", PredefinedType.string)
            .doc(
                "Opens the given file in Godot. For files such as shaders, scenes,... it will open them inside the scene tree, shader editor... " +
                    "For files that cannot be opened, will be highlighted inside the filesystem dock."
            )
        source("openInRider", PredefinedType.string).doc("Opens a given file in Rider editor.")

        source("currentSceneChange", PredefinedType.string).doc("Used for syncing what scene Godot has currently opened.")

        // Debug current C# scene in editor
        callback("playCurrentSceneForDebug", PredefinedType.string, PredefinedType.bool)
            .doc(
                "Plays the current scene and adds the specified argument to the game command line. " +
                    "Returns true if Godot starts the scene."
            )
        sink("stopPlayingScene", PredefinedType.void).doc("Stops the scene that is currently being played.")
        const("diagnosticPortsArgument", PredefinedType.string, "--rider-diagnostic-ports")
            .doc(
                "Flag for passing DOTNET_DiagnosticPorts to the game. This argument is required because Godot does not pass " +
                    "the editor environment on macOS. The game extension sets the variable before .NET starts."
            )

        const(
            "portFilename",
            PredefinedType.string,
            "rider_ide_server.cfg"
        ).doc("Name for a file, into which Godot writes its server port.")
        const("portKey", PredefinedType.string, "port")
            .doc("Name of key inside portFilename for the server port.")
        const("modelHashKey", PredefinedType.string, "modelHash")
            .doc("Name of key for inside portFilename for the model hash of the server.")
    }
}
