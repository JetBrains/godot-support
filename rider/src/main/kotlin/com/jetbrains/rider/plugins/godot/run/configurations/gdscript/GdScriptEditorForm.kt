package com.jetbrains.rider.plugins.godot.run.configurations.gdscript

import com.intellij.ui.PortField
import com.intellij.ui.dsl.builder.panel
import com.jetbrains.rd.util.threading.coroutines.async
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import com.jetbrains.rider.plugins.godot.actions.StartGodotEditorAction
import kotlinx.coroutines.Dispatchers
import javax.swing.JPanel
import javax.swing.event.HyperlinkEvent

class GdScriptEditorForm(viewModel: GdScriptViewModel) {
    var panel: JPanel

    private var portField: PortField = PortField(viewModel.port.value)

    init {
        panel = panel {
            indent {
                row{
                    text(GodotPluginBundle.message("editor.should.be.running")){
                        if (it.eventType == HyperlinkEvent.EventType.ACTIVATED) {
                            viewModel.lifetime.async(Dispatchers.Default) {
                                StartGodotEditorAction.startEditor(viewModel.project)
                            }
                        }
                    }
                }
                row(GodotPluginBundle.message("gdscript.editor.form.port")) {
                    cell(portField)
                    browserLink(GodotPluginBundle.message("godot.debug.run.configuration.editor.help.message"),
                                GodotPluginBundle.message("godot.debug.run.configuration.editor.help.link"))
                }
            }
        }

        portField.addChangeListener {
            viewModel.port.set(portField.value as Int)
        }

        viewModel.port.advise(viewModel.lifetime) {
            portField.value = it
        }
    }
}