package model.frontendGodot

import com.jetbrains.rd.generator.nova.Ext
import com.jetbrains.rd.generator.nova.PredefinedType
import com.jetbrains.rd.generator.nova.Root
import com.jetbrains.rd.generator.nova.call
import com.jetbrains.rd.generator.nova.const
import com.jetbrains.rd.generator.nova.cpp.Cpp17Generator
import com.jetbrains.rd.generator.nova.doc
import com.jetbrains.rd.generator.nova.field
import com.jetbrains.rd.generator.nova.immutableList
import com.jetbrains.rd.generator.nova.kotlin.Kotlin11Generator
import com.jetbrains.rd.generator.nova.property
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
    private val EnvironmentVariable = structdef("EnvironmentVariable") {
        sourceFileAndLine = null
        field("name", PredefinedType.string)
        field("value", PredefinedType.string)
    }
    private val DebugSessionStatus = enum("DebugSessionStatus") {
        sourceFileAndLine = null
        +"Ready"
        +"Busy"
        +"Failed"
    }
    private val DebugSession = structdef("DebugSession") {
        sourceFileAndLine = null
        field("status", DebugSessionStatus)
        field("sessionId", PredefinedType.string)
        field("environment", immutableList(EnvironmentVariable))
    }

    init {
        sourceFileAndLine = null
        sink("openInGodot", PredefinedType.string)
            .doc(
                "Opens the given file in Godot. For files such as shaders, scenes,... it will open them inside the scene tree, shader editor... " +
                    "For files that cannot be opened, will be highlighted inside the filesystem dock."
            )
        source("openInRider", PredefinedType.string).doc("Opens a given file in Rider editor.")

        source("currentSceneChange", PredefinedType.string).doc("Used for syncing what scene Godot has currently opened.")

        call("prepareDebugSession", PredefinedType.void, DebugSession)
            .doc("Opens a suspended diagnostic port and returns the game environment. Rider attaches when the game connects.")
        source("debugLaunchFailed", PredefinedType.string).doc("Godot could not play the scene for the given session ID.")
        source("debugSceneStopped", PredefinedType.string).doc("Godot stopped the scene for the given session ID.")

        sink("stopPlayingScene", PredefinedType.void).doc("Stops the scene that is currently being played.")

        property("godotPID", PredefinedType.long).doc("Connected GODOT PID")

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
