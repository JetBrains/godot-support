package com.jetbrains.rider.plugins.godot

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

// IJ will automatically add `_dark` to the basename of the SVG file if in Dark theme.

class GodotIcons {
    class Icons {
        companion object {
            val GodotLogo = IconLoader.getIcon("resharper/GodotLogo/Godot.svg", GodotIcons::class.java)
            val GodotLogoDisconnected = IconLoader.getIcon("/Icons/GodotDisconnected.svg", GodotIcons::class.java)
        }
    }

    class Actions {
        companion object {
            @JvmField val StartGodotEditorActionIcon = AllIcons.Actions.Execute
        }
    }

    class RunConfigurations {
        companion object {
            val StartAndDebug = Icons.GodotLogo
        }
    }

    class GdScriptIcons {
        companion object {
            /** 16x16  */
            val AiAssistant: Icon = IconLoader.getIcon("Icons/gdScript/aiAssistant.svg", GdScriptIcons::class.java)

            /** 16x16  */
            val Completion: Icon = IconLoader.getIcon("Icons/gdScript/completion.svg", GdScriptIcons::class.java)

            /** 16x16  */
            val Preview: Icon = IconLoader.getIcon("Icons/gdScript/preview.svg", GdScriptIcons::class.java)

            /** 16x16  */
            val Refactor: Icon = IconLoader.getIcon("Icons/gdScript/refactor.svg", GdScriptIcons::class.java)

            /** 16x16  */
            val Search: Icon =IconLoader.getIcon("Icons/gdScript/search.svg", GdScriptIcons::class.java)
        }
    }
}