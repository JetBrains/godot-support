package gdscript.run

import com.intellij.execution.actions.ConfigurationContext
import com.intellij.execution.actions.LazyRunConfigurationProducer
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.openapi.util.Ref
import com.intellij.psi.PsiElement
import gdscript.psi.GdFile
import gdscript.psi.utils.PsiGdFileUtil

class GdRunConfigurationProducer : LazyRunConfigurationProducer<GdRunConfiguration>() {

    override fun setupConfigurationFromContext(
        configuration: GdRunConfiguration,
        context: ConfigurationContext,
        sourceElement: Ref<PsiElement>,
    ): Boolean {
        val psi = context.psiLocation ?: return false;
        if (psi.containingFile !is GdFile) {
            return false;
        }

        configuration.setTscn(PsiGdFileUtil.filepath(psi));

        return true;
    }

    override fun isConfigurationFromContext(configuration: GdRunConfiguration, context: ConfigurationContext): Boolean {
        return true;
    }

    override fun getConfigurationFactory(): ConfigurationFactory =
        GdConfigurationFactory.INSTANCE

}
