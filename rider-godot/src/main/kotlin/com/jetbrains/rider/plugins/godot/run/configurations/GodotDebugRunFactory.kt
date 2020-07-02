import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfiguration
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfigurationParameters
import org.jetbrains.annotations.NotNull

open class GodotDebugRunFactory(type: ConfigurationType) : ConfigurationFactory(type) {
    override fun getId() = "Start and Debug"

    override fun isConfigurationSingletonByDefault(): Boolean {
        return true
    }

    fun createParameters(): GodotDebugRunConfigurationParameters {
        return GodotDebugRunConfigurationParameters(
                "",
                "",
                "",
                hashMapOf(),
                true,
                false
        )
    }

    override fun createConfiguration(name: String?, template: RunConfiguration): RunConfiguration =
            GodotDebugRunConfiguration(name ?: this.name, template.project, this, createParameters())

    override fun createTemplateConfiguration(@NotNull project: Project): RunConfiguration =
            GodotDebugRunConfiguration(this.name, project, this, createParameters())
}