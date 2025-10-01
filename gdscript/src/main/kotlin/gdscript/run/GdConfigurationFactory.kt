package gdscript.run

import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.components.BaseState
import com.intellij.openapi.project.Project

object GdConfigurationFactory : ConfigurationFactory(GdRunConfigurationType.INSTANCE) {

    override fun getId(): String = GdRunConfigurationType.ID

    override fun createTemplateConfiguration(project: Project): RunConfiguration =
        GdRunConfiguration(project, this, "GdScript")

    override fun getOptionsClass(): Class<out BaseState?> =
        GdRunConfigurationOptions::class.java

}
