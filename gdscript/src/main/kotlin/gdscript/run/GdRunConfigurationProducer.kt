package gdscript.run

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.ide.plugins.PluginManagerCore
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import gdscript.psi.GdFile
import gdscript.psi.utils.PsiGdFileUtil
import gdscript.utils.isRiderGodotSupportPluginInstalled
import tscn.psi.TscnFile

class GdRunConfigurationProducer : LazyRunConfigurationProducer<GdRunConfiguration>() {

    override fun setupConfigurationFromContext(
        configuration: GdRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>,
    ): Boolean {
        if (PluginManagerCore.isRiderGodotSupportPluginInstalled())
            return false

        val psi = context.psiLocation ?: return false
        if (psi.containingFile is GdFile || psi.containingFile is TscnFile) {
            configuration.tscn = PsiGdFileUtil.filepath(psi)
            configuration.name = configuration.actionName
        } else {
            return false
        }

        return true
    }

    override fun isConfigurationFromContext(configuration: GdRunConfiguration, context: ConfigurationContext): Boolean {
        return true
    }

    override fun getConfigurationFactory(): ConfigurationFactory {

        return GdConfigurationFactory
    }

}
