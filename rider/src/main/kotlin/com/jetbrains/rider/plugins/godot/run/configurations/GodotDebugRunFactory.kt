import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfiguration
import com.jetbrains.rider.run.configurations.exe.ExeConfigurationParameters
import org.jetbrains.annotations.NotNull

open class GodotDebugRunFactory(type: ConfigurationType) : ConfigurationFactory(type) {
    override fun getId() = "Start and Debug"

    override fun createTemplateConfiguration(@NotNull project: Project): RunConfiguration =
            GodotDebugRunConfiguration(this.name, project, this, ExeConfigurationParameters(
                exePath = "",
                programParameters = "",
                workingDirectory = "",
                envs = hashMapOf(),
                isPassParentEnvs = true,
            ))
}