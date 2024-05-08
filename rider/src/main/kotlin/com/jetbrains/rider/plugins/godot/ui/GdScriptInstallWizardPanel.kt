package com.jetbrains.rider.plugins.godot.ui

import com.intellij.icons.AllIcons
import com.intellij.ide.IdeBundle
import com.intellij.ide.plugins.PluginEnabler
import com.intellij.ide.plugins.PluginInstallOperation
import com.intellij.ide.plugins.PluginNode
import com.intellij.ide.plugins.RepositoryHelper
import com.intellij.ide.plugins.marketplace.MarketplaceRequests
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.application.EDT
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.asContextElement
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.progress.util.AbstractProgressIndicatorExBase
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.ui.popup.IconButton
import com.intellij.platform.util.coroutines.childScope
import com.intellij.ui.InplaceButton
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.dsl.gridLayout.UnscaledGapsY
import com.intellij.ui.layout.predicate
import com.intellij.util.concurrency.ThreadingAssertions
import com.intellij.util.concurrency.annotations.RequiresEdt
import com.intellij.util.ui.RestartDialogImpl
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.plugins.godot.gdscript.GDSCRIPT_PLUGIN_ID
import com.jetbrains.rider.plugins.godot.gdscript.GDSCRIPT_PLUGIN_NAME
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.awt.BorderLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JProgressBar
import javax.swing.SwingConstants

// UI is nearly identical to LLMInstallerWizardPanel

open class GdScriptInstallWizardPanel(val project:Project): JPanel(BorderLayout()){

    private lateinit var state: MutableStateFlow<State>

    private lateinit var progressState: JLabel
    private lateinit var progressBar: JProgressBar
    private lateinit var progressFlow: MutableStateFlow<Int>
    private var progressIndicator: MyProgressIndicator? = null
    private val panelScope = getScope(project).childScope("panelScope")

    fun Panel.createButtonsPanel() {
        state = MutableStateFlow(State.BEGIN)
        progressState = JLabel()
        progressBar = JProgressBar(0, 100)
        progressFlow = MutableStateFlow(0)

        panelScope.launch {
            progressFlow.collect { value ->
                withContext(Dispatchers.EDT + ModalityState.any().asContextElement()) {
                    progressBar.value = value
                }
            }
        }

        row {
            button(GodotPluginBundle.message("promo.panel.wizard.install")) {
                onInstall()
            }.defaultStyle()
                .component
        }.mediumSpaceAfter()
            .visibleIf(state.predicate(panelScope) { it == State.BEGIN })

        row {
            cell(progressBar)
                .align(AlignX.FILL)
                .resizableColumn()
                .label(progressState, LabelPosition.TOP)
            cell(createCancelButton())
                .align(AlignY.BOTTOM)
        }.mediumSpaceAfter()
            .visibleIf(state.predicate(panelScope) { it == State.INSTALLING })

        row {
            button(GodotPluginBundle.message("promo.panel.wizard.restart.ide")) {
                RestartDialogImpl.restartWithConfirmation()
            }.defaultStyle()
                .gap(RightGap.SMALL)
            label(GodotPluginBundle.message("promo.panel.wizard.installed")).applyToComponent {
                icon = AllIcons.General.GreenCheckmark
                horizontalTextPosition = SwingConstants.RIGHT
            }
        }.mediumSpaceAfter()
            .visibleIf(state.predicate(panelScope) { it == State.END })
    }

    private fun createCancelButton(): InplaceButton {
        return InplaceButton(IconButton(GodotPluginBundle.message("promo.panel.wizard.cancel.tooltip"),
                                        AllIcons.Process.Stop, AllIcons.Process.StopHovered)) {
            progressIndicator?.cancelled = true
        }
    }

