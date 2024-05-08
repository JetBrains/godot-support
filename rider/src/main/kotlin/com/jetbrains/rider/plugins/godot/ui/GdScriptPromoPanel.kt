package com.jetbrains.rider.plugins.godot.ui

import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.popup.IconButton
import com.intellij.ui.InplaceButton
import com.intellij.ui.ScrollPaneFactory
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.dsl.builder.*
import com.intellij.ui.dsl.gridLayout.UnscaledGaps
import com.intellij.ui.dsl.gridLayout.UnscaledGapsY
import com.intellij.util.ui.JBDimension
import com.intellij.util.ui.JBFont
import com.jetbrains.rider.plugins.godot.GodotIcons
import com.jetbrains.rider.plugins.godot.GodotPluginBundle
import org.jetbrains.annotations.Nls
import java.awt.BorderLayout
import java.awt.Color
import javax.swing.Icon
import javax.swing.JLabel

class GdScriptPromoPanel(project: Project) : GdScriptInstallWizardPanel(project) {
    init {
        add(createHeader(), BorderLayout.PAGE_START)
        add(createScrollPane())
    }

    private fun createHeader() = panel {
        row {
            label(GodotPluginBundle.message("promo.label.install.gdscript.plugin")).resizableColumn()
            cell(InplaceButton(IconButton(GodotPluginBundle.message("promo.tooltip.close"),
                                          AllIcons.Ide.Notification.Close, AllIcons.Ide.Notification.CloseHover)) {
                this@GdScriptPromoPanel.isVisible = false
            }.setFillBg(false)).customize(UnscaledGaps(6,6,6,6))
        }
        row {
            cell(object : JLabel(Banner.ICON){
                override fun setBackground(bg: Color?) {
                    // Deny changing background by the tool window framework
                    super.setBackground(Banner.BACKGROUND)
                }
            })
                .customize(UnscaledGaps.EMPTY)
                .align(AlignX.FILL)
                .applyToComponent {
                    minimumSize = JBDimension(1, Banner.HEIGHT)
                    preferredSize = JBDimension(1, Banner.HEIGHT)
                    isOpaque = true
                }
        }
    }

    private fun createScrollPane(): JBScrollPane {
        val scrollPane = ScrollPaneFactory.createScrollPane(createPanel(), true) as JBScrollPane
        scrollPane.isOverlappingScrollBar = true
        return scrollPane
    }

    private fun createPanel() = panel {
        row {
            panel {
                row {
                    text(GodotPluginBundle.message("promo.panel.title"))
                        .applyToComponent {
                            font = JBFont.h3().asBold()
                            putClientProperty(DslComponentProperty.VERTICAL_COMPONENT_GAP, VerticalComponentGap(bottom = false))
                        }
                }.mediumSpaceAfter()

                row {
                    text(
                        GodotPluginBundle.message("promo.label.gdscript.promo"))
                        .applyToComponent {
                            putClientProperty(DslComponentProperty.VERTICAL_COMPONENT_GAP, VerticalComponentGap(bottom = false))
                        }
                }.mediumSpaceAfter()

                createButtonsPanel()
                for (suggestion in activateSuggestions) {
                    row {
                        icon(suggestion.icon)
                            .gap(RightGap.SMALL)
                            .applyToComponent {
                                putClientProperty(DslComponentProperty.VERTICAL_COMPONENT_GAP, VerticalComponentGap(false, false))
                            }
                        text(suggestion.text, maxLineLength = MAX_LINE_LENGTH_NO_WRAP)
                    }
                }
                row {
                    text(GodotPluginBundle.message("promo.panel.learn.more"))
                }.customize(UnscaledGapsY(bottom = 18))
                createBottomPanel()
            }.align(Align.FILL)
                .customize(UnscaledGaps(16, 32, 16, 32))
        }.resizableRow()
    }

    private fun Panel.createBottomPanel() {
        row {
            panel {
                row {
                    text(GodotPluginBundle.message("promo.panel.promo.foot.note"))
                        .align(AlignY.TOP)
                }
            }.align(AlignY.BOTTOM)
        }.resizableRow()
    }

    companion object {
        val activateSuggestions = listOf(
            ActivateSuggestion(GodotIcons.GdScriptIcons.Refactor, GodotPluginBundle.message("promo.refactoring")),
            ActivateSuggestion(GodotIcons.GdScriptIcons.Search, GodotPluginBundle.message("promo.find.usages")),
            ActivateSuggestion(GodotIcons.GdScriptIcons.Preview, GodotPluginBundle.message("promo.scene.preview")),
            ActivateSuggestion(GodotIcons.GdScriptIcons.Completion, GodotPluginBundle.message("promo.smart.autocompletion")),
            ActivateSuggestion(GodotIcons.GdScriptIcons.AiAssistant, GodotPluginBundle.message("promo.ai.assistant.integration"))
        )
    }

    data class ActivateSuggestion(
        val icon: Icon,
        val text: @Nls String
    )
}