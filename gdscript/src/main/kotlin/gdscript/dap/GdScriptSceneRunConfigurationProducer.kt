package gdscript.dap

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.runConfigurationType
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import com.jetbrains.rider.godot.community.run.configurations.GodotRunContextUtil
import com.jetbrains.rider.godot.community.utils.GodotCommunityUtil

class GdScriptSceneRunConfigurationProducer : LazyRunConfigurationProducer<GdScriptRunConfiguration>() {
    override fun getConfigurationFactory() = runConfigurationType<GdScriptConfigurationType>().factory

    override fun isConfigurationFromContext(configuration: GdScriptRunConfiguration, context: ConfigurationContext): Boolean {
        val isPureGdScriptProject = GodotCommunityUtil.isPureGdScriptProject(context.project) ?: return false
        if (!isPureGdScriptProject) return false
        val basePath = GodotCommunityUtil.getGodotProjectBasePath(context.project) ?: return false
        val resPath = GodotRunContextUtil.getSceneResPathFromContext(basePath, context) ?: return false
        return configuration.structured.scene == resPath
    }

    override fun setupConfigurationFromContext(configuration: GdScriptRunConfiguration,
                                               context: ConfigurationContext,
                                               sourceElement: Ref<PsiElement>): Boolean {
        val file = GodotRunContextUtil.getTscnFileFromContext(context) ?: return false
        val isPureGdScriptProject = GodotCommunityUtil.isPureGdScriptProject(context.project) ?: return false
        if (!isPureGdScriptProject) return false
        val basePath = GodotCommunityUtil.getGodotProjectBasePath(context.project) ?: return false
        val resPath = GodotRunContextUtil.getSceneResPathFromContext(basePath, context) ?: return false

        val structured = GdScriptStructuredArguments(
            configuration.structured.request,
            configuration.structured.debugServerPort,
            configuration.structured.remainingArguments,
            resPath
        )
        configuration.json = GdScriptRunConfigurationHelper.serialize(structured)
        configuration.name = file.name
        return true
    }
}
