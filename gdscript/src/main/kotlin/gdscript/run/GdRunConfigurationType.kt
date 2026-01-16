package gdscript.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.rider.plugins.godot.community.icons.RiderPluginsGodotCommunityIcons
import gdscript.GdScriptBundle
import javax.swing.Icon

class GdRunConfigurationType : ConfigurationType {

    companion object {
        val ID = "GodotRunConfiguration"
        val INSTANCE: GdRunConfigurationType = GdRunConfigurationType()
    }

    override fun getDisplayName(): String = GdScriptBundle.message("run.configuration.type.display.name")

    override fun getConfigurationTypeDescription(): String = GdScriptBundle.message("run.configuration.type.description")

    override fun getIcon(): Icon = RiderPluginsGodotCommunityIcons.GDScript

    override fun getId(): String = ID

    override fun getConfigurationFactories(): Array<ConfigurationFactory> =
        arrayOf(GdConfigurationFactory)
}