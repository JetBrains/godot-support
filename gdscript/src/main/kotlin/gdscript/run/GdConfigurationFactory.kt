package gdscript.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.project.Project

class GdConfigurationFactory : ConfigurationFactory {

    companion object {
        val INSTANCE = GdConfigurationFactory(GdRunConfigurationType.INSTANCE);
    }

    constructor(type: ConfigurationType) : super(type);

    override fun getId(): String = GdRunConfigurationType.ID;

    override fun createTemplateConfiguration(project: Project): RunConfiguration =
        GdRunConfiguration(project, this, "GdScript")

    override fun getOptionsClass(): Class<out BaseState?> =
        GdRunConfigurationOptions::class.java;

}
