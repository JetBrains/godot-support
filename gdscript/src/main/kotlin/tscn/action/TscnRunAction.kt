package tscn.action

import com.intellij.execution.ProgramRunnerUtil
import com.intellij.execution.RunManager
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.icons.AllIcons
import com.intellij.ide.actions.runAnything.RunAnythingAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.psi.PsiFile
import gdscript.psi.utils.PsiGdFileUtil
import gdscript.run.GdConfigurationFactory
import gdscript.run.GdRunConfiguration
import gdscript.run.GdRunConfigurationType

class TscnRunAction : RunAnythingAction {

    val file: PsiFile

    constructor(file: PsiFile) {
        this.file = file
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
        return file.name
    }

    private fun prepareAction(): RunnerAndConfigurationSettings? {
        val name = getName()
        val configuration = RunManager.getInstance(file.project)
            .getConfigurationSettingsList(GdRunConfigurationType::class.java)
            .find { it.configuration is GdRunConfiguration && it.name == name }

        if (configuration != null) {
            return configuration
        }

        val manager: RunManager = RunManager.getInstance(file.project)
        val template = manager.getConfigurationTemplate(GdConfigurationFactory).configuration as GdRunConfiguration
        val current = GdRunConfiguration(file.project, GdConfigurationFactory, name)

        current.tscn = PsiGdFileUtil.filepath(file)
        current.godotExe = template.godotExe
        val action: RunnerAndConfigurationSettings = manager.createConfiguration(current, GdConfigurationFactory)
        action.isTemporary = true
        manager.addConfiguration(action)
        manager.selectedConfiguration = action

        return action
    }

}
