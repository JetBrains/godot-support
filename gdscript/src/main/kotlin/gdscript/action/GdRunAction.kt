package gdscript.action

import PluginConstants
import com.intellij.execution.ProgramRunnerUtil
import com.intellij.execution.RunManager
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.icons.AllIcons
import com.intellij.ide.actions.runAnything.RunAnythingAction
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.utils.PsiGdFileUtil
import gdscript.run.GdConfigurationFactory
import gdscript.run.GdRunConfiguration
import gdscript.run.GdRunConfigurationType
import gdscript.utils.isRiderGodotSupportPluginInstalled
import tscn.psi.utils.TscnResourceUtil

class GdRunAction : RunAnythingAction {
    val element: PsiElement

    constructor(element: PsiElement) {
        this.element = element
        templatePresentation.icon = AllIcons.RunConfigurations.TestState.Run
        templatePresentation.text = GdScriptBundle.message("action.run.text")
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

    override fun update(e: AnActionEvent) {
        //RIDER-126489 Run configuration in the GdScript plugin
        if (PluginManagerCore.isRiderGodotSupportPluginInstalled())
            e.presentation.isEnabledAndVisible = false
        super.update(e)
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
            createWarning(project, GdScriptBundle.message("godot.executable.not.set"))
            return null
        }

        val scenes = TscnResourceUtil.findTscnByResources(element)
        if (scenes.isEmpty()) {
            createWarning(project, GdScriptBundle.message("no.scene.found"))
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
                .createNotification(GdScriptBundle.message("notification.content.failed.to.run.due.to", message), NotificationType.WARNING)
                .notify(project);
    }

}
