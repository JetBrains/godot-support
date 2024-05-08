package com.jetbrains.rider.plugins.godot.ui

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Splitter
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.rider.plugins.godot.Util
import com.jetbrains.rider.plugins.godot.gdscript.PluginInterop
import org.jetbrains.annotations.NotNull

class GdScriptEditorWithPromotion : FileEditorProvider, DumbAware {

    private val promoShownKey = "gdscript.promo.shown"

    override fun accept(project: Project, file: VirtualFile): Boolean {
        return file.extension == Util.GD
    }

    override fun createEditor(project: Project, file: VirtualFile): FileEditor {

        val textEditor = TextEditorProvider.getInstance().createEditor(project, file) as TextEditor

        val properties = PropertiesComponent.getInstance(project)

        if (!properties.getBoolean(promoShownKey, false) && !PluginInterop.isGdScriptPluginInstalled()) {
            val splitter = Splitter(false, 0.5f)
            val sidePanel = GdScriptPromoPanel(project)
            splitter.firstComponent = textEditor.component
            splitter.secondComponent = sidePanel

            properties.setValue(promoShownKey, true)
            return object : TextEditor by textEditor {
                override fun getComponent() = splitter

                @NotNull
                override fun getFile(): VirtualFile {
                    return file
                }
            }
        }
        else {
            return textEditor
        }
    }

    override fun getEditorTypeId(): String {
        return "GDSCRIPT_WITH_PROMO_EDITOR_TYPE_ID"
    }

    override fun getPolicy(): FileEditorPolicy {
        return FileEditorPolicy.HIDE_OTHER_EDITORS
    }
}