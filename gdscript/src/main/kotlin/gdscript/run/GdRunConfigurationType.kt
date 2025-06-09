package gdscript.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.rider.plugins.godot.community.icons.RiderPluginsGodotCommunityIcons
import javax.swing.Icon

class GdRunConfigurationType : ConfigurationType {

    companion object {
        val ID = "GodotRunConfiguration"
        val INSTANCE: GdRunConfigurationType = GdRunConfigurationType()
    }

    override fun getDisplayName(): String = "Godot"

    override fun getConfigurationTypeDescription(): String = "Godot run configuration type"

    override fun getIcon(): Icon = RiderPluginsGodotCommunityIcons.GDScript

    override fun getId(): String = ID

    override fun getConfigurationFactories(): Array<ConfigurationFactory> =
        arrayOf(GdConfigurationFactory)
}