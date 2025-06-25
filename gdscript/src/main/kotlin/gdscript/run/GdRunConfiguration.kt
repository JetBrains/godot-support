package gdscript.run

import com.intellij.execution.ExecutionException
import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessHandlerFactory
import com.intellij.execution.process.ProcessTerminatedListener
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import gdscript.utils.getMainProjectBasePath

class GdRunConfiguration : LocatableConfigurationBase<GdRunConfigurationOptions> {

    constructor(project: Project, factory: ConfigurationFactory, name: String) :
        super(project, factory, name)

    override fun getActionName(): String {
        return (options.tscn ?: "").split("/").last().split(".")[0]
    }

    override fun getOptions(): GdRunConfigurationOptions = super.getOptions() as GdRunConfigurationOptions

    var godotExe
        get() = options.godotExe
        set(value) {
            options.godotExe = value
        }

    var tscn
        get() = options.tscn
        set(value) {
            options.tscn = value
        }

    var debugShapes
        get() = options.debugShapes
        set(value) {
            options.debugShapes = value
        }

    var debugPaths
        get() = options.debugPaths
        set(value) {
            options.debugPaths = value
        }

    var arguments
        get() = options.arguments ?: ""
        set(value) {
            options.arguments = value
        }

    override fun suggestedName(): String {
        return options.tscn ?: "".split("/").last().split(".")[0]
    }

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> = GdSettingsEditor()

    override fun checkConfiguration() {}

    override fun getState(executor: Executor, executionEnvironment: ExecutionEnvironment): RunProfileState {
        return object : CommandLineState(executionEnvironment) {
            @Throws(ExecutionException::class)
            override fun startProcess(): ProcessHandler {
                val processHandler = ProcessHandlerFactory.getInstance().createColoredProcessHandler(command())
                ProcessTerminatedListener.attach(processHandler)

                return processHandler
            }
        }
    }

    private fun command(): GeneralCommandLine {
        var command = GeneralCommandLine()
            .withExePath(options.godotExe ?: "")
            .withWorkingDirectory(project.getMainProjectBasePath())

        val scene = options.tscn
        if (scene?.isNotEmpty() == true) {
            command = command.withParameters(scene)
        }

        val opts = mutableSetOf<String>()
        opts.addAll(arguments?.split(" ") ?: emptyList())

        if (debugShapes) opts.add(GdCliArguments.DEBUG_SHAPES)
        if (debugPaths) opts.add(GdCliArguments.DEBUG_PATHS)

        command = command.withParameters(opts.toList())

        return command
    }

}
