package com.jetbrains.rider.plugins.godot.lang.service

import com.intellij.icons.AllIcons
import com.intellij.lang.LangBundle
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.NlsActions
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.platform.lang.lsWidget.LanguageServicePopupSection
import com.intellij.platform.lsp.api.LspServer
import com.intellij.platform.lsp.api.LspServerState
import com.intellij.platform.lsp.api.lsWidget.LspServerWidgetItem
import com.jetbrains.rider.model.godot.frontendBackend.LanguageServerConnectionMode
import com.jetbrains.rider.plugins.godot.GodotIcons
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.plugins.godot.GodotProjectDiscoverer
import com.jetbrains.rider.plugins.godot.actions.StartGodotEditorAction
import javax.swing.Icon

class GodotLspServerWidgetItem(lspServer: LspServer,
                               currentFile: VirtualFile?,
                               icon: Icon = AllIcons.Json.Object,
                               settingsPageClass: Class<out Configurable>? = null) : LspServerWidgetItem(lspServer, currentFile, icon, settingsPageClass) {

    override val widgetActionText: @NlsActions.ActionText String
        get() = when (lspServer.state) {
            LspServerState.Initializing -> LangBundle.message("language.services.widget.item.initializing", serverLabel)
            LspServerState.Running -> serverLabel
            LspServerState.ShutdownNormally -> LangBundle.message("language.services.widget.item.shutdown.normally", serverLabel)
            LspServerState.ShutdownUnexpectedly -> GodotPluginBundle.message("language.services.widget.item.shutdown.unexpectedly", serverLabel)
        }

    override fun createAdditionalInlineActions(): List<AnAction> {
        if (widgetActionLocation == LanguageServicePopupSection.ForCurrentFile &&
            lspServer.state in arrayOf(LspServerState.ShutdownNormally, LspServerState.ShutdownUnexpectedly)
        ) {
            val discoverer = GodotProjectDiscoverer.getInstance(lspServer.project)
            if (discoverer.lspConnectionMode.value == LanguageServerConnectionMode.ConnectRunningEditor)
                return listOf(StartGodotEditorActionInWidget(lspServer))
        }
        return super.createAdditionalInlineActions()
    }

    private class StartGodotEditorActionInWidget(
        private val lspServer: LspServer,
    ) : AnAction(GodotPluginBundle.message("action.StartEditorAction.text"), null, GodotIcons.Actions.StartGodotEditorActionIcon), DumbAware {
        override fun actionPerformed(e: AnActionEvent) {
            val project = lspServer.project
            StartGodotEditorAction.startEditor(project)
            // todo: try to connect, display connecting status to UI
            //if (lspServer.state in arrayOf(LspServerState.ShutdownNormally, LspServerState.ShutdownUnexpectedly)){
            //    val scope = GodotProjectLifetimeService.getScope(project)
            //    scope.launch(Dispatchers.Default) {
            //        delay(1000)
            //        repeat(5){
            //            if (lspServer.state in arrayOf(LspServerState.ShutdownNormally, LspServerState.ShutdownUnexpectedly)) {
            //                GodotLspProjectService.getInstance(project).restartServer()
            //                delay(1000)
            //            }
            //        }
            //    }
            //}
        }
    }
}