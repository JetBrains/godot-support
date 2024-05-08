package com.jetbrains.rider.plugins.godot.ui

import com.intellij.ide.ui.laf.darcula.ui.DarculaButtonUI
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import com.intellij.ui.Gray
import com.intellij.ui.dsl.builder.Cell
import kotlinx.coroutines.CoroutineScope
import org.jetbrains.annotations.ApiStatus
import java.awt.Color
import javax.swing.Icon
import javax.swing.JButton


@ApiStatus.Internal
object Banner {
    private val LAZY_ICON = lazy { IconLoader.getIcon("/Icons/banner.png", GdScriptPromoPanel::class.java.classLoader) }

    const val HEIGHT = 230
    val BACKGROUND: Color = Gray.x00
    val ICON: Icon
        get() = LAZY_ICON.value
}

fun <T : JButton> Cell<T>.defaultStyle(): Cell<T> {
    component.putClientProperty(DarculaButtonUI.DEFAULT_STYLE_KEY, true)
    return this
}

internal fun getScope(project: Project) = project.service<InstallerServiceProjectScope>().scope

@Service(Service.Level.PROJECT)
private class InstallerServiceProjectScope(val scope: CoroutineScope)