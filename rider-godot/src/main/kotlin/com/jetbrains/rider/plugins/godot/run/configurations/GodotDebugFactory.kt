import com.intellij.execution.configurations.ConfigurationType
import com.intellij.openapi.project.Project
import com.jetbrains.rider.plugins.godot.GodotIcons
import com.jetbrains.rider.plugins.godot.run.configurations.GodotDebugRunConfiguration
import com.jetbrains.rider.run.configurations.DotNetConfigurationFactoryBase

open class GodotDebugFactory(type: ConfigurationType)
    : DotNetConfigurationFactoryBase<GodotDebugRunConfiguration>(type) {

    override fun createTemplateConfiguration(project: Project) = GodotDebugRunConfiguration(project, this)
    override fun isConfigurationSingletonByDefault() = true
    override fun getName() = "Godot Debug"
    override fun getIcon() = GodotIcons.RunConfigurations.StartAndDebug

    // This value gets written to the config file. By default it defers to getName, which is what happened pre-2018.3.
    // Keep the "Godot Debug" value so that we can load configs created by earlier versions, and earlier versions can
    // load this config
    override fun getId() = "Godot Debug"
}