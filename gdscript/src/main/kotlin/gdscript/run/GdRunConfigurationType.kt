package gdscript.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import gdscript.GdIcon
import javax.swing.Icon

class GdRunConfigurationType : ConfigurationType {

    companion object {
        val ID = "GodotRunConfiguration";
        val INSTANCE = GdRunConfigurationType();
    }

    override fun getDisplayName(): String = "Godot";

    override fun getConfigurationTypeDescription(): String = "Godot run configuration type";

    override fun getIcon(): Icon = GdIcon.FILE;

    override fun getId(): String = ID;

    override fun getConfigurationFactories(): Array<ConfigurationFactory> =
        arrayOf(GdConfigurationFactory(this));

}