package gdscript.action

import com.intellij.execution.ProgramRunnerUtil
import com.intellij.execution.RunManager
import com.intellij.execution.RunnerAndConfigurationSettings
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.icons.AllIcons
import com.intellij.ide.actions.runAnything.RunAnythingAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.search.GlobalSearchScope
import gdscript.GdKeywords
import gdscript.index.impl.GdClassNamingIndex
import gdscript.psi.utils.PsiGdFileUtil
import gdscript.run.GdConfigurationFactory
import gdscript.run.GdRunConfiguration
import gdscript.run.GdRunConfigurationType
import tscn.index.impl.TscnResourceIndex

class GdRunAction : RunAnythingAction {
    val element: PsiNamedElement;

    constructor(element: PsiNamedElement) {
        this.element = element;
        templatePresentation.icon = AllIcons.RunConfigurations.TestState.Run;
        templatePresentation.text = "Run"
    }

    fun runAction() {
        val forRun = prepareAction();
        if (forRun != null) {
            ProgramRunnerUtil.executeConfiguration(forRun, DefaultRunExecutor.getRunExecutorInstance())
        }
    }

    override fun actionPerformed(e: AnActionEvent) {
        runAction();
    }

    private fun getName(): String {
        val className = GdClassNamingIndex.INSTANCE.getInFile(element).firstOrNull();
        if (className != null) {
            return className.classname;
        }

        return PsiGdFileUtil.filepath(element.containingFile);
    }

    private fun prepareAction(): RunnerAndConfigurationSettings? {
        val name = getName();
        val configuration = RunManager.getInstance(element.project)
            .getConfigurationSettingsList(GdRunConfigurationType::class.java)
            .find { it.configuration is GdRunConfiguration && it.name == name };

        if (configuration != null) {
            return configuration;
        }

        val manager: RunManager = RunManager.getInstance(element.project);
        val template = manager.getConfigurationTemplate(GdConfigurationFactory).configuration as GdRunConfiguration;
        val current = GdRunConfiguration(element.project, GdConfigurationFactory, name);

        val filename = PsiGdFileUtil.filepath(element);
        val script = TscnResourceIndex.INSTANCE.get("${GdKeywords.RESOURCE_PREFIX}$filename", element.project, GlobalSearchScope.allScope(element.project))
            .firstOrNull() ?: return null;

        current.setTscn(PsiGdFileUtil.filepath(script.containingFile));
        current.setGodotExe(template.getGodotExe());
        val action: RunnerAndConfigurationSettings = manager.createConfiguration(current, GdConfigurationFactory)
        action.isTemporary = true
        manager.addConfiguration(action)
        manager.selectedConfiguration = action;

        return action;
    }
}