    private fun onInstall() {
        val newProgressIndicator = MyProgressIndicator()
        progressIndicator = newProgressIndicator
        progressState.text = GodotPluginBundle.message("promo.panel.wizard.downloading")
        progressFlow.value = 0
        progressBar.value = 0
        state.value = State.INSTALLING

        ProgressManager.getInstance().run(object : Task.Backgroundable(project,
                                                                       GodotPluginBundle.message("promo.panel.wizard.task.title")) {
            override fun run(indicator: ProgressIndicator) {
                (indicator as AbstractProgressIndicatorExBase).addStateDelegate(newProgressIndicator)
                val pluginId = GDSCRIPT_PLUGIN_ID
                val marketplacePlugins: List<PluginNode>
                val customPlugins: List<PluginNode>
                val descriptor: PluginNode?

                try {
                    marketplacePlugins = MarketplaceRequests.loadLastCompatiblePluginDescriptors(setOf(pluginId), throwExceptions = true)
                    customPlugins = RepositoryHelper.loadPluginsFromCustomRepositories(indicator)
                    val descriptors = RepositoryHelper.mergePluginsFromRepositories(marketplacePlugins, customPlugins, true)
                    descriptor = descriptors.find { it.pluginId == pluginId }
                }
                catch (e: Exception) {
                    ApplicationManager.getApplication().invokeLater {
                        onInstallError(loadingStage = true, true, e)
                    }
                    return
                }

                if (descriptor == null) {
                    ApplicationManager.getApplication().invokeLater {
                        Messages.showErrorDialog(project, GodotPluginBundle.message("promo.panel.wizard.downloading.not.found.error", GDSCRIPT_PLUGIN_ID),
                                                 IdeBundle.message("title.plugin.installation"))
                        state.value = State.BEGIN
                    }
                    return
                }

                ApplicationManager.getApplication().invokeLater {
                    progressState.text = GodotPluginBundle.message("promo.panel.wizard.installing")
                }

                val operation = PluginInstallOperation(listOf(descriptor), customPlugins, PluginEnabler.HEADLESS, indicator)
                try {
                    operation.run()
                }
                catch (e: Exception) {
                    ApplicationManager.getApplication().invokeLater {
                        onInstallError(loadingStage = false, !operation.isShownErrors, e)
                    }
                    return
                }

                if (operation.isSuccess) {
                    ApplicationManager.getApplication().invokeLater {
                        onInstallSuccess()
                    }
                }
                else {
                    ApplicationManager.getApplication().invokeLater {
                        onInstallError(loadingStage = false, !operation.isShownErrors, null)
                    }
                }
            }
        })
    }


    @RequiresEdt
    private fun onInstallSuccess() {
        ThreadingAssertions.assertEventDispatchThread()

        state.value = State.END
    }

    /**
     * [loadingStage] determines if error while loading or installing
     */
    @RequiresEdt
    private fun onInstallError(loadingStage: Boolean, showError: Boolean, e: Exception?) {
        ThreadingAssertions.assertEventDispatchThread()
        thisLogger().warn("Error while $GDSCRIPT_PLUGIN_NAME installation: $e")

        if (showError) {
            val errorMessage = e?.toString() ?: IdeBundle.message("unknown.error")
            if (loadingStage) {
                Messages.showErrorDialog(project, GodotPluginBundle.message("promo.panel.wizard.downloading.error", GDSCRIPT_PLUGIN_NAME, errorMessage),
                                         IdeBundle.message("title.plugin.installation"))
            }
            else {
                Messages.showErrorDialog(project, IdeBundle.message("error.plugin.was.not.installed", GDSCRIPT_PLUGIN_NAME, errorMessage),
                                         IdeBundle.message("title.plugin.installation"))
            }
        }

        state.value = State.BEGIN
    }

    protected fun <T : Row> T.mediumSpaceAfter(): T {
        customize(UnscaledGapsY(bottom = 12))
        return this
    }

    private inner class MyProgressIndicator : AbstractProgressIndicatorExBase() {

        @Volatile
        var cancelled: Boolean = false

        override fun setFraction(fraction: Double) {
            // Not on EDT here
            progressFlow.value = (fraction * 100).toInt()
        }

        override fun isCancelable(): Boolean {
            return true
        }

        override fun isCanceled(): Boolean {
            return cancelled
        }
    }

    private enum class State {
        BEGIN,
        INSTALLING,
        END
    }
}