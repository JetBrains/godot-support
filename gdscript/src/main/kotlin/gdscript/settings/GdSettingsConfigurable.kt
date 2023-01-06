package gdscript.settings

import com.intellij.openapi.options.Configurable
import gdscript.library.GdLibraryManager
import gdscript.sdk.GdSdkManager
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
        val settings = GdSettingsState;

        return component?.hidePrivate != settings.hidePrivate
                || component?.sdkPath != settings.sdkPath
    }

    override fun apply() {
        val settings = GdSettingsState;
        settings.hidePrivate = component?.hidePrivate ?: false;
        val oldSdk = settings.sdkPath;
        settings.sdkPath = component?.sdkPath;

        if (oldSdk == settings.sdkPath) return;
//        GdLibraryManager.setUpLibrary(settings.sdkPath);
        GdSdkManager.setupSdkIfNeeded();
        GdSdkManager.setClassPath(settings.sdkPath);
    }

    override fun reset() {
        val settings = GdSettingsState;
        component?.hidePrivate = settings.hidePrivate;
        component?.sdkPath = settings.sdkPath;
    }

    override fun disposeUIResources() {
        component = null;
    }

}
