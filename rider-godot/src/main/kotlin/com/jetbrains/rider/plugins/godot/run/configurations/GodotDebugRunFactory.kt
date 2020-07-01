import com.intellij.execution.BeforeRunTask
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Key
import com.jetbrains.rider.build.tasks.BuildSolutionBeforeRunTask
import com.jetbrains.rider.build.tasks.BuildSolutionBeforeRunTaskProvider
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfigurationParameters
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfiguration
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

    override fun createTemplateConfiguration(@NotNull project: Project): RunConfiguration =
            GodotDebugRunConfiguration(this.name, project, this, createParameters())
}