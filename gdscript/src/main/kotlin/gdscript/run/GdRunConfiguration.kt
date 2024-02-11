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

class GdRunConfiguration : LocatableConfigurationBase<GdRunConfigurationOptions> {


    constructor(project: Project, factory: ConfigurationFactory, name: String) :
        super(project, factory, name)

    override fun getActionName(): String {
        return getTscn().split("/").last().split(".")[0]
    }

    override fun getOptions(): GdRunConfigurationOptions =
         super.getOptions() as GdRunConfigurationOptions

    fun getGodotExe(): String {
        return options.getGodotExe()
    }

    fun setGodotExe(exe: String) {
        options.setGodotExe(exe)
    }

    fun getTscn(): String {
        return options.getTscn()
    }

    fun setTscn(file: String) {
        options.setTscn(file)
    }

    fun isDebugShapes(): Boolean {
        return options.getDebugShapes()
    }

    fun setDebugShapes(debugShapes: Boolean) {
        options.setDebugShapes(debugShapes)
    }

    override fun suggestedName(): String {
        return getTscn().split("/").last().split(".")[0]
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
            .withExePath(getGodotExe())
            .withWorkDirectory(project.basePath)

        val scene = getTscn()
        if (scene.isNotEmpty()) {
            command = command.withParameters(scene)
        }

        if (isDebugShapes()) {
            command = command.withParameters("--debug-collisions")
        }

        return command
    }

}
