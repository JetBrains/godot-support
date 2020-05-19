import com.intellij.execution.configurations.ConfigurationType
import com.intellij.openapi.project.Project
import com.jetbrains.rider.plugins.godot.GodotIcons
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfiguration
import com.jetbrains.rider.run.configurations.DotNetConfigurationFactoryBase

open class GodotDebugFactory(type: ConfigurationType)
    : DotNetConfigurationFactoryBase<GodotDebugRunConfiguration>(type) {

    override fun createTemplateConfiguration(project: Project) = GodotDebugRunConfiguration(project, this)
    override fun isConfigurationSingletonByDefault() = true
    override fun getIcon() = GodotIcons.RunConfigurations.StartAndDebug

    // This value gets written to the config file. By default it defers to getName, however,
    // the value needs to be kept the same even if the display name changes in the future
    // in order to maintain compatibility with older configs.
    override fun getId() = "Start and Debug"
}