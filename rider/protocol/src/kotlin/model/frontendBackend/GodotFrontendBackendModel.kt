package model.frontendBackendGodot

import com.jetbrains.rider.model.nova.ide.SolutionModel
import com.jetbrains.rd.generator.nova.*
import com.jetbrains.rd.generator.nova.PredefinedType.*
import com.jetbrains.rd.generator.nova.csharp.CSharp50Generator
import com.jetbrains.rd.generator.nova.kotlin.Kotlin11Generator
import com.jetbrains.rider.model.nova.ide.rider.RunnableProjectsModel

@Suppress("unused")
object GodotFrontendBackendModel : Ext(SolutionModel.Solution) {

    val TestRunnerOutputEvent = structdef("TestRunnerOutputEvent"){
        field("port", int)
        field("type", enum("TestRunnerOutputEventType") {
            +"Error"
            +"Message"
        })
        field("message", string)
    }

    val GodotEditorState = enum {
        +"Disconnected"
        +"Connected"
    }

    val GodotDescriptor = structdef("GodotDescriptor"){
        field("isPureGdScriptProject", bool).documentation = "True for pure GdScript project"
        field("mainProjectBasePath", string).documentation = "Path to the folder with the project.godot"
        field("tfm", RunnableProjectsModel.rdTargetFrameworkId.nullable)
    }

    private val LanguageServerConnectionMode = enum {
        +"Never"
        +"ConnectRunningEditor"
        +"StartEditorHeadless"
    }

    init {
        setting(Kotlin11Generator.Namespace, "com.jetbrains.rider.model.godot.frontendBackend")
        setting(CSharp50Generator.Namespace, "JetBrains.Rider.Model.Godot.FrontendBackend")

        // Actions called from the backend to the frontend
        sink("activateRider", void).documentation = "Tell Rider to bring itself to the foreground. Called when opening a file from Godot"
        callback("startDebuggerServer", void, int).documentation = "Tell the frontend to start listening for debugger. Returns port. Used for debugging unit tests"
        sink("onTestRunnerOutputEvent", TestRunnerOutputEvent).documentation = "Pass output of the game under tests to frontend"

        // Misc backend/fronted context
        property("godotPath", string).documentation = "Path to GodotEditor"

        // Settings stored in the backend
        field("backendSettings", aggregatedef("GodotBackendSettings") {
            property("enableDebuggerExtensions", bool)

            property("lspConnectionMode", LanguageServerConnectionMode)
            property("remoteHostPort", int)
            property("useDynamicPort", bool)
        })

        property("godotDescriptor", GodotDescriptor)
        property("godotInitialized", bool.nullable)

        property("editorState", GodotEditorState)
    }
}
