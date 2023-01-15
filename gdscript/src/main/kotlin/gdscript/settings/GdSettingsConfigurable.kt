package gdscript.settings

import com.intellij.openapi.options.Configurable
import javax.swing.JComponent

class GdSettingsConfigurable : Configurable {

    private var component: GdSettingsComponent? = null

    override fun getDisplayName(): String {
        return "GdScript Settings";
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return component?.preferredFocusedComponent();
    }

    override fun createComponent(): JComponent? {
        component = GdSettingsComponent();

        return component?.panel;
    }

    override fun isModified(): Boolean {
        val settings = GdSettingsState.getInstance().state;

        return component?.hidePrivate != settings.hidePrivate
                || component?.sdkPath != settings.sdkPath
    }

    override fun apply() {
        val settings = GdSettingsState.getInstance().state;
        settings.hidePrivate = component?.hidePrivate ?: false;
        settings.sdkPath = component?.sdkPath;
    }

    override fun reset() {
        val settings = GdSettingsState.getInstance().state;
        component?.hidePrivate = settings.hidePrivate;
        component?.sdkPath = settings.sdkPath;
    }

    override fun disposeUIResources() {
        component = null;
    }

}
