package gdscript.action

import PluginConstants
import com.intellij.execution.ProgramRunnerUtil
import com.intellij.execution.RunManager
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.icons.AllIcons
import com.intellij.ide.actions.runAnything.RunAnythingAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiNamedElement
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.utils.PsiGdFileUtil
import gdscript.run.GdConfigurationFactory
import gdscript.run.GdRunConfiguration
import gdscript.run.GdRunConfigurationType
import tscn.psi.utils.TscnResourceUtil

class GdRunAction : RunAnythingAction {
    val element: PsiNamedElement

    constructor(element: PsiNamedElement) {
        this.element = element
        templatePresentation.icon = AllIcons.RunConfigurations.TestState.Run
        templatePresentation.text = "Run"
    }

    fun runAction() {
        val forRun = prepareAction()
        if (forRun != null) {
            ProgramRunnerUtil.executeConfiguration(forRun, DefaultRunExecutor.getRunExecutorInstance())
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        runAction()
    }

    private fun getName(): String {
        val className = GdClassNamingIndex.INSTANCE.getInFile(element).firstOrNull()
        if (className != null) {
            return className.classname
        }

        return PsiGdFileUtil.filepath(element.containingFile)
    }

    private fun prepareAction(): RunnerAndConfigurationSettings? {
        val name = getName()
        val project = element.project
        val configuration = RunManager.getInstance(project)
            .getConfigurationSettingsList(GdRunConfigurationType::class.java)
            .find { it.configuration is GdRunConfiguration && it.name == name }

        if (configuration != null) {
            return configuration
        }

        val manager: RunManager = RunManager.getInstance(project)
        val template = manager.getConfigurationTemplate(GdConfigurationFactory).configuration as GdRunConfiguration
        val current = GdRunConfiguration(project, GdConfigurationFactory, name)

        if (template.godotExe == null) {
            createWarning(project, "Godot executable not set in template, please look at installation instructions")
            return null
        }

        val scenes = TscnResourceUtil.findTscnByResources(element)
        if (scenes.isEmpty()) {
            createWarning(project, "No scene file found")
            return null
        }

        current.tscn = scenes.first().containingFile.virtualFile.path
        current.godotExe = template.godotExe
        val action: RunnerAndConfigurationSettings = manager.createConfiguration(current, GdConfigurationFactory)
        action.isTemporary = true
        manager.addConfiguration(action)
        manager.selectedConfiguration = action

        return action
    }

    private fun createWarning(project: Project, message: String) {
        NotificationGroupManager.getInstance()
                .getNotificationGroup(PluginConstants.RUN_NOTIFICATION_GROUP_ID)
                .createNotification("Failed to run due to '$message' ", NotificationType.WARNING)
                .notify(project);
    }

}
