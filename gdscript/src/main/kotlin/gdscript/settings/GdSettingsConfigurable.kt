package gdscript.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import gdscript.library.GdLibraryManager
import javax.swing.JComponent

class GdSettingsConfigurable : Configurable {

    companion object {
        lateinit var PROJECT: Project
    }

    private var component: GdSettingsComponent? = null
    private val project: Project

    constructor(project: Project) {
        this.project = project
        PROJECT = project
    }

    override fun getDisplayName(): String {
        return "GdScript Settings"
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return component?.preferredFocusedComponent()
    }

    override fun createComponent(): JComponent? {
        component = GdSettingsComponent()

        return component?.panel
    }

    override fun isModified(): Boolean {
        val settings = GdSettingsState.getInstance().state;

        return component?.hidePrivate != settings.hidePrivate
                || component?.sdkPath != settings.sdkPath
                || component?.shortTyped != settings.shortTyped
                || component?.shouldType != settings.shouldType
                || component?.annotators != settings.annotators
    }

    override fun apply() {
        val settings = GdSettingsState.getInstance().state
        settings.hidePrivate = component?.hidePrivate ?: true
        settings.shortTyped = component?.shortTyped ?: false
        settings.shouldType = component?.shouldType ?: false
        settings.annotators = component?.annotators ?: GdState.OFF

        val oldSdk = settings.sdkPath
        settings.sdkPath = component?.sdkPath

        if (oldSdk == settings.sdkPath) return
        GdLibraryManager.setUpLibrary(project, settings.sdkPath)
    }

    override fun reset() {
        val settings = GdSettingsState.getInstance().state
        component?.hidePrivate = settings.hidePrivate
        component?.sdkPath = settings.sdkPath
        component?.shortTyped = settings.shortTyped
        component?.shouldType = settings.shouldType
        component?.annotators = settings.annotators
    }

    override fun disposeUIResources() {
        component = null
    }

}
