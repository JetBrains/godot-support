package gdscript.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import gdscript.GdIcon
import javax.swing.Icon

object GdRunConfigurationType : ConfigurationType {

    val ID = "GodotRunConfiguration";

    override fun getDisplayName(): String = "Godot";

    override fun getConfigurationTypeDescription(): String = "Godot run configuration type";

    override fun getIcon(): Icon = GdIcon.FILE;

    override fun getId(): String = ID;

    override fun getConfigurationFactories(): Array<ConfigurationFactory> =
        arrayOf(GdConfigurationFactory);

}